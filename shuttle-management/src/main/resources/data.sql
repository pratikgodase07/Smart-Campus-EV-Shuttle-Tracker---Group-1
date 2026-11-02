-- ===================================================================
--  data.sql  |  Smart Campus EV Shuttle Tracker
--  Realistic campus fleet seed data for development and demo.
-- ===================================================================

-- ── Shuttle Fleet ────────────────────────────────────────────────
INSERT INTO shuttles (driver_name, battery_pct, status) VALUES
    ('Rajesh Kumar',  85, 'AVAILABLE'),    -- Deployable, high battery
    ('Priya Sharma',  60, 'AVAILABLE'),    -- Deployable, good battery
    ('Amit Patel',    18, 'AVAILABLE'),    -- NOT deployable (battery <= 20%)
    ('Sneha Reddy',   50, 'IN_TRANSIT'),  -- Currently on a route
    ('Vikram Singh',  10, 'CHARGING'),    -- Plugged in, not available
    ('Rohan Verma',    8, 'MAINTENANCE'), -- Under repair, blocked
    ('Kavya Nair',    92, 'AVAILABLE'),   -- Deployable, full charge
    ('Deepak Joshi',  35, 'AVAILABLE');   -- Deployable, moderate charge

-- ── Campus Route Stops ───────────────────────────────────────────
INSERT INTO routes (stop_name, sequence_order) VALUES
    ('North Gate Main Terminal',    1),
    ('Central Library Hub',         2),
    ('Engineering Complex',         3),
    ('Science & Research Block',    4),
    ('Student Hostel Quad',         5),
    ('Sports Arena & Cafeteria',    6),
    ('Admin Block & Faculty Lounge',7);

-- ── Historical Trip Logs ─────────────────────────────────────────
INSERT INTO trip_logs (shuttle_id, route_id, passengers_boarded, trip_timestamp) VALUES
    (1, 1, 14, CURRENT_TIMESTAMP),
    (1, 2, 18, CURRENT_TIMESTAMP),
    (2, 3, 12, CURRENT_TIMESTAMP),
    (4, 4, 15, CURRENT_TIMESTAMP),
    (7, 5, 20, CURRENT_TIMESTAMP),
    (8, 6,  9, CURRENT_TIMESTAMP);
