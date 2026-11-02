package com.campus.shuttle_management.service;

import com.campus.shuttle_management.model.Shuttle;
import com.campus.shuttle_management.model.ShuttleAnalytics;

import java.util.List;

/**
 * Business contract for the Smart Campus EV Shuttle Fleet.
 *
 * <p>This interface is what the console UI talks to. It has no idea
 * whether data comes from MySQL, a file, or a mock — that is the
 * whole point of the layer boundary. Business rules live here and
 * only here; the DAO layer knows SQL, and the UI layer knows menus.
 *
 * <pre>
 *  Console UI  ──▶  FleetService  ──▶  DAO (ShuttleDao, TripLogDao, AnalyticsDao)
 *  (Member 6)       (Member 5)         (Member 4)
 * </pre>
 *
 * @author Member 5 — Varun
 */
public interface FleetService {

    /**
     * FR-1 — Onboard a new shuttle into the campus fleet.
     * New shuttles always enter with status AVAILABLE.
     *
     * @param driverName full name of the assigned driver
     * @param batteryPct initial battery charge (0–100)
     * @return the database-generated shuttle ID
     * @throws IllegalArgumentException if name is blank or battery is out of range
     */
    int registerShuttle(String driverName, int batteryPct);

    /**
     * FR-1 — Return the full fleet, every shuttle regardless of status.
     */
    List<Shuttle> getFleetDashboard();

    /**
     * FR-2 — Return only shuttles that are ready to dispatch right now.
     * Criteria: status == 'AVAILABLE' AND battery_pct > 20.
     */
    List<Shuttle> getAvailableShuttles();

    /**
     * FR-3 — Dispatch a shuttle on a route and log the completed trip.
     *
     * <p>Rules applied in order:
     * <ol>
     *   <li>Shuttle must exist — throws if not found.</li>
     *   <li>Status must be AVAILABLE — throws if not.</li>
     *   <li>Battery must be {@code > 20%} — throws if not.</li>
     *   <li>Deduct {@code 10%} from battery (floors at 0).</li>
     *   <li>If new battery {@code < 15%} → status becomes MAINTENANCE.</li>
     *   <li>Otherwise → status returns to AVAILABLE.</li>
     *   <li>Trip log insert + shuttle update are atomic (@Transactional).</li>
     * </ol>
     *
     * @param shuttleId         the shuttle to dispatch
     * @param routeId           the route being served
     * @param passengersBoarded passenger count for this trip
     * @throws IllegalArgumentException if the shuttle does not exist
     * @throws IllegalStateException    if the shuttle is not eligible for dispatch
     */
    void dispatchTrip(int shuttleId, int routeId, int passengersBoarded);

    /**
     * FR-5 — Force a shuttle into MAINTENANCE regardless of its current state.
     * Prevents any further dispatches until a dispatcher clears it manually.
     *
     * @param shuttleId the shuttle to flag
     * @throws IllegalArgumentException if the shuttle does not exist
     */
    void flagMaintenanceOverride(int shuttleId);

    /**
     * FR-4 — Fleet analytics: each shuttle with total trips and total passengers.
     * Delegates entirely to AnalyticsDao — no SQL in the service layer.
     */
    List<ShuttleAnalytics> getFleetAnalytics();
}
