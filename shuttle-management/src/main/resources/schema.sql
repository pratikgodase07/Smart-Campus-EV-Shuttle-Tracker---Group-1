-- ===================================================================
--  schema.sql  |  Smart Campus EV Shuttle Tracker
--  Defines the three core tables with relational integrity.
-- ===================================================================

-- Drop tables in reverse dependency order so re-runs are safe
DROP TABLE IF EXISTS trip_logs;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS shuttles;

-- ── shuttles ─────────────────────────────────────────────────────
--  Tracks every EV vehicle in the campus fleet.
CREATE TABLE shuttles (
    shuttle_id   INT          AUTO_INCREMENT PRIMARY KEY,
    driver_name  VARCHAR(100) NOT NULL,
    battery_pct  INT          NOT NULL,
    status       VARCHAR(30)  NOT NULL
        -- Valid values: AVAILABLE | IN_TRANSIT | CHARGING | MAINTENANCE
);

-- ── routes ───────────────────────────────────────────────────────
--  Named campus stops in their route sequence.
CREATE TABLE routes (
    route_id        INT          AUTO_INCREMENT PRIMARY KEY,
    stop_name       VARCHAR(100) NOT NULL,
    sequence_order  INT          NOT NULL
);

-- ── trip_logs ────────────────────────────────────────────────────
--  Audit log linking shuttles to routes for every completed trip.
CREATE TABLE trip_logs (
    log_id             INT       AUTO_INCREMENT PRIMARY KEY,
    shuttle_id         INT       NOT NULL,
    route_id           INT       NOT NULL,
    passengers_boarded INT       NOT NULL,
    trip_timestamp     TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

    CONSTRAINT fk_trip_shuttle FOREIGN KEY (shuttle_id) REFERENCES shuttles (shuttle_id) ON DELETE CASCADE,
    CONSTRAINT fk_trip_route   FOREIGN KEY (route_id)   REFERENCES routes   (route_id)   ON DELETE CASCADE
);
