package com.campus.shuttle_management.dao;

import com.campus.shuttle_management.model.TripLog;

import java.util.List;

/**
 * DAO contract for the {@code trip_logs} table.
 *
 * <p>The service layer calls {@link #insertTripLog} inside a
 * {@code @Transactional} method alongside a shuttle battery update —
 * so both writes are atomic.
 */
public interface TripLogDao {

    /**
     * FR-3 — Persist a new trip log entry and return its generated log_id.
     */
    int insertTripLog(int shuttleId, int routeId, int passengersBoarded);

    /**
     * Retrieve all trip log entries ordered by most recent first.
     */
    List<TripLog> findAllTripLogs();

    /**
     * Retrieve all trips for a specific shuttle (for diagnostics).
     */
    List<TripLog> findByShuttleId(int shuttleId);
}
