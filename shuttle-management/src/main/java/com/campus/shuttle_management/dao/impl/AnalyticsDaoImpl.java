package com.campus.shuttle_management.dao.impl;

import com.campus.shuttle_management.dao.AnalyticsDao;
import com.campus.shuttle_management.model.ShuttleAnalytics;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * JdbcTemplate implementation of {@link AnalyticsDao}.
 *
 * <p>The LEFT JOIN ensures shuttles with zero trips still appear in
 * the report. COALESCE converts the NULL aggregate values (from the
 * LEFT side with no matching rows) into 0.
 */
@Repository
public class AnalyticsDaoImpl implements AnalyticsDao {

    private final JdbcTemplate jdbcTemplate;

    public AnalyticsDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // ── RowMapper for the JOIN projection ─────────────────────────

    private final RowMapper<ShuttleAnalytics> analyticsRowMapper = (rs, rowNum) -> {
        ShuttleAnalytics a = new ShuttleAnalytics();
        a.setShuttleId(rs.getInt("shuttle_id"));
        a.setDriverName(rs.getString("driver_name"));
        a.setStatus(rs.getString("status"));
        a.setBatteryPct(rs.getInt("battery_pct"));
        a.setTotalTrips(rs.getInt("total_trips"));
        a.setTotalPassengers(rs.getInt("total_passengers"));
        return a;
    };

    // ── FR-4: Fleet Analytics Query ───────────────────────────────

    @Override
    public List<ShuttleAnalytics> getFleetAnalytics() {
        String sql =
            "SELECT " +
            "    s.shuttle_id, " +
            "    s.driver_name, " +
            "    s.status, " +
            "    s.battery_pct, " +
            "    COALESCE(COUNT(t.log_id),            0) AS total_trips, " +
            "    COALESCE(SUM(t.passengers_boarded),  0) AS total_passengers " +
            "FROM shuttles s " +
            "LEFT JOIN trip_logs t ON s.shuttle_id = t.shuttle_id " +
            "GROUP BY s.shuttle_id, s.driver_name, s.status, s.battery_pct " +
            "ORDER BY total_trips DESC";

        return jdbcTemplate.query(sql, analyticsRowMapper);
    }
}
