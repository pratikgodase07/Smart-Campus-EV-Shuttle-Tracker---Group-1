package com.campus.shuttle.rowmapper;

import com.campus.shuttle.model.Route;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Custom RowMapper implementation for Route domain entity.
 */
@Component
public class RouteRowMapper implements RowMapper<Route> {

    @Override
    public Route mapRow(ResultSet rs, int rowNum) throws SQLException {
        Route route = new Route();
        route.setRouteId(rs.getInt("route_id"));
        route.setStopName(rs.getString("stop_name"));
        route.setSequenceOrder(rs.getInt("sequence_order"));
        return route;
    }
}

