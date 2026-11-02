package com.campus.shuttle.model;

import java.sql.Timestamp;
import java.util.Objects;

/**
 * Domain POJO representing a Trip Log record.
 *
 * Fields:
 * - logId: Unique trip log identifier (Primary Key)
 * - shuttleId: Associated vehicle (FK -> shuttles)
 * - routeId: Assigned route / stop (FK -> routes)
 * - passengersBoarded: Number of passengers boarded
 * - tripTimestamp: Timestamp of when the trip was logged
 */
public class TripLog {

    private Integer logId;
    private int shuttleId;
    private int routeId;
    private int passengersBoarded;
    private Timestamp tripTimestamp;

    public TripLog() {
    }

    public TripLog(int shuttleId, int routeId, int passengersBoarded) {
        this.shuttleId = shuttleId;
        this.routeId = routeId;
        this.passengersBoarded = passengersBoarded;
    }

    public TripLog(Integer logId, int shuttleId, int routeId, int passengersBoarded, Timestamp tripTimestamp) {
        this.logId = logId;
        this.shuttleId = shuttleId;
        this.routeId = routeId;
        this.passengersBoarded = passengersBoarded;
        this.tripTimestamp = tripTimestamp;
    }

    public Integer getLogId() {
        return logId;
    }

    public void setLogId(Integer logId) {
        this.logId = logId;
    }

    public int getShuttleId() {
        return shuttleId;
    }

    public void setShuttleId(int shuttleId) {
        this.shuttleId = shuttleId;
    }

    public int getRouteId() {
        return routeId;
    }

    public void setRouteId(int routeId) {
        this.routeId = routeId;
    }

    public int getPassengersBoarded() {
        return passengersBoarded;
    }

    public void setPassengersBoarded(int passengersBoarded) {
        this.passengersBoarded = passengersBoarded;
    }

    public Timestamp getTripTimestamp() {
        return tripTimestamp;
    }

    public void setTripTimestamp(Timestamp tripTimestamp) {
        this.tripTimestamp = tripTimestamp;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TripLog tripLog = (TripLog) o;
        return shuttleId == tripLog.shuttleId &&
                routeId == tripLog.routeId &&
                passengersBoarded == tripLog.passengersBoarded &&
                Objects.equals(logId, tripLog.logId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(logId, shuttleId, routeId, passengersBoarded);
    }

    @Override
    public String toString() {
        return "TripLog{" +
                "logId=" + logId +
                ", shuttleId=" + shuttleId +
                ", routeId=" + routeId +
                ", passengersBoarded=" + passengersBoarded +
                ", tripTimestamp=" + tripTimestamp +
                '}';
    }
}

