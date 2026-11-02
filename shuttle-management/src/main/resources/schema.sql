-- ===================================================
-- Smart Campus EV Shuttle Tracker Database Schema (MySQL)
-- ===================================================

DROP TABLE IF EXISTS trip_logs;
DROP TABLE IF EXISTS routes;
DROP TABLE IF EXISTS shuttles;

-- Table: shuttles
CREATE TABLE shuttles (
    shuttle_id INT AUTO_INCREMENT PRIMARY KEY,
    driver_name VARCHAR(100) NOT NULL,
    battery_pct INT NOT NULL,
    status VARCHAR(30) NOT NULL
);

-- Table: routes
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    stop_name VARCHAR(100) NOT NULL,
    sequence_order INT NOT NULL
);

-- Table: trip_logs
CREATE TABLE trip_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    shuttle_id INT NOT NULL,
    route_id INT NOT NULL,
    passengers_boarded INT NOT NULL,
    trip_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trip_shuttle FOREIGN KEY (shuttle_id) REFERENCES shuttles(shuttle_id) ON DELETE CASCADE,
    CONSTRAINT fk_trip_route FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);

