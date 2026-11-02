package com.campus.shuttle.dao;

import com.campus.shuttle.model.TripLog;
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
@Import(TripLogDaoImpl.class)
@DisplayName("TripLog DAO Integration Tests")
class TripLogDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private TripLogDao tripLogDao;

    @BeforeEach
    void setUp() {
        tripLogDao = new TripLogDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("insertTripLog persists trip and populates generated ID")
    void testInsertTripLog() {
        TripLog log = new TripLog(1, 2, 20);
        int rows = tripLogDao.insertTripLog(log);

        assertEquals(1, rows);
        assertNotNull(log.getLogId());
        assertTrue(log.getLogId() > 0);

        Optional<TripLog> fetched = tripLogDao.findById(log.getLogId());
        assertTrue(fetched.isPresent());
        assertEquals(1, fetched.get().getShuttleId());
        assertEquals(2, fetched.get().getRouteId());
        assertEquals(20, fetched.get().getPassengersBoarded());
    }

    @Test
    @DisplayName("findByShuttleId retrieves trips for specific vehicle")
    void testFindByShuttleId() {
        List<TripLog> logs = tripLogDao.findByShuttleId(1);
        assertNotNull(logs);
        assertFalse(logs.isEmpty());
        for (TripLog t : logs) {
            assertEquals(1, t.getShuttleId());
        }
    }
}

