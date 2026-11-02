package com.campus.shuttle_management.rowmapper;

import com.campus.shuttle_management.model.Shuttle;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Maps a SQL ResultSet row from the {@code shuttles} table into a {@link Shuttle} object.
 *
 * <p>Keeping mapping logic here — separate from the DAO — means you change
 * column names in exactly one place if the schema ever evolves.
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
