package com.campus.shuttle.dao;

import com.campus.shuttle.model.Shuttle;
import com.campus.shuttle.model.ShuttleAnalyticsDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import(ShuttleDaoImpl.class)
@DisplayName("Shuttle DAO Integration Tests")
class ShuttleDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private ShuttleDao shuttleDao;

    @BeforeEach
    void setUp() {
        shuttleDao = new ShuttleDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("FR-1: insertShuttle successfully persists a new shuttle and populates generated ID")
    void testInsertShuttle() {
        Shuttle newShuttle = new Shuttle("Karan Mehta", 95, "AVAILABLE");
        int rows = shuttleDao.insertShuttle(newShuttle);

        assertEquals(1, rows, "Insert should affect exactly 1 row");
        assertNotNull(newShuttle.getShuttleId(), "Generated key should be populated on Shuttle POJO");
        assertTrue(newShuttle.getShuttleId() > 0, "Generated ID should be positive");

        Optional<Shuttle> fetched = shuttleDao.findById(newShuttle.getShuttleId());
        assertTrue(fetched.isPresent(), "Newly inserted shuttle should be retrievable");
        assertEquals("Karan Mehta", fetched.get().getDriverName());
        assertEquals(95, fetched.get().getBatteryPct());
        assertEquals("AVAILABLE", fetched.get().getStatus());
    }

    @Test
    @DisplayName("FR-1: findAllShuttles retrieves all fleet records")
    void testFindAllShuttles() {
        List<Shuttle> shuttles = shuttleDao.findAllShuttles();
        assertNotNull(shuttles);
        assertTrue(shuttles.size() >= 6, "Expected at least 6 seed shuttles");
    }

    @Test
    @DisplayName("FR-2: findDeployableShuttles returns only AVAILABLE shuttles with battery_pct > 20")
    void testFindDeployableShuttles() {
        List<Shuttle> deployable = shuttleDao.findDeployableShuttles();
        assertNotNull(deployable);
        assertFalse(deployable.isEmpty(), "Deployable shuttles list should not be empty");

        for (Shuttle s : deployable) {
            assertEquals("AVAILABLE", s.getStatus(), "Deployable shuttle status must be AVAILABLE");
            assertTrue(s.getBatteryPct() > 20, "Deployable shuttle battery must be > 20%");
        }

        boolean containsLowBatteryAvailable = deployable.stream()
                .anyMatch(s -> "Amit Patel".equals(s.getDriverName())); // Battery 18%
        assertFalse(containsLowBatteryAvailable, "Shuttle with battery <= 20% must NOT be deployable");

        boolean containsInTransit = deployable.stream()
                .anyMatch(s -> "Sneha Reddy".equals(s.getDriverName())); // IN_TRANSIT
        assertFalse(containsInTransit, "IN_TRANSIT shuttle must NOT be deployable");

        boolean containsMaintenance = deployable.stream()
                .anyMatch(s -> "Rohan Verma".equals(s.getDriverName())); // MAINTENANCE
        assertFalse(containsMaintenance, "MAINTENANCE shuttle must NOT be deployable");
    }

    @Test
    @DisplayName("FR-3: updateShuttleBatteryAndStatus updates both battery percentage and status")
    void testUpdateShuttleBatteryAndStatus() {
        Optional<Shuttle> initial = shuttleDao.findById(1);
        assertTrue(initial.isPresent());

        int rows = shuttleDao.updateShuttleBatteryAndStatus(1, 14, "MAINTENANCE");
        assertEquals(1, rows, "Update should affect 1 row");

        Optional<Shuttle> updated = shuttleDao.findById(1);
        assertTrue(updated.isPresent());
        assertEquals(14, updated.get().getBatteryPct());
        assertEquals("MAINTENANCE", updated.get().getStatus());
    }

    @Test
    @DisplayName("FR-4: getShuttleTripAnalytics executes multi-table JOIN query")
    void testGetShuttleTripAnalytics() {
        List<ShuttleAnalyticsDTO> analytics = shuttleDao.getShuttleTripAnalytics();
        assertNotNull(analytics);
        assertFalse(analytics.isEmpty());

        // Shuttle 1 has 2 trips in seed data (14 + 18 = 32 passengers)
        Optional<ShuttleAnalyticsDTO> s1 = analytics.stream()
                .filter(a -> a.getShuttleId() == 1)
                .findFirst();
        assertTrue(s1.isPresent());
        assertEquals(2, s1.get().getTotalTrips());
        assertEquals(32, s1.get().getTotalPassengers());
    }

    @Test
    @DisplayName("FR-5: updateStatus successfully overrides status (e.g. to MAINTENANCE)")
    void testUpdateStatus() {
        Optional<Shuttle> initial = shuttleDao.findById(2);
        assertTrue(initial.isPresent());
        assertEquals("AVAILABLE", initial.get().getStatus());

        int rows = shuttleDao.updateStatus(2, "MAINTENANCE");
        assertEquals(1, rows, "Update should affect 1 row");

        Optional<Shuttle> updated = shuttleDao.findById(2);
        assertTrue(updated.isPresent());
        assertEquals("MAINTENANCE", updated.get().getStatus());
    }

    @Test
    @DisplayName("findById returns empty Optional when ID does not exist")
    void testFindByIdNotFound() {
        Optional<Shuttle> nonExistent = shuttleDao.findById(99999);
        assertTrue(nonExistent.isEmpty(), "Should return empty Optional for non-existent ID");
    }
}

