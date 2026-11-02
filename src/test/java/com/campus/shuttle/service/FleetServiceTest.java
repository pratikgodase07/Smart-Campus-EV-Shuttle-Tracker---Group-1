package com.campus.shuttle.service;

import com.campus.shuttle.dao.*;
import com.campus.shuttle.model.Shuttle;
import com.campus.shuttle.model.TripResult;
import com.campus.shuttle.service.exception.ShuttleNotDeployableException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.core.JdbcTemplate;

import static org.junit.jupiter.api.Assertions.*;

@JdbcTest
@Import({ShuttleDaoImpl.class, RouteDaoImpl.class, TripLogDaoImpl.class, FleetServiceImpl.class})
@DisplayName("Fleet Service Business Logic Automation Tests")
class FleetServiceTest {

    @Autowired
    private FleetService fleetService;

    @Autowired
    private ShuttleDao shuttleDao;

    @Test
    @DisplayName("FR-1: registerShuttle validates and persists new vehicle")
    void testRegisterShuttle() {
        Shuttle shuttle = fleetService.registerShuttle("Ananya Roy", 90);
        assertNotNull(shuttle.getShuttleId());
        assertEquals("Ananya Roy", shuttle.getDriverName());
        assertEquals(90, shuttle.getBatteryPct());
        assertEquals("AVAILABLE", shuttle.getStatus());
    }

    @Test
    @DisplayName("FR-2 & FR-3: logTrip rejects non-deployable shuttle (battery <= 20%)")
    void testLogTripRejectsLowBattery() {
        // Shuttle 3 has 18% battery in seed data
        assertThrows(ShuttleNotDeployableException.class, () ->
                fleetService.logTrip(3, 1, 10));
    }

    @Test
    @DisplayName("FR-2 & FR-3: logTrip rejects non-deployable shuttle (status != AVAILABLE)")
    void testLogTripRejectsNonAvailableStatus() {
        // Shuttle 4 is IN_TRANSIT, Shuttle 6 is MAINTENANCE
        assertThrows(ShuttleNotDeployableException.class, () ->
                fleetService.logTrip(4, 1, 10));
        assertThrows(ShuttleNotDeployableException.class, () ->
                fleetService.logTrip(6, 1, 10));
    }

    @Test
    @DisplayName("FR-3: logTrip deducts battery and maintains AVAILABLE when battery >= 15%")
    void testLogTripNormalDeduction() {
        // Shuttle 1 has 85% battery
        TripResult result = fleetService.logTrip(1, 1, 15);
        assertEquals(85, result.getBatteryBefore());
        assertEquals(10, result.getBatteryDeducted());
        assertEquals(75, result.getBatteryAfter());
        assertEquals("AVAILABLE", result.getStatus());
        assertFalse(result.isAutoMaintenanceTriggered());
    }

    @Test
    @DisplayName("FR-3: Automated Business Rule: Battery dropping below 15% auto-triggers MAINTENANCE")
    void testLogTripAutoMaintenanceTrigger() {
        // Create a shuttle with exactly 22% battery (deployable because > 20%)
        Shuttle shuttle = new Shuttle("Test Low Batt", 22, "AVAILABLE");
        shuttleDao.insertShuttle(shuttle);

        // Trip deducts 10% -> 22% - 10% = 12% (< 15% threshold!)
        TripResult result = fleetService.logTrip(shuttle.getShuttleId(), 1, 8);

        assertEquals(22, result.getBatteryBefore());
        assertEquals(10, result.getBatteryDeducted());
        assertEquals(12, result.getBatteryAfter());
        assertEquals("MAINTENANCE", result.getStatus(), "Status must automatically transition to MAINTENANCE");
        assertTrue(result.isAutoMaintenanceTriggered(), "Auto maintenance flag must be true");

        // Verify that subsequent dispatch attempts are now blocked!
        assertThrows(ShuttleNotDeployableException.class, () ->
                fleetService.logTrip(shuttle.getShuttleId(), 1, 5));
    }

    @Test
    @DisplayName("FR-5: placeShuttleUnderMaintenance overrides vehicle status")
    void testPlaceShuttleUnderMaintenance() {
        Shuttle updated = fleetService.placeShuttleUnderMaintenance(2);
        assertEquals("MAINTENANCE", updated.getStatus());

        // Subsequent trip dispatches must be blocked
        assertThrows(ShuttleNotDeployableException.class, () ->
                fleetService.logTrip(2, 1, 5));
    }
}

