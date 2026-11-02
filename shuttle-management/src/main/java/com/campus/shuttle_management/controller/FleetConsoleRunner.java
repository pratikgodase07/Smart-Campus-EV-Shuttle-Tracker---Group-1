package com.campus.shuttle_management.controller;

import com.campus.shuttle_management.model.Shuttle;
import com.campus.shuttle_management.model.ShuttleAnalytics;
import com.campus.shuttle_management.service.FleetService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

/**
 * Interactive console menu for the campus fleet dispatcher.
 *
 * <p>Reads operator input, calls {@link FleetService}, and formats
 * the output neatly. Zero SQL, zero business logic — this class
 * only handles I/O and delegates everything to the service layer.
 */
@Component
public class FleetConsoleRunner {

    private final FleetService fleetService;
    private final Scanner      scanner = new Scanner(System.in);

    public FleetConsoleRunner(FleetService fleetService) {
        this.fleetService = fleetService;
    }

    // ── Entry Point ───────────────────────────────────────────────

    public void run() {
        printBanner();

        boolean active = true;
        while (active) {
            printMenu();
            String choice = scanner.nextLine().trim();

            switch (choice) {
                case "1" -> handleRegister();
                case "2" -> handleDashboard();
                case "3" -> handleAvailable();
                case "4" -> handleDispatch();
                case "5" -> handleMaintenanceFlag();
                case "6" -> handleAnalytics();
                case "0" -> {
                    System.out.println("\n  Goodbye! Fleet Manager shutting down.\n");
                    active = false;
                }
                default  -> System.out.println("\n  Invalid option — please choose 0 through 6.\n");
            }
        }
    }

    // ── Menu Formatting ───────────────────────────────────────────

    private void printBanner() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════════════╗");
        System.out.println("  ║     Smart Campus EV Shuttle Fleet Manager        ║");
        System.out.println("  ║     Spring JDBC Console App — Group 1            ║");
        System.out.println("  ╚══════════════════════════════════════════════════╝");
        System.out.println();
    }

    private void printMenu() {
        System.out.println("  ┌────────────────────────────────────┐");
        System.out.println("  │           FLEET MENU               │");
        System.out.println("  ├────────────────────────────────────┤");
        System.out.println("  │  1.  Register New Shuttle          │");
        System.out.println("  │  2.  Fleet Dashboard               │");
        System.out.println("  │  3.  Available Shuttles            │");
        System.out.println("  │  4.  Dispatch a Trip               │");
        System.out.println("  │  5.  Flag Maintenance Override     │");
        System.out.println("  │  6.  Fleet Analytics Report        │");
        System.out.println("  │  0.  Exit                          │");
        System.out.println("  └────────────────────────────────────┘");
        System.out.print("  Your choice: ");
    }

    // ── FR-1: Register ────────────────────────────────────────────

    private void handleRegister() {
        System.out.println("\n  ── Register New Shuttle ─────────────────");
        System.out.print("  Driver Name  : ");
        String name = scanner.nextLine().trim();

        System.out.print("  Battery %    : ");
        int battery = readInt();

        try {
            fleetService.registerShuttle(name, battery);
        } catch (IllegalArgumentException e) {
            System.out.println("\n  ✖  " + e.getMessage() + "\n");
        }
    }

    // ── FR-1: Dashboard ───────────────────────────────────────────

    private void handleDashboard() {
        System.out.println("\n  ── Fleet Dashboard ──────────────────────");
        List<Shuttle> fleet = fleetService.getFleetDashboard();

        if (fleet.isEmpty()) {
            System.out.println("  No shuttles registered yet.\n");
            return;
        }

        System.out.printf("  %-5s  %-20s  %-10s  %-14s%n", "ID", "Driver", "Battery %", "Status");
        System.out.println("  " + "─".repeat(55));
        fleet.forEach(s -> System.out.printf(
            "  %-5d  %-20s  %-10d  %-14s%n",
            s.getShuttleId(), s.getDriverName(), s.getBatteryPct(), s.getStatus()
        ));
        System.out.println();
    }

    // ── FR-2: Available ───────────────────────────────────────────

    private void handleAvailable() {
        System.out.println("\n  ── Available Shuttles (AVAILABLE & battery > 20%) ──");
        List<Shuttle> available = fleetService.getAvailableShuttles();

        if (available.isEmpty()) {
            System.out.println("  No shuttles are currently deployable.\n");
            return;
        }

        available.forEach(s -> System.out.printf(
            "  Shuttle #%-3d  %-20s  Battery: %d%%%n",
            s.getShuttleId(), s.getDriverName(), s.getBatteryPct()
        ));
        System.out.println();
    }

    // ── FR-3: Dispatch ────────────────────────────────────────────

    private void handleDispatch() {
        System.out.println("\n  ── Dispatch a Trip ──────────────────────");
        System.out.print("  Shuttle ID          : ");
        int shuttleId = readInt();

        System.out.print("  Route ID            : ");
        int routeId = readInt();

        System.out.print("  Passengers boarded  : ");
        int passengers = readInt();

        try {
            fleetService.dispatchTrip(shuttleId, routeId, passengers);
        } catch (IllegalArgumentException | IllegalStateException e) {
            System.out.println("\n  ✖  Dispatch blocked: " + e.getMessage() + "\n");
        }
    }

    // ── FR-5: Maintenance Override ────────────────────────────────

    private void handleMaintenanceFlag() {
        System.out.println("\n  ── Maintenance Override ─────────────────");
        System.out.print("  Shuttle ID to flag  : ");
        int shuttleId = readInt();

        try {
            fleetService.flagMaintenanceOverride(shuttleId);
        } catch (IllegalArgumentException e) {
            System.out.println("\n  ✖  " + e.getMessage() + "\n");
        }
    }

    // ── FR-4: Analytics ───────────────────────────────────────────

    private void handleAnalytics() {
        System.out.println("\n  ── Fleet Analytics Report ───────────────");
        List<ShuttleAnalytics> report = fleetService.getFleetAnalytics();

        if (report.isEmpty()) {
            System.out.println("  No data available yet.\n");
            return;
        }

        System.out.printf("  %-4s  %-20s  %-14s  %-9s  %-7s  %s%n",
            "ID", "Driver", "Status", "Battery%", "Trips", "Passengers");
        System.out.println("  " + "─".repeat(72));

        report.forEach(a -> System.out.printf(
            "  %-4d  %-20s  %-14s  %-9d  %-7d  %d%n",
            a.getShuttleId(), a.getDriverName(), a.getStatus(),
            a.getBatteryPct(), a.getTotalTrips(), a.getTotalPassengers()
        ));
        System.out.println();
    }

    // ── Utility ───────────────────────────────────────────────────

    private int readInt() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("  Invalid input — defaulting to 0.");
            return 0;
        }
    }
}
