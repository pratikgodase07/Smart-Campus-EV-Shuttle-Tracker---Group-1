package com.campus.shuttle.dao;

import com.campus.shuttle.model.Route;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for Route entity.
 */
public interface RouteDao {

    int insertRoute(Route route);

    List<Route> findAllRoutes();

    Optional<Route> findById(int routeId);
}

