package com.campus.shuttle.rowmapper;

import com.campus.shuttle.model.ShuttleAnalyticsDTO;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Custom RowMapper for FR-4 Multi-Table Joined Analytics query.
 * Maps:
 * - s.shuttle_id
 * - s.driver_name
 * - s.battery_pct
 * - s.status
 * - COUNT(t.log_id) AS total_trips
 * - COALESCE(SUM(t.passengers_boarded), 0) AS total_passengers
 */
@Component
public class ShuttleAnalyticsRowMapper implements RowMapper<ShuttleAnalyticsDTO> {

    @Override
    public ShuttleAnalyticsDTO mapRow(ResultSet rs, int rowNum) throws SQLException {
        ShuttleAnalyticsDTO dto = new ShuttleAnalyticsDTO();
        dto.setShuttleId(rs.getInt("shuttle_id"));
        dto.setDriverName(rs.getString("driver_name"));
        dto.setBatteryPct(rs.getInt("battery_pct"));
        dto.setStatus(rs.getString("status"));
        dto.setTotalTrips(rs.getInt("total_trips"));
        dto.setTotalPassengers(rs.getInt("total_passengers"));
        return dto;
    }
}

