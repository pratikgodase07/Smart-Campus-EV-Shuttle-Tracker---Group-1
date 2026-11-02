package com.campus.shuttle_management.dao.impl;

import com.campus.shuttle_management.dao.TripLogDao;
import com.campus.shuttle_management.model.TripLog;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.List;

/**
 * JdbcTemplate implementation of {@link TripLogDao}.
 *
 * <p>The inline RowMapper handles the TIMESTAMP column safely by
 * checking for null before converting to LocalDateTime.
 */
@Repository
public class TripLogDaoImpl implements TripLogDao {

    private final JdbcTemplate jdbcTemplate;

    public TripLogDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ── RowMapper ─────────────────────────────────────────────────

    private final RowMapper<TripLog> tripLogRowMapper = (rs, rowNum) -> {
        TripLog log = new TripLog();
        log.setLogId(rs.getInt("log_id"));
        log.setShuttleId(rs.getInt("shuttle_id"));
        log.setRouteId(rs.getInt("route_id"));
        log.setPassengersBoarded(rs.getInt("passengers_boarded"));

        Timestamp ts = rs.getTimestamp("trip_timestamp");
        if (ts != null) {
            log.setTripTimestamp(ts.toLocalDateTime());
        }
        return log;
    };

    // ── FR-3: Insert ──────────────────────────────────────────────

    @Override
    public int insertTripLog(int shuttleId, int routeId, int passengersBoarded) {
        String sql = "INSERT INTO trip_logs (shuttle_id, route_id, passengers_boarded) "
                   + "VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, shuttleId);
            ps.setInt(2, routeId);
            ps.setInt(3, passengersBoarded);
            return ps;
        }, keyHolder);

        return keyHolder.getKey() != null ? keyHolder.getKey().intValue() : -1;
    }

    // ── All Logs ──────────────────────────────────────────────────

    @Override
    public List<TripLog> findAllTripLogs() {
        String sql = "SELECT * FROM trip_logs ORDER BY trip_timestamp DESC";
        return jdbcTemplate.query(sql, tripLogRowMapper);
    }

    // ── By Shuttle ────────────────────────────────────────────────

    @Override
    public List<TripLog> findByShuttleId(int shuttleId) {
        String sql = "SELECT * FROM trip_logs WHERE shuttle_id = ? ORDER BY trip_timestamp DESC";
        return jdbcTemplate.query(sql, tripLogRowMapper, shuttleId);
    }
}
