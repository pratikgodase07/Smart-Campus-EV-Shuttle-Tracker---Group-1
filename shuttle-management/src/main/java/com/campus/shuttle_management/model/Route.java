package com.campus.shuttle_management.model;

/**
 * Represents a named campus stop and its sequential position on a route.
 */
public class Route {

    private int    routeId;
    private String stopName;
    private int    sequenceOrder;

    public Route() {}

    public Route(int routeId, String stopName, int sequenceOrder) {
        this.routeId       = routeId;
        this.stopName      = stopName;
        this.sequenceOrder = sequenceOrder;
    }

    public int    getRouteId()                           { return routeId; }
    public void   setRouteId(int routeId)               { this.routeId = routeId; }

    public String getStopName()                          { return stopName; }
    public void   setStopName(String stopName)           { this.stopName = stopName; }

    public int    getSequenceOrder()                     { return sequenceOrder; }
    public void   setSequenceOrder(int sequenceOrder)    { this.sequenceOrder = sequenceOrder; }

    @Override
    public String toString() {
        return String.format("Route{ id=%d, stop='%s', seq=%d }", routeId, stopName, sequenceOrder);
    }
}
