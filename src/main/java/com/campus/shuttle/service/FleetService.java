package com.campus.shuttle.service;

import com.campus.shuttle.model.*;

import java.util.List;
import java.util.Optional;

/**
 * Service Layer Interface for Fleet Operations & Business Logic Automation.
 */
public interface FleetService {

    /**
     * FR-1: Register / Onboard a new shuttle with driver and battery percentage.
     */
    Shuttle registerShuttle(String driverName, int initialBattery);

    /**
     * FR-1: Retrieve all shuttles for the fleet dashboard inventory.
     */
    List<Shuttle> getAllShuttles();

    /**
     * FR-2: Fetch deployable shuttles (status = 'AVAILABLE' and battery_pct > 20%).
     */
    List<Shuttle> getDeployableShuttles();

    /**
     * FR-3: Log a route trip, deduct battery, and automatically set status to 'MAINTENANCE'
     * if battery drops below 15%.
     */
    TripResult logTrip(int shuttleId, int routeId, int passengersBoarded);

    /**
     * FR-4: Multi-Table Joined Analytics displaying total completed trips and aggregate passenger counts.
     */
    List<ShuttleAnalyticsDTO> getFleetAnalytics();

    /**
     * FR-5: Manual maintenance override to place any vehicle under maintenance.
     */
    Shuttle placeShuttleUnderMaintenance(int shuttleId);

    /**
     * Release vehicle from maintenance / recharge battery back to AVAILABLE status.
     */
    Shuttle rechargeAndReleaseShuttle(int shuttleId, int chargedBattery);

    /**
     * Retrieve shuttle by ID.
     */
    Optional<Shuttle> getShuttleById(int shuttleId);

    /**
     * Retrieve all campus routes.
     */
    List<Route> getAllRoutes();

    /**
     * Onboard / add a new campus route.
     */
    Route addRoute(String stopName, int sequenceOrder);

    /**
     * Retrieve all logged trips.
     */
    List<TripLog> getAllTripLogs();
}

