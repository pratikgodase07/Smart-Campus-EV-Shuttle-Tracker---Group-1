package com.campus.shuttle.model;

import java.util.Objects;

/**
 * Domain POJO representing a Campus Route / Stop.
 *
 * Fields:
 * - routeId: Unique route identifier (Primary Key)
 * - stopName: Campus hub / pickup station name
 * - sequenceOrder: Chronological stop order on route
 */
public class Route {

    private Integer routeId;
    private String stopName;
    private int sequenceOrder;

    public Route() {
    }

    public Route(String stopName, int sequenceOrder) {
        this.stopName = stopName;
        this.sequenceOrder = sequenceOrder;
    }

    public Route(Integer routeId, String stopName, int sequenceOrder) {
        this.routeId = routeId;
        this.stopName = stopName;
        this.sequenceOrder = sequenceOrder;
    }

    public Integer getRouteId() {
        return routeId;
    }

    public void setRouteId(Integer routeId) {
        this.routeId = routeId;
    }

    public String getStopName() {
        return stopName;
    }

    public void setStopName(String stopName) {
        this.stopName = stopName;
    }

    public int getSequenceOrder() {
        return sequenceOrder;
    }

    public void setSequenceOrder(int sequenceOrder) {
        this.sequenceOrder = sequenceOrder;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Route route = (Route) o;
        return sequenceOrder == route.sequenceOrder &&
                Objects.equals(routeId, route.routeId) &&
                Objects.equals(stopName, route.stopName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(routeId, stopName, sequenceOrder);
    }

    @Override
    public String toString() {
        return "Route{" +
                "routeId=" + routeId +
                ", stopName='" + stopName + '\'' +
                ", sequenceOrder=" + sequenceOrder +
                '}';
    }
}

