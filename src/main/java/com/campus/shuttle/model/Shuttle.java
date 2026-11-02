package com.campus.shuttle.model;

import java.util.Objects;

/**
 * Domain POJO representing an Electric Vehicle (EV) Shuttle.
 *
 * Fields:
 * - shuttleId: Unique identifier for the shuttle (Primary Key)
 * - driverName: Assigned driver name
 * - batteryPct: Current battery charge percentage (0 - 100)
 * - status: Current operational status (AVAILABLE, IN_TRANSIT, CHARGING, MAINTENANCE)
 */
public class Shuttle {

    private Integer shuttleId;
    private String driverName;
    private int batteryPct;
    private String status;

    public Shuttle() {
    }

    public Shuttle(String driverName, int batteryPct, String status) {
        this.driverName = driverName;
        this.batteryPct = batteryPct;
        this.status = status;
    }

    public Shuttle(Integer shuttleId, String driverName, int batteryPct, String status) {
        this.shuttleId = shuttleId;
        this.driverName = driverName;
        this.batteryPct = batteryPct;
        this.status = status;
    }

    public Shuttle(int shuttleId, String driverName, int batteryPct, String status) {
        this.shuttleId = shuttleId;
        this.driverName = driverName;
        this.batteryPct = batteryPct;
        this.status = status;
    }

    public Integer getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(Integer shuttleId) {
        this.shuttleId = shuttleId;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Shuttle shuttle = (Shuttle) o;
        return batteryPct == shuttle.batteryPct &&
                Objects.equals(shuttleId, shuttle.shuttleId) &&
                Objects.equals(driverName, shuttle.driverName) &&
                Objects.equals(status, shuttle.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shuttleId, driverName, batteryPct, status);
    }

    @Override
    public String toString() {
        return "Shuttle{" +
                "shuttleId=" + shuttleId +
                ", driverName='" + driverName + '\'' +
                ", batteryPct=" + batteryPct + "%" +
                ", status='" + status + '\'' +
                '}';
    }
}

