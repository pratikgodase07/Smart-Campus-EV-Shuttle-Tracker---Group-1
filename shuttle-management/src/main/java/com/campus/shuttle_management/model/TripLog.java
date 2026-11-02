package com.campus.shuttle_management.model;

import java.time.LocalDateTime;

/**
 * Represents a single completed trip entry stored in the trip_logs table.
 * Created every time a shuttle is dispatched on a route.
 */
public class TripLog {

    private int           logId;
    private int           shuttleId;
    private int           routeId;
    private int           passengersBoarded;
    private LocalDateTime tripTimestamp;

    public TripLog() {}

    public TripLog(int logId, int shuttleId, int routeId,
                   int passengersBoarded, LocalDateTime tripTimestamp) {
        this.logId             = logId;
        this.shuttleId         = shuttleId;
        this.routeId           = routeId;
        this.passengersBoarded = passengersBoarded;
        this.tripTimestamp     = tripTimestamp;
    }

    public int           getLogId()                                        { return logId; }
    public void          setLogId(int logId)                               { this.logId = logId; }

    public int           getShuttleId()                                    { return shuttleId; }
    public void          setShuttleId(int shuttleId)                       { this.shuttleId = shuttleId; }

    public int           getRouteId()                                      { return routeId; }
    public void          setRouteId(int routeId)                           { this.routeId = routeId; }

    public int           getPassengersBoarded()                            { return passengersBoarded; }
    public void          setPassengersBoarded(int passengersBoarded)       { this.passengersBoarded = passengersBoarded; }

    public LocalDateTime getTripTimestamp()                                { return tripTimestamp; }
    public void          setTripTimestamp(LocalDateTime tripTimestamp)     { this.tripTimestamp = tripTimestamp; }

    @Override
    public String toString() {
        return String.format(
            "TripLog{ log=%d, shuttle=%d, route=%d, passengers=%d, time=%s }",
            logId, shuttleId, routeId, passengersBoarded, tripTimestamp
        );
    }
}
