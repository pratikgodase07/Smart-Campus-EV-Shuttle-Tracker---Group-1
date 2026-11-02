package com.campus.shuttle_management.dao;

import com.campus.shuttle_management.model.Shuttle;
import com.campus.shuttle_management.rowmapper.ShuttleRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

/**
 * Spring JDBC Implementation of ShuttleDao.
 * Uses JdbcTemplate with parameterized queries (? placeholders)
 * to prevent SQL injection and custom ShuttleRowMapper to map ResultSets.
 */
@Repository
public class ShuttleDaoImpl implements ShuttleDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Shuttle> rowMapper;

    @Autowired
    public ShuttleDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new ShuttleRowMapper();
    }

    public ShuttleDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Shuttle> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rowMapper != null) ? rowMapper : new ShuttleRowMapper();
    }

    /**
     * FR-1: Shuttle Fleet Onboarding
     * Inserts a new shuttle and populates its generated primary key ID.
     */
    @Override
    public int insertShuttle(Shuttle shuttle) {
        String sql = "INSERT INTO shuttles (driver_name, battery_pct, status) VALUES (?, ?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rowsAffected = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, shuttle.getDriverName());
            ps.setInt(2, shuttle.getBatteryPct());
            ps.setString(3, shuttle.getStatus());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            shuttle.setShuttleId(keyHolder.getKey().intValue());
        }

        return rowsAffected;
    }

    /**
     * FR-1: Shuttle Fleet Inventory
     * Lists all shuttles ordered by shuttle_id.
     */
    @Override
    public List<Shuttle> findAllShuttles() {
        String sql = "SELECT shuttle_id, driver_name, battery_pct, status FROM shuttles ORDER BY shuttle_id ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    /**
     * FR-2: Dynamic Availability Querying
     * Parameterized query strictly filtering for:
     * status = ? ('AVAILABLE') AND battery_pct > ? (20)
     */
    @Override
    public List<Shuttle> findDeployableShuttles() {
        String sql = "SELECT shuttle_id, driver_name, battery_pct, status FROM shuttles " +
                     "WHERE status = ? AND battery_pct > ? " +
                     "ORDER BY battery_pct DESC";
        return jdbcTemplate.query(sql, rowMapper, "AVAILABLE", 20);
    }

    /**
     * Updates shuttle battery percentage and status (e.g., after trip completion).
     */
    @Override
    public int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus) {
        String sql = "UPDATE shuttles SET battery_pct = ?, status = ? WHERE shuttle_id = ?";
        return jdbcTemplate.update(sql, newBattery, newStatus, shuttleId);
    }

    /**
     * FR-5: Maintenance Flag & Override
     * Manually updates the shuttle status.
     */
    @Override
    public int updateStatus(int shuttleId, String status) {
        String sql = "UPDATE shuttles SET status = ? WHERE shuttle_id = ?";
        return jdbcTemplate.update(sql, status, shuttleId);
    }

    /**
     * Finds a shuttle by its ID using parameterized query.
     */
    @Override
    public Optional<Shuttle> findById(int shuttleId) {
        String sql = "SELECT shuttle_id, driver_name, battery_pct, status FROM shuttles WHERE shuttle_id = ?";
        List<Shuttle> results = jdbcTemplate.query(sql, rowMapper, shuttleId);
        return results.stream().findFirst();
    }
}

