package com.campus.shuttle_management.dao;

import com.campus.shuttle_management.model.ShuttleAnalytics;

import java.util.List;

/**
 * DAO contract for fleet-wide analytics (FR-4).
 *
 * <p>Runs a single multi-table JOIN query returning each shuttle
 * alongside its aggregated trip count and passenger total.
 * Uses a LEFT JOIN so shuttles with zero trips are included.
 */
public interface AnalyticsDao {

    /**
     * FR-4 — Returns the complete fleet report: each shuttle with
     * total trips completed and total passengers carried.
     */
    List<ShuttleAnalytics> getFleetAnalytics();
}
