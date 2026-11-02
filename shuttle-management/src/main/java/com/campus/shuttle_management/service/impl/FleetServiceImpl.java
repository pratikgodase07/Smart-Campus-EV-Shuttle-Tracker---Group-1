package com.campus.shuttle_management.service.impl;

import com.campus.shuttle_management.dao.AnalyticsDao;
import com.campus.shuttle_management.dao.ShuttleDao;
import com.campus.shuttle_management.dao.TripLogDao;
import com.campus.shuttle_management.model.Shuttle;
import com.campus.shuttle_management.model.ShuttleAnalytics;
import com.campus.shuttle_management.service.FleetService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Core implementation of the fleet business logic.
 *
 * <p>This is Member 5's primary deliverable. Every method enforces
 * a specific functional requirement from the hackathon spec. There
 * is no SQL here — only decisions, validations, and DAO calls.
 *
 * <p>Dependencies arrive via constructor injection so Spring manages
 * the wiring and unit tests can substitute mock implementations freely.
 *
 * @author Member 5 — Varun
 */
@Service
public class FleetServiceImpl implements FleetService {

    // ── Business Rule Constants ───────────────────────────────────
    //    Named constants instead of magic numbers — if a rule changes,
    //    there is exactly one line to update.

    /** Battery % deducted for each dispatched trip. */
    private static final int    DEPLETION_PER_TRIP   = 10;

    /** Below this post-trip battery level → auto MAINTENANCE. */
    private static final int    MAINTENANCE_THRESHOLD = 15;

    /** Shuttle must exceed this battery % to be dispatched at all. */
    private static final int    DISPATCH_MIN_BATTERY  = 20;

    /** Status literals — defined once to prevent silent typos. */
    private static final String AVAILABLE   = "AVAILABLE";
    private static final String MAINTENANCE = "MAINTENANCE";

    // ── Injected Dependencies ─────────────────────────────────────

    private final ShuttleDao   shuttleDao;
    private final TripLogDao   tripLogDao;
    private final AnalyticsDao analyticsDao;

    public FleetServiceImpl(ShuttleDao shuttleDao,
                            TripLogDao tripLogDao,
                            AnalyticsDao analyticsDao) {
        this.shuttleDao   = shuttleDao;
        this.tripLogDao   = tripLogDao;
        this.analyticsDao = analyticsDao;
    }

    // ═════════════════════════════════════════════════════════════
    //  FR-1  |  Shuttle Registration
    // ═════════════════════════════════════════════════════════════

    @Override
    public int registerShuttle(String driverName, int batteryPct) {
        if (driverName == null || driverName.isBlank()) {
            throw new IllegalArgumentException("Driver name cannot be empty.");
        }
        if (batteryPct < 0 || batteryPct > 100) {
            throw new IllegalArgumentException(
                "Battery must be between 0 and 100. Received: " + batteryPct
            );
        }

        // All freshly registered shuttles start ready for deployment
        Shuttle newShuttle = new Shuttle();
        newShuttle.setDriverName(driverName.trim());
        newShuttle.setBatteryPct(batteryPct);
        newShuttle.setStatus(AVAILABLE);

        shuttleDao.insertShuttle(newShuttle);

        System.out.printf(
            "%n  ✔  Shuttle registered — ID #%d | Driver: %s | Battery: %d%%%n",
            newShuttle.getShuttleId(), newShuttle.getDriverName(), newShuttle.getBatteryPct()
        );

        return newShuttle.getShuttleId();
    }

    // ═════════════════════════════════════════════════════════════
    //  FR-1  |  Fleet Dashboard
    // ═════════════════════════════════════════════════════════════

    @Override
    public List<Shuttle> getFleetDashboard() {
        return shuttleDao.findAllShuttles();
    }

    // ═════════════════════════════════════════════════════════════
    //  FR-2  |  Available Shuttles
    // ═════════════════════════════════════════════════════════════

    @Override
    public List<Shuttle> getAvailableShuttles() {
        return shuttleDao.findDeployableShuttles();
    }

    // ═════════════════════════════════════════════════════════════
    //  FR-3  |  Trip Dispatch with Battery Depletion
    // ═════════════════════════════════════════════════════════════

    /**
     * Dispatches a shuttle, logs the trip, and updates battery — all in
     * one transaction. If anything fails mid-way, Spring rolls back both
     * the INSERT and the UPDATE so the database stays consistent.
     */
    @Override
    @Transactional
    public void dispatchTrip(int shuttleId, int routeId, int passengersBoarded) {

        // 1. Verify the shuttle actually exists
        Shuttle shuttle = shuttleDao.findById(shuttleId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Shuttle not found with ID: " + shuttleId
            ));

