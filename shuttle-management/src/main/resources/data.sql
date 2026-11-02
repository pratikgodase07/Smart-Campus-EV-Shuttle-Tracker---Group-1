-- ===================================================
-- Smart Campus EV Shuttle Tracker Seed Data
-- ===================================================

-- Shuttles
INSERT INTO shuttles (driver_name, battery_pct, status) VALUES
('Rajesh Kumar', 85, 'AVAILABLE'),       -- Deployable (battery > 20%)
('Priya Sharma', 60, 'AVAILABLE'),       -- Deployable (battery > 20%)
('Amit Patel', 18, 'AVAILABLE'),         -- NOT deployable (battery <= 20%)
('Sneha Reddy', 50, 'IN_TRANSIT'),       -- NOT deployable (status != AVAILABLE)
('Vikram Singh', 10, 'CHARGING'),        -- NOT deployable (status != AVAILABLE)
('Rohan Verma', 8, 'MAINTENANCE');       -- Under Maintenance

-- Routes
INSERT INTO routes (stop_name, sequence_order) VALUES
('North Gate Main Terminal', 1),
('Central Library Hub', 2),
('Engineering Complex', 3),
('Science & Research Block', 4),
('Student Hostel Quad', 5),
('Sports Arena & Cafeteria', 6);

-- Trip Logs
INSERT INTO trip_logs (shuttle_id, route_id, passengers_boarded, trip_timestamp) VALUES
(1, 1, 14, CURRENT_TIMESTAMP),
(1, 2, 18, CURRENT_TIMESTAMP),
(2, 3, 12, CURRENT_TIMESTAMP),
(4, 4, 15, CURRENT_TIMESTAMP);

