package com.campus.shuttle.dao;

import com.campus.shuttle.model.Route;
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
@Import(RouteDaoImpl.class)
@DisplayName("Route DAO Integration Tests")
class RouteDaoTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    private RouteDao routeDao;

    @BeforeEach
    void setUp() {
        routeDao = new RouteDaoImpl(jdbcTemplate);
    }

    @Test
    @DisplayName("insertRoute adds new campus stop with generated ID")
    void testInsertRoute() {
        Route route = new Route("Innovation Lab", 7);
        int rows = routeDao.insertRoute(route);

        assertEquals(1, rows);
        assertNotNull(route.getRouteId());
        assertTrue(route.getRouteId() > 0);

        Optional<Route> fetched = routeDao.findById(route.getRouteId());
        assertTrue(fetched.isPresent());
        assertEquals("Innovation Lab", fetched.get().getStopName());
        assertEquals(7, fetched.get().getSequenceOrder());
    }

    @Test
    @DisplayName("findAllRoutes retrieves all stops sorted by sequence")
    void testFindAllRoutes() {
        List<Route> routes = routeDao.findAllRoutes();
        assertNotNull(routes);
        assertTrue(routes.size() >= 6);
        assertEquals("North Gate Main Terminal", routes.get(0).getStopName());
    }
}

