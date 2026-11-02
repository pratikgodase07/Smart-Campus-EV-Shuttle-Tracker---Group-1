package com.campus.shuttle.ui;

import com.campus.shuttle.model.*;
import com.campus.shuttle.service.FleetService;
import com.campus.shuttle.service.exception.InvalidTripException;
import com.campus.shuttle.service.exception.RouteNotFoundException;
import com.campus.shuttle.service.exception.ShuttleNotDeployableException;
import com.campus.shuttle.service.exception.ShuttleNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 * Interactive Console UI Runner for the Smart Campus EV Shuttle Tracker.
 * Demonstrates all functional requirements (FR-1 to FR-5) without crashing.
 */
@Component
public class FleetConsoleRunner implements CommandLineRunner {

    private final FleetService fleetService;

    @Autowired
    public FleetConsoleRunner(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    @Override
    public void run(String... args) {
        // Skip interactive loop during automated test execution
        if (System.getProperty("spring.profiles.active") != null &&
            System.getProperty("spring.profiles.active").contains("test")) {
            return;
        }

        Scanner scanner = new Scanner(System.in);
        boolean running = true;

        printBanner();

        try {
            while (running) {
                printMenu();
                System.out.print("👉 Enter choice (1 - 9): ");

                if (!scanner.hasNextLine()) {
                    System.out.println("\n[Console Runner] Non-interactive environment detected. Exiting gracefully.");
                    break;
                }

                String input = scanner.nextLine().trim();

                try {
                    switch (input) {
                        case "1":
                            handleRegisterShuttle(scanner);
                            break;
                        case "2":
                            handleViewAllShuttles();
                            break;
                        case "3":
                            handleViewDeployableShuttles();
                            break;
                        case "4":
                            handleLogTrip(scanner);
                            break;
                        case "5":
                            handleViewAnalytics();
                            break;
                        case "6":
                            handlePlaceUnderMaintenance(scanner);
                            break;
                        case "7":
                            handleRechargeAndRelease(scanner);
                            break;
                        case "8":
                            handleViewRoutesAndLogs();
                            break;
                        case "9":
                            System.out.println("\n👋 Exiting Smart Campus EV Shuttle Tracker. Have a great day!\n");
                            running = false;
                            break;
                        default:
                            System.out.println("⚠️  Invalid choice! Please select an option between 1 and 9.");
                    }
                } catch (Exception e) {
                    System.out.println("❌ Error: " + e.getMessage());
                }

                if (running) {
                    System.out.println("\nPress [Enter] to continue...");
                    if (scanner.hasNextLine()) {
                        scanner.nextLine();
                    } else {
                        break;
                    }
                }
            }
        } catch (NoSuchElementException e) {
            // Graceful exit if input stream ends
            System.out.println("\nInput stream ended. Exiting console runner.");
        }
    }

    private void printBanner() {
        System.out.println("\n================================================================================");
        System.out.println("          ⚡ SMART CAMPUS EV SHUTTLE TRACKER & FLEET ALLOCATION ⚡              ");
        System.out.println("                   Spring Core & Spring JDBC Enterprise CLI                     ");
        System.out.println("================================================================================");
    }

    private void printMenu() {
        System.out.println("\n--------------------------------------------------");
        System.out.println("                   FLEET MENU                     ");
        System.out.println("--------------------------------------------------");
        System.out.println("  1. [FR-1] Register / Onboard New Shuttle");
        System.out.println("  2. [FR-1] Fleet Inventory Dashboard (All Shuttles)");
        System.out.println("  3. [FR-2] View Deployable Shuttles (AVAILABLE & Battery > 20%)");
        System.out.println("  4. [FR-3] Log Route Trip & Deplete Battery (Auto-Maintenance < 15%)");
        System.out.println("  5. [FR-4] Multi-Table Joined Fleet Analytics (Trips & Passengers)");
        System.out.println("  6. [FR-5] Manual Maintenance Flag / Override");
        System.out.println("  7. Recharge & Release Shuttle from Maintenance");
        System.out.println("  8. View Campus Routes & Recent Trip Logs");
        System.out.println("  9. Exit Application");
        System.out.println("--------------------------------------------------");
    }

    private void handleRegisterShuttle(Scanner scanner) {
        System.out.println("\n--- [FR-1] Onboard New Shuttle ---");
        System.out.print("Enter driver name: ");
        if (!scanner.hasNextLine()) return;
        String name = scanner.nextLine().trim();

        System.out.print("Enter initial battery percentage (0 - 100): ");
        if (!scanner.hasNextLine()) return;
        int battery;
        try {
            battery = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid battery number! Registration cancelled.");
            return;
        }

        Shuttle registered = fleetService.registerShuttle(name, battery);
        System.out.printf("✅ Successfully registered Shuttle #%d for driver '%s' with %d%% battery (Status: %s)%n",
                registered.getShuttleId(), registered.getDriverName(), registered.getBatteryPct(), registered.getStatus());
    }

    private void handleViewAllShuttles() {
        System.out.println("\n--- [FR-1] Fleet Inventory Dashboard ---");
        List<Shuttle> shuttles = fleetService.getAllShuttles();
        if (shuttles.isEmpty()) {
            System.out.println("No shuttles currently registered in fleet.");
            return;
        }
        printShuttleTable(shuttles);
    }

    private void handleViewDeployableShuttles() {
        System.out.println("\n--- [FR-2] Deployable Shuttles (status = 'AVAILABLE' AND battery > 20%) ---");
        List<Shuttle> deployable = fleetService.getDeployableShuttles();
        if (deployable.isEmpty()) {
            System.out.println("⚠️  No shuttles currently meet deployability criteria (AVAILABLE & Battery > 20%).");
            return;
        }
        printShuttleTable(deployable);
    }

    private void handleLogTrip(Scanner scanner) {
        System.out.println("\n--- [FR-3] Log Route Trip & Battery Depletion ---");
        System.out.print("Enter Shuttle ID to dispatch: ");
        if (!scanner.hasNextLine()) return;
        int shuttleId;
        try {
            shuttleId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid Shuttle ID!");
            return;
        }

        System.out.print("Enter Route Stop ID: ");
        if (!scanner.hasNextLine()) return;
        int routeId;
        try {
            routeId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid Route ID!");
            return;
        }

        System.out.print("Enter number of passengers boarded: ");
        if (!scanner.hasNextLine()) return;
        int passengers;
        try {
            passengers = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid passenger count!");
            return;
        }

        try {
            TripResult result = fleetService.logTrip(shuttleId, routeId, passengers);
            System.out.println("\n🎉 TRIP SUCCESSFULLY COMPLETED!");
            System.out.printf("   • Trip Log ID        : #%d%n", result.getTripLogId());
            System.out.printf("   • Shuttle Assigned   : #%d (%s)%n", result.getShuttleId(), result.getDriverName());
            System.out.printf("   • Route Stop         : #%d (%s)%n", result.getRouteId(), result.getStopName());
            System.out.printf("   • Passengers Boarded : %d passengers%n", result.getPassengersBoarded());
            System.out.printf("   • Battery Before     : %d%%%n", result.getBatteryBefore());
            System.out.printf("   • Battery Deducted   : -%d%%%n", result.getBatteryDeducted());
            System.out.printf("   • Current Battery    : %d%%%n", result.getBatteryAfter());
            System.out.printf("   • Vehicle Status     : %s%n", result.getStatus());

            if (result.isAutoMaintenanceTriggered()) {
                System.out.println("   ⚠️ AUTOMATED BUSINESS RULE TRIGGERED:");
                System.out.println("      Battery dropped below 15%! Vehicle status automatically changed to 'MAINTENANCE'.");
            }
        } catch (ShuttleNotDeployableException | ShuttleNotFoundException | RouteNotFoundException | InvalidTripException ex) {
            System.out.println("❌ Dispatch Rejected: " + ex.getMessage());
        }
    }

    private void handleViewAnalytics() {
        System.out.println("\n--- [FR-4] Multi-Table Joined Analytics (Shuttles + Trips + Passengers) ---");
        List<ShuttleAnalyticsDTO> analytics = fleetService.getFleetAnalytics();

        System.out.printf("+------------+----------------------+---------------+---------------+-------------+------------------+%n");
        System.out.printf("| Shuttle ID | Driver Name          | Battery (%%)   | Status        | Total Trips | Total Passengers |%n");
        System.out.printf("+------------+----------------------+---------------+---------------+-------------+------------------+%n");
        for (ShuttleAnalyticsDTO a : analytics) {
            System.out.printf("| %-10d | %-20s | %-13s | %-13s | %-11d | %-16d |%n",
                    a.getShuttleId(),
                    truncate(a.getDriverName(), 20),
                    a.getBatteryPct() + "%",
                    a.getStatus(),
                    a.getTotalTrips(),
                    a.getTotalPassengers());
        }
        System.out.printf("+------------+----------------------+---------------+---------------+-------------+------------------+%n");
    }

    private void handlePlaceUnderMaintenance(Scanner scanner) {
        System.out.println("\n--- [FR-5] Manual Maintenance Flag & Override ---");
        System.out.print("Enter Shuttle ID to place under maintenance: ");
        if (!scanner.hasNextLine()) return;
        int shuttleId;
        try {
            shuttleId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid Shuttle ID!");
            return;
        }

        try {
            Shuttle updated = fleetService.placeShuttleUnderMaintenance(shuttleId);
            System.out.printf("🛠️  Shuttle #%d (%s) has been placed under MAINTENANCE. Further trip dispatches blocked.%n",
                    updated.getShuttleId(), updated.getDriverName());
        } catch (ShuttleNotFoundException ex) {
            System.out.println("❌ " + ex.getMessage());
        }
    }

    private void handleRechargeAndRelease(Scanner scanner) {
        System.out.println("\n--- Recharge & Release from Maintenance ---");
        System.out.print("Enter Shuttle ID to recharge: ");
        if (!scanner.hasNextLine()) return;
        int shuttleId;
        try {
            shuttleId = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid Shuttle ID!");
            return;
        }

        System.out.print("Enter recharged battery percentage (21 - 100): ");
        if (!scanner.hasNextLine()) return;
        int battery;
        try {
            battery = Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("❌ Invalid battery percentage!");
            return;
        }

        try {
            Shuttle updated = fleetService.rechargeAndReleaseShuttle(shuttleId, battery);
            System.out.printf("⚡ Shuttle #%d (%s) recharged to %d%% and released to AVAILABLE.%n",
                    updated.getShuttleId(), updated.getDriverName(), updated.getBatteryPct());
        } catch (Exception ex) {
            System.out.println("❌ " + ex.getMessage());
        }
    }

    private void handleViewRoutesAndLogs() {
        System.out.println("\n--- Campus Hub Routes ---");
        List<Route> routes = fleetService.getAllRoutes();
        System.out.printf("+----------+--------------------------------+----------------+%n");
        System.out.printf("| Route ID | Campus Stop Name               | Sequence Order |%n");
        System.out.printf("+----------+--------------------------------+----------------+%n");
        for (Route r : routes) {
            System.out.printf("| %-8d | %-30s | %-14d |%n", r.getRouteId(), truncate(r.getStopName(), 30), r.getSequenceOrder());
        }
        System.out.printf("+----------+--------------------------------+----------------+%n");

        System.out.println("\n--- Recent Trip Logs ---");
        List<TripLog> logs = fleetService.getAllTripLogs();
        if (logs.isEmpty()) {
            System.out.println("No trips logged yet.");
            return;
        }
        System.out.printf("+--------+------------+----------+-------------------+----------------------------+%n");
        System.out.printf("| Log ID | Shuttle ID | Route ID | Passengers Logged | Trip Timestamp             |%n");
        System.out.printf("+--------+------------+----------+-------------------+----------------------------+%n");
        for (TripLog t : logs) {
            System.out.printf("| %-6d | %-10d | %-8d | %-17d | %-26s |%n",
                    t.getLogId(), t.getShuttleId(), t.getRouteId(), t.getPassengersBoarded(), String.valueOf(t.getTripTimestamp()));
        }
        System.out.printf("+--------+------------+----------+-------------------+----------------------------+%n");
    }

    private void printShuttleTable(List<Shuttle> shuttles) {
        System.out.printf("+------------+----------------------+---------------+---------------+%n");
        System.out.printf("| Shuttle ID | Driver Name          | Battery (%%)   | Status        |%n");
        System.out.printf("+------------+----------------------+---------------+---------------+%n");
        for (Shuttle s : shuttles) {
            System.out.printf("| %-10d | %-20s | %-13s | %-13s |%n",
                    s.getShuttleId(),
                    truncate(s.getDriverName(), 20),
                    s.getBatteryPct() + "%",
                    s.getStatus());
        }
        System.out.printf("+------------+----------------------+---------------+---------------+%n");
    }

    private String truncate(String text, int maxLength) {
        if (text == null) return "";
        return text.length() > maxLength ? text.substring(0, maxLength - 3) + "..." : text;
    }
}
