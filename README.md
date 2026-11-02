# ⚡ Smart Campus EV Shuttle Tracker (Group 1)

[![Java 17](https://img.shields.io/badge/Java-17%20%7C%2021-orange.svg)](https://www.oracle.com/java/)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.2.5-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![Spring JDBC](https://img.shields.io/badge/Data%20Access-Spring%20JdbcTemplate-blue.svg)](https://spring.io/)
[![MySQL](https://img.shields.io/badge/Database-MySQL%208.x-blue.svg)](https://www.mysql.com/)

A modular, enterprise-grade console application powered by **Spring Core** and **Spring JDBC (`JdbcTemplate`)** to monitor campus electric vehicle (EV) fleets, manage route allocation, record passenger trip logs, and automate fleet maintenance workflows.

---

## 🏛️ Enterprise Multi-Layer Architecture

The project strictly follows layered separation of concerns to avoid monolithic designs:

```
src/main/java/com/campus/shuttle/
├── SmartCampusShuttleApplication.java          # Spring Boot Main Entry Point
├── model/                                      # Domain POJOs & DTOs
│   ├── Shuttle.java                            # EV Shuttle Entity (shuttleId, driverName, batteryPct, status)
│   ├── Route.java                              # Campus Hub Route Entity (routeId, stopName, sequenceOrder)
│   ├── TripLog.java                            # Completed Trip Record (logId, shuttleId, routeId, passengers)
│   ├── ShuttleAnalyticsDTO.java                # Multi-table Joined Analytics DTO (FR-4)
│   └── TripResult.java                         # Trip Execution Summary DTO with Battery Depletion
├── rowmapper/                                  # Custom Spring RowMapper Implementations
│   ├── ShuttleRowMapper.java                   # ResultSet -> Shuttle mapping
│   ├── RouteRowMapper.java                     # ResultSet -> Route mapping
│   ├── TripLogRowMapper.java                   # ResultSet -> TripLog mapping
│   └── ShuttleAnalyticsRowMapper.java          # Multi-Table Joined ResultSet -> ShuttleAnalyticsDTO
├── dao/                                        # Data Access Layer (Spring JdbcTemplate)
│   ├── ShuttleDao.java & ShuttleDaoImpl.java   # Parameterized JDBC queries for Shuttles
│   ├── RouteDao.java & RouteDaoImpl.java       # Parameterized JDBC queries for Routes
│   └── TripLogDao.java & TripLogDaoImpl.java   # Parameterized JDBC queries for Trip Logs
├── service/                                    # Business Logic & Rules Automation
│   ├── FleetService.java                       # Service Interface
│   ├── FleetServiceImpl.java                   # Automated state transitions & validations
│   └── exception/                              # Domain-specific Custom Exceptions
│       ├── ShuttleNotFoundException.java
│       ├── RouteNotFoundException.java
│       ├── ShuttleNotDeployableException.java
│       └── InvalidTripException.java
└── ui/                                         # Controller / User Interface Layer
    └── FleetConsoleRunner.java                 # Interactive ASCII Terminal CLI Runner
```

---

## 📋 Functional Requirements Coverage

| Requirement | Description | Implementation Highlights |
|---|---|---|
| **FR-1: Shuttle Fleet Onboarding & Inventory** | Register new shuttles with driver name and battery percentage; full inventory dashboard. | `FleetService.registerShuttle()` using `GeneratedKeyHolder` to capture auto-increment IDs; `findAllShuttles()`. |
| **FR-2: Dynamic Availability Querying** | Query deployable vehicles meeting strict criteria (`status = 'AVAILABLE'` AND `battery_pct > 20%`). | Parameterized query `WHERE status = ? AND battery_pct > ?` preventing SQL injection; sorted by highest battery first. |
| **FR-3: Trip Logging & Battery Depletion** | Log completed trips, deduct realistic battery percentage (10% per trip). | Deducts battery; **Automated Business Rule**: If battery drops below **15%**, automatically transitions status to `'MAINTENANCE'`. |
| **FR-4: Multi-Table Joined Analytics** | Display each shuttle with total completed trips and aggregate passenger count. | `LEFT JOIN trip_logs` grouped by `shuttle_id` using custom `ShuttleAnalyticsRowMapper`. |
| **FR-5: Maintenance Flag & Override** | Allow campus dispatchers to manually place any vehicle under maintenance. | Updates status to `'MAINTENANCE'`, instantly preventing subsequent trip dispatches. |

---

## 🗄️ Database Schema Design (MySQL)

Relational integrity enforced with Primary Keys, Foreign Keys, and Cascade Deletions:

```sql
-- Table: shuttles
CREATE TABLE shuttles (
    shuttle_id INT AUTO_INCREMENT PRIMARY KEY,
    driver_name VARCHAR(100) NOT NULL,
    battery_pct INT NOT NULL,
    status VARCHAR(30) NOT NULL -- 'AVAILABLE', 'IN_TRANSIT', 'CHARGING', 'MAINTENANCE'
);

-- Table: routes
CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    stop_name VARCHAR(100) NOT NULL,
    sequence_order INT NOT NULL
);

-- Table: trip_logs
CREATE TABLE trip_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    shuttle_id INT NOT NULL,
    route_id INT NOT NULL,
    passengers_boarded INT NOT NULL,
    trip_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_trip_shuttle FOREIGN KEY (shuttle_id) REFERENCES shuttles(shuttle_id) ON DELETE CASCADE,
    CONSTRAINT fk_trip_route FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);
```

---

## 👥 Team Roles & Responsibilities

| Member | Role | Branch | Completed Contributions |
|---|---|---|---|
| **Utkarsh Tripathi** | Shuttle DAO Specialist & Architecture Lead | `feature/shuttle-dao` / `main` | End-to-end multi-layer architecture, domain models, Spring JDBC DAOs, Service layer automation, and test suite |
| **Pratik Godase** | Project Setup & Initial Scaffolding | `feature/pratik` | Repository initialization, baseline Spring configuration |
| **Varun** | Console Runner & UI | `feature/Varun` | Fleet interactive CLI loop and terminal display formatting |
| **Atharva** | Route Management Specialist | `feature/atharva` | Route data access and stop sequencing |
| **Himanshu** | Analytics & Join Specialist | `feature/himanshu` | Multi-table joins and aggregation reporting |
| **Omkar** | Service Rules & Validation | `feature/omkar` | Business rule assertions and exception handling |

---

## 🚀 How to Run the Application

### 1. Database Setup (MySQL)
Create the database schema in MySQL Workbench or CLI:
```sql
CREATE DATABASE IF NOT EXISTS shuttle_tracker;
```
Verify your MySQL credentials in `src/main/resources/application.properties`:
```properties
spring.datasource.url=jdbc:mysql://localhost:3306/shuttle_tracker?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```
*(Spring Boot will automatically execute `schema.sql` and `data.sql` upon boot).*

### 2. Build and Execute Tests
Run the automated test suite (uses H2 in-memory database with zero external setup needed):
```powershell
./mvnw clean test
```

### 3. Launch Interactive Console UI
Run the Spring Boot console application:
```powershell
./mvnw spring-boot:run
```

---

## 🖥️ Interactive Console Menu Preview

```text
================================================================================
          ⚡ SMART CAMPUS EV SHUTTLE TRACKER & FLEET ALLOCATION ⚡              
                   Spring Core & Spring JDBC Enterprise CLI                     
================================================================================

--------------------------------------------------
                   FLEET MENU                     
--------------------------------------------------
  1. [FR-1] Register / Onboard New Shuttle
  2. [FR-1] Fleet Inventory Dashboard (All Shuttles)
  3. [FR-2] View Deployable Shuttles (AVAILABLE & Battery > 20%)
  4. [FR-3] Log Route Trip & Deplete Battery (Auto-Maintenance < 15%)
  5. [FR-4] Multi-Table Joined Fleet Analytics (Trips & Passengers)
  6. [FR-5] Manual Maintenance Flag / Override
  7. Recharge & Release Shuttle from Maintenance
  8. View Campus Routes & Recent Trip Logs
  9. Exit Application
--------------------------------------------------
👉 Enter choice (1 - 9):
```
