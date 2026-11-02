package com.campus.shuttle.rowmapper;

import com.campus.shuttle.model.Shuttle;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Custom RowMapper implementation for Shuttle domain entity.
 */
@Component
public class ShuttleRowMapper implements RowMapper<Shuttle> {

    @Override
    public Shuttle mapRow(ResultSet rs, int rowNum) throws SQLException {
        Shuttle shuttle = new Shuttle();
        shuttle.setShuttleId(rs.getInt("shuttle_id"));
        shuttle.setDriverName(rs.getString("driver_name"));
        shuttle.setBatteryPct(rs.getInt("battery_pct"));
        shuttle.setStatus(rs.getString("status"));
        return shuttle;
    }
}

