package com.campus.shuttle_management.model;

/**
 * A read-only analytics projection used by the fleet report (FR-4).
 *
 * <p>Assembled from a multi-table JOIN across shuttles and trip_logs.
 * Not a database table — built by {@code AnalyticsDao} and surfaced
 * through the service layer to the console UI.
 */
public class ShuttleAnalytics {

    private int    shuttleId;
    private String driverName;
    private String status;
    private int    batteryPct;
    private int    totalTrips;
    private int    totalPassengers;

    public ShuttleAnalytics() {}

    public ShuttleAnalytics(int shuttleId, String driverName, String status,
                            int batteryPct, int totalTrips, int totalPassengers) {
        this.shuttleId       = shuttleId;
        this.driverName      = driverName;
        this.status          = status;
        this.batteryPct      = batteryPct;
        this.totalTrips      = totalTrips;
        this.totalPassengers = totalPassengers;
    }

    public int    getShuttleId()                           { return shuttleId; }
    public void   setShuttleId(int shuttleId)              { this.shuttleId = shuttleId; }

    public String getDriverName()                          { return driverName; }
    public void   setDriverName(String driverName)         { this.driverName = driverName; }

    public String getStatus()                              { return status; }
    public void   setStatus(String status)                 { this.status = status; }

    public int    getBatteryPct()                          { return batteryPct; }
    public void   setBatteryPct(int batteryPct)            { this.batteryPct = batteryPct; }

    public int    getTotalTrips()                          { return totalTrips; }
    public void   setTotalTrips(int totalTrips)            { this.totalTrips = totalTrips; }

    public int    getTotalPassengers()                     { return totalPassengers; }
    public void   setTotalPassengers(int totalPassengers)  { this.totalPassengers = totalPassengers; }

    @Override
    public String toString() {
        return String.format(
            "ShuttleAnalytics{ id=%d, driver='%s', status='%s', battery=%d%%, trips=%d, passengers=%d }",
            shuttleId, driverName, status, batteryPct, totalTrips, totalPassengers
        );
    }
}
