package com.campus.shuttle.dao;

import com.campus.shuttle.model.Route;
import com.campus.shuttle.rowmapper.RouteRowMapper;
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
 * Spring JDBC Implementation of RouteDao using JdbcTemplate.
 */
@Repository
public class RouteDaoImpl implements RouteDao {

    private final JdbcTemplate jdbcTemplate;
    private final RowMapper<Route> rowMapper;

    @Autowired
    public RouteDaoImpl(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = new RouteRowMapper();
    }

    public RouteDaoImpl(JdbcTemplate jdbcTemplate, RowMapper<Route> rowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.rowMapper = (rowMapper != null) ? rowMapper : new RouteRowMapper();
    }

    @Override
    public int insertRoute(Route route) {
        String sql = "INSERT INTO routes (stop_name, sequence_order) VALUES (?, ?)";
        KeyHolder keyHolder = new GeneratedKeyHolder();

        int rows = jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, route.getStopName());
            ps.setInt(2, route.getSequenceOrder());
            return ps;
        }, keyHolder);

        if (keyHolder.getKey() != null) {
            route.setRouteId(keyHolder.getKey().intValue());
        }

        return rows;
    }

    @Override
    public List<Route> findAllRoutes() {
        String sql = "SELECT route_id, stop_name, sequence_order FROM routes ORDER BY sequence_order ASC";
        return jdbcTemplate.query(sql, rowMapper);
    }

    @Override
    public Optional<Route> findById(int routeId) {
        String sql = "SELECT route_id, stop_name, sequence_order FROM routes WHERE route_id = ?";
        List<Route> results = jdbcTemplate.query(sql, rowMapper, routeId);
        return results.stream().findFirst();
    }
}

