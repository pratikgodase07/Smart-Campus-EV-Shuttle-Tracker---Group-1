package com.campus.shuttle.dao;

import com.campus.shuttle.model.Shuttle;
import com.campus.shuttle.model.ShuttleAnalyticsDTO;

import java.util.List;
import java.util.Optional;

/**
 * Data Access Object (DAO) interface for Shuttle domain entity.
 */
public interface ShuttleDao {

    /**
     * FR-1: Shuttle Fleet Onboarding
     * Inserts a new shuttle and populates its generated primary key ID.
     */
    int insertShuttle(Shuttle shuttle);

    /**
     * FR-1: Shuttle Fleet Inventory
     * Lists all shuttles ordered by shuttle_id.
     */
    List<Shuttle> findAllShuttles();

    /**
     * FR-2: Dynamic Availability Querying
     * Parameterized query strictly filtering for:
     * status = ? ('AVAILABLE') AND battery_pct > ? (20)
     */
    List<Shuttle> findDeployableShuttles();

    /**
     * Updates shuttle battery percentage and status (e.g., after trip completion).
     */
    int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus);

    /**
     * FR-5: Maintenance Flag & Override
     * Manually updates the shuttle status.
     */
    int updateStatus(int shuttleId, String status);

    /**
     * Finds a shuttle by its ID using parameterized query.
     */
    Optional<Shuttle> findById(int shuttleId);

    /**
     * FR-4: Multi-Table Joined Analytics
     * Executes a multi-table JOIN query displaying each shuttle alongside
     * its total completed trips and aggregate passenger count.
     */
    List<ShuttleAnalyticsDTO> getShuttleTripAnalytics();
}

