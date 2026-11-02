package com.campus.shuttle_management;

import com.campus.shuttle_management.controller.FleetConsoleRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

/**
 * Application entry point for the Smart Campus EV Shuttle Tracker.
 *
 * <p>{@code @SpringBootApplication} enables component scanning across
 * all sub-packages, auto-configures the DataSource and JdbcTemplate
 * from {@code application.properties}, and activates transaction support.
 *
 * <p>After the Spring context is fully loaded, we pull the
 * {@link FleetConsoleRunner} bean and hand off to the interactive menu.
 */
@SpringBootApplication
public class ShuttleManagementApplication {

    public static void main(String[] args) {
        ApplicationContext context =
            SpringApplication.run(ShuttleManagementApplication.class, args);

        // Start the interactive dispatcher console
        FleetConsoleRunner runner = context.getBean(FleetConsoleRunner.class);
        runner.run();

        System.exit(0);
    }
}
