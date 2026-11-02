package com.campus.shuttle_management.dao;

import com.campus.shuttle_management.model.Shuttle;

import java.util.List;
import java.util.Optional;

/**
 * DAO contract for the {@code shuttles} table.
 *
 * <p>All SQL lives behind this interface — the service layer never
 * touches JdbcTemplate directly, keeping business logic clean and
 * this layer independently testable.
 */
public interface ShuttleDao {

    /**
     * FR-1 — Persist a new shuttle and return its generated primary key.
     */
    int insertShuttle(Shuttle shuttle);

    /**
     * FR-1 — Retrieve the full fleet, ordered by shuttle_id.
     */
    List<Shuttle> findAllShuttles();

    /**
     * FR-2 — Fetch shuttles that are deployable right now:
     *         status = 'AVAILABLE' AND battery_pct > 20.
     */
    List<Shuttle> findDeployableShuttles();

    /**
     * Update battery and status together after a completed trip.
     * Called atomically with the trip log insert inside a transaction.
     */
    int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus);

    /**
     * FR-5 — Update status only (e.g., force MAINTENANCE override).
     */
    int updateStatus(int shuttleId, String status);

    /**
     * Look up a shuttle by its primary key. Returns empty if not found.
     */
    Optional<Shuttle> findById(int shuttleId);
}
