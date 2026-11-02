package com.campus.shuttle.model;

/**
 * Result DTO representing the outcome of logging a completed trip (FR-3).
 * Captures battery deduction, remaining charge, and automatic maintenance flag status.
 */
public class TripResult {

    private int tripLogId;
    private int shuttleId;
    private String driverName;
    private int routeId;
    private String stopName;
    private int passengersBoarded;
    private int batteryBefore;
    private int batteryDeducted;
    private int batteryAfter;
    private String status;
    private boolean autoMaintenanceTriggered;

    public TripResult() {
    }

    public TripResult(int tripLogId, int shuttleId, String driverName, int routeId, String stopName,
                      int passengersBoarded, int batteryBefore, int batteryDeducted, int batteryAfter,
                      String status, boolean autoMaintenanceTriggered) {
        this.tripLogId = tripLogId;
        this.shuttleId = shuttleId;
        this.driverName = driverName;
        this.routeId = routeId;
        this.stopName = stopName;
        this.passengersBoarded = passengersBoarded;
        this.batteryBefore = batteryBefore;
        this.batteryDeducted = batteryDeducted;
        this.batteryAfter = batteryAfter;
        this.status = status;
        this.autoMaintenanceTriggered = autoMaintenanceTriggered;
    }

    public int getTripLogId() {
        return tripLogId;
    }

    public void setTripLogId(int tripLogId) {
        this.tripLogId = tripLogId;
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

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public int getPassengersBoarded() {
        return passengersBoarded;
    }

    public void setPassengersBoarded(int passengersBoarded) {
        this.passengersBoarded = passengersBoarded;
    }

    public int getBatteryBefore() {
        return batteryBefore;
    }

    public void setBatteryBefore(int batteryBefore) {
        this.batteryBefore = batteryBefore;
    }

    public int getBatteryDeducted() {
        return batteryDeducted;
    }

    public void setBatteryDeducted(int batteryDeducted) {
        this.batteryDeducted = batteryDeducted;
    }

    public int getBatteryAfter() {
        return batteryAfter;
    }

    public void setBatteryAfter(int batteryAfter) {
        this.batteryAfter = batteryAfter;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public boolean isAutoMaintenanceTriggered() {
        return autoMaintenanceTriggered;
    }

    public void setAutoMaintenanceTriggered(boolean autoMaintenanceTriggered) {
        this.autoMaintenanceTriggered = autoMaintenanceTriggered;
    }

    @Override
    public String toString() {
        return "TripResult{" +
                "tripLogId=" + tripLogId +
                ", shuttleId=" + shuttleId +
                ", driverName='" + driverName + '\'' +
                ", routeId=" + routeId +
                ", stopName='" + stopName + '\'' +
                ", passengersBoarded=" + passengersBoarded +
                ", batteryBefore=" + batteryBefore + "%" +
                ", batteryDeducted=" + batteryDeducted + "%" +
                ", batteryAfter=" + batteryAfter + "%" +
                ", status='" + status + '\'' +
                ", autoMaintenanceTriggered=" + autoMaintenanceTriggered +
                '}';
    }
}