        // 2. Check it is eligible for dispatch
        checkDispatchEligibility(shuttle);

        // 3. Calculate new battery level — never dip below 0
        int oldBattery = shuttle.getBatteryPct();
        int newBattery = Math.max(oldBattery - DEPLETION_PER_TRIP, 0);

        // 4. Apply the auto-maintenance business rule
        String newStatus = (newBattery < MAINTENANCE_THRESHOLD) ? MAINTENANCE : AVAILABLE;

        // 5. Write the trip log first — if this fails, nothing else runs
        int logId = tripLogDao.insertTripLog(shuttleId, routeId, passengersBoarded);

        // 6. Persist updated shuttle state (battery + status)
        shuttleDao.updateShuttleBatteryAndStatus(shuttleId, newBattery, newStatus);

        // 7. Print a clear summary for the console operator
        printDispatchResult(shuttle, logId, routeId, passengersBoarded,
                            oldBattery, newBattery, newStatus);
    }

    /**
     * Validates that a shuttle is in the right state to be dispatched.
     * Throws with a clear, actionable message on any violation.
     */
    private void checkDispatchEligibility(Shuttle shuttle) {
        if (!AVAILABLE.equals(shuttle.getStatus())) {
            throw new IllegalStateException(String.format(
                "Shuttle #%d cannot be dispatched — it is currently '%s'. "
                + "Only AVAILABLE shuttles can be dispatched.",
                shuttle.getShuttleId(), shuttle.getStatus()
            ));
        }

        if (shuttle.getBatteryPct() <= DISPATCH_MIN_BATTERY) {
            throw new IllegalStateException(String.format(
                "Shuttle #%d has insufficient battery (%d%%). "
                + "Minimum required is %d%%.",
                shuttle.getShuttleId(), shuttle.getBatteryPct(), DISPATCH_MIN_BATTERY
            ));
        }
    }

    /** Formats and prints the dispatch result to the console. */
    private void printDispatchResult(Shuttle shuttle, int logId,
                                     int routeId, int passengers,
                                     int oldBattery, int newBattery,
                                     String newStatus) {
        System.out.println();
        System.out.println("  ══════════════════════════════════════════");
        System.out.println("   ✔  TRIP DISPATCHED");
        System.out.println("  ══════════════════════════════════════════");
        System.out.printf("   Shuttle    : #%d — %s%n", shuttle.getShuttleId(), shuttle.getDriverName());
        System.out.printf("   Route      : #%d%n", routeId);
        System.out.printf("   Passengers : %d%n", passengers);
        System.out.printf("   Battery    : %d%% → %d%%%n", oldBattery, newBattery);
        System.out.printf("   Status     : %s → %s%n", shuttle.getStatus(), newStatus);
        System.out.printf("   Log ID     : #%d%n", logId);

        if (MAINTENANCE.equals(newStatus)) {
            System.out.println();
            System.out.println("  ⚠  Battery dropped below 15% — auto-flagged for MAINTENANCE.");
        }

        System.out.println("  ══════════════════════════════════════════");
        System.out.println();
    }

    // ═════════════════════════════════════════════════════════════
    //  FR-5  |  Maintenance Override
    // ═════════════════════════════════════════════════════════════

    @Override
    public void flagMaintenanceOverride(int shuttleId) {
        // Confirm the shuttle exists before touching the database
        Shuttle shuttle = shuttleDao.findById(shuttleId)
            .orElseThrow(() -> new IllegalArgumentException(
                "Cannot flag maintenance — shuttle not found with ID: " + shuttleId
            ));

        // Idempotent: if already in maintenance, nothing changes
        if (MAINTENANCE.equals(shuttle.getStatus())) {
            System.out.printf(
                "%n  ℹ  Shuttle #%d is already in MAINTENANCE. No change needed.%n",
                shuttleId
            );
            return;
        }

        shuttleDao.updateStatus(shuttleId, MAINTENANCE);

        System.out.printf(
            "%n  ✔  Shuttle #%d (%s) forced into MAINTENANCE.%n"
            + "     Previous status was '%s'. Dispatch is now blocked.%n",
            shuttleId, shuttle.getDriverName(), shuttle.getStatus()
        );
    }

    // ═════════════════════════════════════════════════════════════
    //  FR-4  |  Fleet Analytics
    // ═════════════════════════════════════════════════════════════

    @Override
    public List<ShuttleAnalytics> getFleetAnalytics() {
        // The service just delegates — the JOIN lives in the DAO
        return analyticsDao.getFleetAnalytics();
    }
}
