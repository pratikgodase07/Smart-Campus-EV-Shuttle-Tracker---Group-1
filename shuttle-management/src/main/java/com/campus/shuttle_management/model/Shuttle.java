package com.campus.shuttle_management.model;

import java.util.Objects;

/**
 * Represents a campus EV shuttle vehicle and its current operating state.
 *
 * <p>The {@code status} field follows a defined lifecycle:
 * <pre>
 *   AVAILABLE  ──▶  IN_TRANSIT  ──▶  AVAILABLE
 *                         │
 *                         └──▶  MAINTENANCE  (auto or manual)
 * </pre>
 *
 * <p>A shuttle can also be placed in CHARGING when it needs a battery top-up
 * and manually returned to AVAILABLE by a dispatcher.
 */
public class Shuttle {

    private int    shuttleId;
    private String driverName;
    private int    batteryPct;
    private String status;

    // ── Constructors ──────────────────────────────────────────────

    public Shuttle() {}

    public Shuttle(int shuttleId, String driverName, int batteryPct, String status) {
        this.shuttleId  = shuttleId;
        this.driverName = driverName;
        this.batteryPct = batteryPct;
        this.status     = status;
    }

    // ── Getters & Setters ─────────────────────────────────────────

    public int    getShuttleId()                    { return shuttleId; }
    public void   setShuttleId(int shuttleId)       { this.shuttleId = shuttleId; }

    public String getDriverName()                   { return driverName; }
    public void   setDriverName(String driverName)  { this.driverName = driverName; }

    public int    getBatteryPct()                   { return batteryPct; }
    public void   setBatteryPct(int batteryPct)     { this.batteryPct = batteryPct; }

    public String getStatus()                       { return status; }
    public void   setStatus(String status)          { this.status = status; }

    // ── equals / hashCode / toString ─────────────────────────────

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Shuttle shuttle)) return false;
        return shuttleId == shuttle.shuttleId
            && batteryPct == shuttle.batteryPct
            && Objects.equals(driverName, shuttle.driverName)
            && Objects.equals(status, shuttle.status);
    }

    @Override
    public int hashCode() {
        return Objects.hash(shuttleId, driverName, batteryPct, status);
    }

    @Override
    public String toString() {
        return String.format(
            "Shuttle{ id=%d, driver='%s', battery=%d%%, status='%s' }",
            shuttleId, driverName, batteryPct, status
        );
    }
}
