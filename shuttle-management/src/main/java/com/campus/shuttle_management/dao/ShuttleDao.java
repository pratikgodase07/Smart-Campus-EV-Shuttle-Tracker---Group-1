package com.campus.shuttle_management.dao;

import com.campus.shuttle_management.model.Shuttle;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for Shuttle domain entity.
 * Provides abstraction over database operations using Spring JdbcTemplate.
 */
public interface ShuttleDao {

    /**
     * FR-1: Shuttle Fleet Onboarding
     * Inserts a new shuttle record into the database with driver name,
     * initial battery percentage, and operational status.
     *
     * @param shuttle the shuttle domain object to be persisted
     * @return the number of rows affected (1 on success)
     */
    int insertShuttle(Shuttle shuttle);

    /**
     * FR-1: Shuttle Fleet Inventory
     * Retrieves all shuttles in the fleet regardless of current status.
     *
     * @return a List of all Shuttle objects in the database
     */
    List<Shuttle> findAllShuttles();

    /**
     * FR-2: Dynamic Availability Querying
     * Fetches all deployable shuttles meeting strict operating criteria:
     * status = ? ('AVAILABLE') AND battery_pct > ? (20).
     * Must use parameterized queries to prevent SQL injection.
     *
     * @return a List of deployable Shuttle objects
     */
    List<Shuttle> findDeployableShuttles();

    /**
     * Updates the battery percentage and status of a specific shuttle.
     * Typically used after completing a route trip to deduct battery
     * and update state (e.g. to 'MAINTENANCE' if battery < 15%).
     *
     * @param shuttleId the unique ID of the shuttle
     * @param newBattery the updated battery percentage (0 - 100)
     * @param newStatus the new status (e.g. AVAILABLE, MAINTENANCE, CHARGING)
     * @return the number of rows affected
     */
    int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus);

    /**
     * FR-5: Maintenance Flag & Override
     * Manually or programmatically updates only the status of a specific shuttle.
     * Allows dispatchers to flag vehicles under MAINTENANCE.
     *
     * @param shuttleId the unique ID of the shuttle
     * @param status the new status string
     * @return the number of rows affected
     */
    int updateStatus(int shuttleId, String status);

    /**
     * Finds a shuttle by its unique identifier.
     *
     * @param shuttleId the unique ID of the shuttle
     * @return Optional containing the Shuttle if found, or Optional.empty()
     */
    Optional<Shuttle> findById(int shuttleId);
}

