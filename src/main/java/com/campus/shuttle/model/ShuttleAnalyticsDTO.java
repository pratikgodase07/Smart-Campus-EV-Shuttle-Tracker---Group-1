package com.campus.shuttle.model;

import java.util.Objects;

/**
 * Data Transfer Object (DTO) for FR-4 Multi-Table Joined Analytics.
 * Combines Shuttle attributes with aggregate trip metrics:
 * - total completed trips
 * - aggregate passenger count
 */
public class ShuttleAnalyticsDTO {

    private int shuttleId;
    private String driverName;
    private int batteryPct;
    private String status;
    private int totalTrips;
    private int totalPassengers;

    public ShuttleAnalyticsDTO() {
    }

    public ShuttleAnalyticsDTO(int shuttleId, String driverName, int batteryPct, String status, int totalTrips, int totalPassengers) {
        this.shuttleId = shuttleId;
        this.driverName = driverName;
        this.batteryPct = batteryPct;
        this.status = status;
        this.totalTrips = totalTrips;
        this.totalPassengers = totalPassengers;
    }

    public int getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(int shuttleId) {
        this.shuttleId = shuttleId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public int getBatteryPct() {
        return batteryPct;
    }

    public void setBatteryPct(int batteryPct) {
        this.batteryPct = batteryPct;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getTotalTrips() {
        return totalTrips;
    }

    public void setTotalTrips(int totalTrips) {
        this.totalTrips = totalTrips;
    }

    public int getTotalPassengers() {
        return totalPassengers;
    }

    public void setTotalPassengers(int totalPassengers) {
        this.totalPassengers = totalPassengers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ShuttleAnalyticsDTO that = (ShuttleAnalyticsDTO) o;
        return shuttleId == that.shuttleId &&
                batteryPct == that.batteryPct &&
                totalTrips == that.totalTrips &&
                totalPassengers == that.totalPassengers &&
                Objects.equals(driverName, that.driverName) &&
                Objects.equals(status, that.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shuttleId, driverName, batteryPct, status, totalTrips, totalPassengers);
    }

    @Override
    public String toString() {
        return "ShuttleAnalyticsDTO{" +
                "shuttleId=" + shuttleId +
                ", driverName='" + driverName + '\'' +
                ", batteryPct=" + batteryPct + "%" +
                ", status='" + status + '\'' +
                ", totalTrips=" + totalTrips +
                ", totalPassengers=" + totalPassengers +
                '}';
    }
}

