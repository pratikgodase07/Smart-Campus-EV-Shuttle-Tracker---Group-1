package com.campus.shuttle.dao;

import com.campus.shuttle.model.TripLog;
import com.campus.shuttle.rowmapper.TripLogRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Spring JDBC Implementation of TripLogDao using JdbcTemplate.
 */
@Repository
public class TripLogDaoImpl implements TripLogDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<TripLog> rowMapper;

    @Autowired
    public TripLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new TripLogRowMapper();
    }

    public TripLogDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<TripLog> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rowMapper != null) ? rowMapper : new TripLogRowMapper();
    }

    @Override
    public int insertTripLog(TripLog tripLog) {
        String sql = "INSERT INTO trip_logs (shuttle_id, route_id, passengers_boarded) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, tripLog.getShuttleId());
            ps.setInt(2, tripLog.getRouteId());
            ps.setInt(3, tripLog.getPassengersBoarded());
            return ps;
        }, keyHolder);

        // Extract generated primary key safely even if multiple generated keys (e.g., TIMESTAMP) are returned
        Map<String, Object> keys = keyHolder.getKeys();
        if (keys != null) {
            Object id = keys.get("LOG_ID");
            if (id == null) {
                id = keys.get("log_id");
            }
            if (id instanceof Number) {
                tripLog.setLogId(((Number) id).intValue());
            } else {
                for (Object val : keys.values()) {
                    if (val instanceof Number) {
                        tripLog.setLogId(((Number) val).intValue());
                        break;
                    }
                }
            }
        }

        return rows;
    }

    @Override
    public List<TripLog> findAllTripLogs() {
        String sql = "SELECT log_id, shuttle_id, route_id, passengers_boarded, trip_timestamp FROM trip_logs ORDER BY trip_timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public List<TripLog> findByShuttleId(int shuttleId) {
        String sql = "SELECT log_id, shuttle_id, route_id, passengers_boarded, trip_timestamp FROM trip_logs WHERE shuttle_id = ? ORDER BY trip_timestamp DESC";
        return jdbcTemplate.query(sql, rowMapper, shuttleId);
    }

    @Override
    public Optional<TripLog> findById(int logId) {
        String sql = "SELECT log_id, shuttle_id, route_id, passengers_boarded, trip_timestamp FROM trip_logs WHERE log_id = ?";
        List<TripLog> results = jdbcTemplate.query(sql, rowMapper, logId);
        return results.stream().findFirst();
    }
}
