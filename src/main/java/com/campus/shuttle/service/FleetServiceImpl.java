package com.campus.shuttle.service;

import com.campus.shuttle.dao.RouteDao;
import com.campus.shuttle.dao.ShuttleDao;
import com.campus.shuttle.dao.TripLogDao;
import com.campus.shuttle.model.*;
import com.campus.shuttle.service.exception.InvalidTripException;
import com.campus.shuttle.service.exception.RouteNotFoundException;
import com.campus.shuttle.service.exception.ShuttleNotDeployableException;
import com.campus.shuttle.service.exception.ShuttleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer implementation for Campus EV Fleet Operations.
 * Enforces business rules, validations, and automated state transitions.
 */
@Service
public class FleetServiceImpl implements FleetService {

    private static final int BATTERY_DEPLETION_PER_TRIP = 10;
    private static final int MAINTENANCE_BATTERY_THRESHOLD = 15;
    private static final int MIN_DEPLOYABLE_BATTERY = 20;

    private final ShuttleDao shuttleDao;
    private final RouteDao routeDao;
    private final TripLogDao tripLogDao;

    @Autowired
    public FleetServiceImpl(ShuttleDao shuttleDao, RouteDao routeDao, TripLogDao tripLogDao) {
        this.shuttleDao = shuttleDao;
        this.routeDao = routeDao;
        this.tripLogDao = tripLogDao;
    }

    /**
     * FR-1: Register / Onboard a new shuttle with driver and battery percentage.
     */
    @Override
    @Transactional
    public Shuttle registerShuttle(String driverName, int initialBattery) {
        if (driverName == null || driverName.trim().isEmpty()) {
            throw new IllegalArgumentException("Driver name cannot be empty.");
        }
        if (initialBattery < 0 || initialBattery > 100) {
            throw new IllegalArgumentException("Initial battery percentage must be between 0 and 100.");
        }

        String initialStatus = (initialBattery < MAINTENANCE_BATTERY_THRESHOLD) ? "MAINTENANCE" : "AVAILABLE";
        Shuttle shuttle = new Shuttle(driverName.trim(), initialBattery, initialStatus);
        shuttleDao.insertShuttle(shuttle);
        return shuttle;
    }

    /**
     * FR-1: Retrieve all shuttles for the fleet dashboard inventory.
     */
    @Override
    public List<Shuttle> getAllShuttles() {
        return shuttleDao.findAllShuttles();
    }

    /**
     * FR-2: Fetch deployable shuttles meeting strict criteria (AVAILABLE and battery_pct > 20%).
     */
    @Override
    public List<Shuttle> getDeployableShuttles() {
        return shuttleDao.findDeployableShuttles();
    }

    /**
     * FR-3: Log a route trip, deduct battery, and automatically set status to 'MAINTENANCE'
     * if battery drops below 15%.
     */
    @Override
    @Transactional
    public TripResult logTrip(int shuttleId, int routeId, int passengersBoarded) {
        if (passengersBoarded <= 0) {
            throw new InvalidTripException("Passengers boarded must be greater than 0.");
        }

        Shuttle shuttle = shuttleDao.findById(shuttleId)
                .orElseThrow(() -> new ShuttleNotFoundException("Shuttle not found with ID: " + shuttleId));

        Route route = routeDao.findById(routeId)
                .orElseThrow(() -> new RouteNotFoundException("Route stop not found with ID: " + routeId));

        // Strict deployability validation (FR-2 criteria)
        if (!"AVAILABLE".equalsIgnoreCase(shuttle.getStatus()) || shuttle.getBatteryPct() <= MIN_DEPLOYABLE_BATTERY) {
            throw new ShuttleNotDeployableException(String.format(
                    "Cannot dispatch Shuttle #%d (%s). Status is '%s' with %d%% battery. " +
                    "Vehicles require 'AVAILABLE' status and > 20%% battery for deployment.",
                    shuttleId, shuttle.getDriverName(), shuttle.getStatus(), shuttle.getBatteryPct()
            ));
        }

        // Deduct battery
        int batteryBefore = shuttle.getBatteryPct();
        int batteryDeducted = BATTERY_DEPLETION_PER_TRIP;
        int batteryAfter = Math.max(0, batteryBefore - batteryDeducted);

        // Automated Business Rule: If battery drops below 15%, auto-update to MAINTENANCE
        boolean autoMaintenance = batteryAfter < MAINTENANCE_BATTERY_THRESHOLD;
        String newStatus = autoMaintenance ? "MAINTENANCE" : "AVAILABLE";

        // Persist trip log
        TripLog tripLog = new TripLog(shuttleId, routeId, passengersBoarded);
        tripLogDao.insertTripLog(tripLog);

        // Update shuttle state
        shuttleDao.updateShuttleBatteryAndStatus(shuttleId, batteryAfter, newStatus);

        return new TripResult(
                tripLog.getLogId() != null ? tripLog.getLogId() : 0,
                shuttleId,
                shuttle.getDriverName(),
                routeId,
                route.getStopName(),
                passengersBoarded,
                batteryBefore,
                batteryDeducted,
                batteryAfter,
                newStatus,
                autoMaintenance
        );
    }

    /**
     * FR-4: Multi-Table Joined Analytics displaying total completed trips and aggregate passenger counts.
     */
    @Override
    public List<ShuttleAnalyticsDTO> getFleetAnalytics() {
        return shuttleDao.getShuttleTripAnalytics();
    }

    /**
     * FR-5: Manual maintenance override to place any vehicle under maintenance.
     */
    @Override
    @Transactional
    public Shuttle placeShuttleUnderMaintenance(int shuttleId) {
        Shuttle shuttle = shuttleDao.findById(shuttleId)
                .orElseThrow(() -> new ShuttleNotFoundException("Shuttle not found with ID: " + shuttleId));

        shuttleDao.updateStatus(shuttleId, "MAINTENANCE");
        shuttle.setStatus("MAINTENANCE");
        return shuttle;
    }

    /**
     * Recharge and release vehicle back to AVAILABLE status.
     */
    @Override
    @Transactional
    public Shuttle rechargeAndReleaseShuttle(int shuttleId, int chargedBattery) {
        if (chargedBattery <= MIN_DEPLOYABLE_BATTERY || chargedBattery > 100) {
            throw new IllegalArgumentException("Recharged battery must be between 21% and 100% to resume AVAILABLE status.");
        }

        Shuttle shuttle = shuttleDao.findById(shuttleId)
                .orElseThrow(() -> new ShuttleNotFoundException("Shuttle not found with ID: " + shuttleId));

        shuttleDao.updateShuttleBatteryAndStatus(shuttleId, chargedBattery, "AVAILABLE");
        shuttle.setBatteryPct(chargedBattery);
        shuttle.setStatus("AVAILABLE");
        return shuttle;
    }

    @Override
    public Optional<Shuttle> getShuttleById(int shuttleId) {
        return shuttleDao.findById(shuttleId);
    }

    @Override
    public List<Route> getAllRoutes() {
        return routeDao.findAllRoutes();
    }

    @Override
    @Transactional
    public Route addRoute(String stopName, int sequenceOrder) {
        if (stopName == null || stopName.trim().isEmpty()) {
            throw new IllegalArgumentException("Stop name cannot be empty.");
        }
        Route route = new Route(stopName.trim(), sequenceOrder);
        routeDao.insertRoute(route);
        return route;
    }

    @Override
    public List<TripLog> getAllTripLogs() {
        return tripLogDao.findAllTripLogs();
    }
}

