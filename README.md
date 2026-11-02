# Smart Campus EV Shuttle Tracker (Group 1)

Spring Core & Spring JDBC application for campus electric vehicle (EV) fleet monitoring, route allocation, trip logging, and automated maintenance workflows.

---

## 👥 Member 2: Shuttle DAO Specialist

- **Branch**: `feature/shuttle-dao` / `feature/utkarsh`
- **Assigned Module**: Domain Model, RowMapper, and DAO Layer for Shuttle Entity

### Responsibilities Completed:
1. **Domain Model (`Shuttle.java`)**:
   - Location: `shuttle-management/src/main/java/com/campus/shuttle_management/model/Shuttle.java`
   - Fields: `shuttleId`, `driverName`, `batteryPct`, `status`
   - Constructors, Getters/Setters, `toString()`, `equals()`, and `hashCode()`
2. **RowMapper (`ShuttleRowMapper.java`)**:
   - Location: `shuttle-management/src/main/java/com/campus/shuttle_management/rowmapper/ShuttleRowMapper.java`
   - Implements `RowMapper<Shuttle>` to cleanly map SQL columns (`shuttle_id`, `driver_name`, `battery_pct`, `status`) to `Shuttle` POJO.
3. **DAO Layer (`ShuttleDao` & `ShuttleDaoImpl`)**:
   - Location: `shuttle-management/src/main/java/com/campus/shuttle_management/dao/`
   - `int insertShuttle(Shuttle shuttle)` (FR-1: Fleet Onboarding, captures generated keys)
   - `List<Shuttle> findAllShuttles()` (FR-1: Fleet Inventory Dashboard)
   - `List<Shuttle> findDeployableShuttles()` (FR-2: Parameterized query for `status = ? ('AVAILABLE')` and `battery_pct > ? (20)`)
   - `int updateShuttleBatteryAndStatus(int shuttleId, int newBattery, String newStatus)` (FR-3: Trip completion & battery depletion)
   - `int updateStatus(int shuttleId, String status)` (FR-5: Maintenance override)
   - `Optional<Shuttle> findById(int shuttleId)` (Convenience query with safe Optional return)
4. **Integration & Unit Tests (`ShuttleDaoTest.java`)**:
   - Location: `shuttle-management/src/test/java/com/campus/shuttle_management/dao/ShuttleDaoTest.java`

---

## 🛠️ Tech Stack & Requirements
- **Java**: 17+ (or 21)
- **Framework**: Spring Boot 3.x/4.x & Spring JDBC (`JdbcTemplate`, `RowMapper`)
- **Database**: MySQL 8.x (with H2 in-memory support for testing)
- **Build Tool**: Apache Maven (`mvnw`)

---

## 🗄️ Database Schema (`shuttles` table)

```sql
CREATE TABLE shuttles (
    shuttle_id INT AUTO_INCREMENT PRIMARY KEY,
    driver_name VARCHAR(100) NOT NULL,
    battery_pct INT NOT NULL,
    status VARCHAR(30) NOT NULL -- 'AVAILABLE', 'IN_TRANSIT', 'CHARGING', 'MAINTENANCE'
);
```

---

## 🚀 Running the Project

```bash
cd shuttle-management

# Compile
./mvnw clean compile

# Run tests
./mvnw test
```
