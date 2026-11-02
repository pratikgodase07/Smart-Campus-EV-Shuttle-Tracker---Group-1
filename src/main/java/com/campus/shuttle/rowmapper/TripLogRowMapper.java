package com.campus.shuttle.rowmapper;

import com.campus.shuttle.model.TripLog;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Custom RowMapper implementation for TripLog domain entity.
 */
@Component
public class TripLogRowMapper implements RowMapper<TripLog> {

    @Override
    public TripLog mapRow(ResultSet rs, int rowNum) throws SQLException {
        TripLog log = new TripLog();
        log.setLogId(rs.getInt("log_id"));
        log.setShuttleId(rs.getInt("shuttle_id"));
        log.setRouteId(rs.getInt("route_id"));
        log.setPassengersBoarded(rs.getInt("passengers_boarded"));
        log.setTripTimestamp(rs.getTimestamp("trip_timestamp"));
        return log;
    }
}

