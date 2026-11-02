package com.campus.shuttle.dao;

import com.campus.shuttle.model.TripLog;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for TripLog entity.
 */
public interface TripLogDao {

    int insertTripLog(TripLog tripLog);

    List<TripLog> findAllTripLogs();

    List<TripLog> findByShuttleId(int shuttleId);

    Optional<TripLog> findById(int logId);
}

