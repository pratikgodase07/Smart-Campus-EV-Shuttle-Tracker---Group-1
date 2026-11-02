# Smart Campus EV Shuttle Tracker (Group 1)

Spring Core & Spring JDBC console application for campus electric vehicle (EV) fleet monitoring, dynamic route allocation, trip logging, and automated maintenance workflows.

---

## 👥 Team Roles & Responsibilities

| Member | Assigned Role | Branch | Key Responsibilities |
|---|---|---|---|
| **Pratik Godase** | Member 1: Project Setup & Architecture | `feature/pratik` | Spring Boot skeleton, Maven config, initial architecture |
| **Utkarsh Tripathi** | Member 2: Shuttle DAO Specialist | `feature/shuttle-dao` | Domain model (POJO), RowMapper, ShuttleDao & ShuttleDaoImpl (JdbcTemplate), schema/data SQL, tests |
| **Atharva** | Member 3 | `feature/atharva` | Route DAO & Route Management |
| **Himanshu** | Member 4 | `feature/himanshu` | Trip Log DAO & Multi-Table Analytics |
| **Omkar** | Member 5 | `feature/omkar` | Service Layer & Business Logic Automation |
| **Varun** | Member 6 | `feature/Varun` | Fleet Console Runner & Interactive CLI |

---

## 🚀 Utkarsh Tripathi (Member 2) - Shuttle DAO Specialist Contributions

### 1. Domain Model (`Shuttle.java`)
- **Package**: `com.campus.shuttle_management.model`
- **Fields**: `shuttleId`, `driverName`, `batteryPct`, `status`
- **Design**: Encapsulated POJO with default constructor, constructor for onboarding without ID, full constructor with ID, getters/setters, `toString()`, `equals()`, and `hashCode()`.

### 2. Custom RowMapper (`ShuttleRowMapper.java`)
- **Package**: `com.campus.shuttle_management.rowmapper`
- **Implementation**: Implements Spring's `RowMapper<Shuttle>`.
- **Mapping**:
  - `shuttle_id` $\rightarrow$ `shuttleId`
  - `driver_name` $\rightarrow$ `driverName`
  - `battery_pct` $\rightarrow$ `batteryPct`
  - `status` $\rightarrow$ `status`

### 3. DAO Layer (`ShuttleDao` & `ShuttleDaoImpl`)
- **Package**: `com.campus.shuttle_management.dao`
- **Technology**: Spring JDBC with `JdbcTemplate`, using constructor dependency injection and parameterized queries (`?`) to prevent SQL injection.
- **Methods**:
  - `int insertShuttle(Shuttle shuttle)`: **[FR-1]** Registers new shuttles using `PreparedStatement` and retrieves auto-generated primary key via `GeneratedKeyHolder`.
  - `List<Shuttle> findAllShuttles()`: **[FR-1]** Retrieves all fleet records ordered by `shuttle_id` for the inventory dashboard.
  - `List<Shuttle> findDeployableShuttles()`: **[FR-2]** Parameterized query selecting only vehicles with `status = 'AVAILABLE'` and `battery_pct > 20%`, sorted by battery descending.
  - `int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus)`: **[FR-3]** Updates battery level and status following trip completion (e.g. setting to `MAINTENANCE` if battery < 15%).
  - `int updateStatus(int shuttleId, String status)`: **[FR-5]** Manually overrides status to place any vehicle under `MAINTENANCE`.
  - `Optional<Shuttle> findById(int shuttleId)`: Safely queries a single shuttle by ID without throwing `EmptyResultDataAccessException`.

### 4. Database Schema & Seed Data
- **`schema.sql`**: Relational MySQL DDL defining `shuttles`, `routes`, and `trip_logs` with primary keys, foreign keys (`ON DELETE CASCADE`), and constraints.
- **`data.sql`**: Comprehensive seed dataset covering:
  - Deployable shuttles (`AVAILABLE` with battery > 20%)
  - Low-battery available shuttles (`AVAILABLE` with battery <= 20%)
  - In-transit shuttles (`IN_TRANSIT`)
  - Charging shuttles (`CHARGING`)
  - Maintenance shuttles (`MAINTENANCE`)
  - Campus routes and trip logs with passenger counts

### 5. Automated Integration & Unit Tests (`ShuttleDaoTest.java`)
- **Package**: `com.campus.shuttle_management.dao`
- **Testing Scope**:
  - Uses `@JdbcTest` with an in-memory database to test all DAO methods in isolation.
  - Validates `insertShuttle` and auto-generated key population.
  - Validates `findDeployableShuttles` filtering (asserts non-deployable states are correctly excluded).
  - Validates `updateShuttleBatteryAndStatus` and `updateStatus`.
  - Validates safe `findById` lookup.

---

## 🗄️ Database Schema Design

```sql
CREATE TABLE shuttles (
    shuttle_id INT AUTO_INCREMENT PRIMARY KEY,
    driver_name VARCHAR(100) NOT NULL,
    battery_pct INT NOT NULL,
    status VARCHAR(30) NOT NULL -- 'AVAILABLE', 'IN_TRANSIT', 'CHARGING', 'MAINTENANCE'
);

CREATE TABLE routes (
    route_id INT AUTO_INCREMENT PRIMARY KEY,
    stop_name VARCHAR(100) NOT NULL,
    sequence_order INT NOT NULL
);

CREATE TABLE trip_logs (
    log_id INT AUTO_INCREMENT PRIMARY KEY,
    shuttle_id INT NOT NULL,
    route_id INT NOT NULL,
    passengers_boarded INT NOT NULL,
    trip_timestamp TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (shuttle_id) REFERENCES shuttles(shuttle_id) ON DELETE CASCADE,
    FOREIGN KEY (route_id) REFERENCES routes(route_id) ON DELETE CASCADE
);
```

---

## 🛠️ Tech Stack & Requirements
- **Java**: 17+ (or 21)
- **Framework**: Spring Boot 3.x/4.x & Spring JDBC (`JdbcTemplate`, `RowMapper`)
- **Database**: MySQL 8.x (with H2 in-memory support for testing)
- **Build Tool**: Apache Maven (`mvnw`)

---

## 🏃 Build & Run Instructions

```bash
cd shuttle-management

# Clean and compile
./mvnw clean compile

# Execute tests
./mvnw test
```
