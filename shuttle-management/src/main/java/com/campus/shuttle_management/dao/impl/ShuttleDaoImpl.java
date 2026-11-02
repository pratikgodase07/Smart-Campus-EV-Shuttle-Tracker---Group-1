package com.campus.shuttle_management.dao.impl;

import com.campus.shuttle_management.dao.ShuttleDao;
import com.campus.shuttle_management.model.Shuttle;
import com.campus.shuttle_management.rowmapper.ShuttleRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * JdbcTemplate implementation of {@link ShuttleDao}.
 *
 * <p>Every query uses parameterized placeholders (?) — no string
 * concatenation near SQL anywhere in this class. SQL injection
 * is structurally impossible.
 */
@Repository
public class ShuttleDaoImpl implements ShuttleDao {

    private final JdbcTemplate    jdbcTemplate;
    private final ShuttleRowMapper rowMapper;

    public ShuttleDaoImpl(JdbcTemplate jdbcTemplate, ShuttleRowMapper rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper    = rowMapper;
    }

    // ── FR-1: Insert ──────────────────────────────────────────────

    @Override
    public int insertShuttle(Shuttle shuttle) {
        String sql = "INSERT INTO shuttles (driver_name, battery_pct, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, shuttle.getDriverName());
            ps.setInt(2, shuttle.getBatteryPct());
            ps.setString(3, shuttle.getStatus());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            shuttle.setShuttleId(keyHolder.getKey().intValue());
        }
        return rows;
    }

    // ── FR-1: All Shuttles ────────────────────────────────────────

    @Override
    public List<Shuttle> findAllShuttles() {
        String sql = "SELECT shuttle_id, driver_name, battery_pct, status "
                   + "FROM shuttles ORDER BY shuttle_id ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    // ── FR-2: Available Shuttles ──────────────────────────────────

    @Override
    public List<Shuttle> findDeployableShuttles() {
        String sql = "SELECT shuttle_id, driver_name, battery_pct, status "
                   + "FROM shuttles "
                   + "WHERE status = ? AND battery_pct > ? "
                   + "ORDER BY battery_pct DESC";
        return jdbcTemplate.query(sql, rowMapper, "AVAILABLE", 20);
    }

    // ── Battery + Status Update ───────────────────────────────────

    @Override
    public int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus) {
        String sql = "UPDATE shuttles SET battery_pct = ?, status = ? WHERE shuttle_id = ?";
        return jdbcTemplate.update(sql, newBattery, newStatus, shuttleId);
    }

    // ── FR-5: Status-Only Update ──────────────────────────────────

    @Override
    public int updateStatus(int shuttleId, String status) {
        String sql = "UPDATE shuttles SET status = ? WHERE shuttle_id = ?";
        return jdbcTemplate.update(sql, status, shuttleId);
    }

    // ── Find by ID ────────────────────────────────────────────────

    @Override
    public Optional<Shuttle> findById(int shuttleId) {
        String sql = "SELECT shuttle_id, driver_name, battery_pct, status "
                   + "FROM shuttles WHERE shuttle_id = ?";
        List<Shuttle> results = jdbcTemplate.query(sql, rowMapper, shuttleId);
        return results.stream().findFirst();
    }
}
