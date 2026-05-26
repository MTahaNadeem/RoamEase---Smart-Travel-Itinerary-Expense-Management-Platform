# RoamEase - Smart Travel Itinerary & Expense Management Platform

## Overview
RoamEase is a comprehensive CLI application for managing travel trips, itineraries, and shared expenses. Built with Java 17+, SQLite, and following strict OOP and SOLID principles.

**Course:** SE3512 - Software Construction and Development  
**Instructor:** Ms. Nadia Ashfaq  
**Team:** Muhammad Taha, Abdul Haseeb, Haseeb Badshah, Mustafa

## Features
- **Trip Management**: Create and manage travel trips with destinations and dates
- **Itinerary Events**: Add and organize events during your trips
- **Expense Tracking**: Log expenses and track who paid for what
- **Smart Split Calculation**: Calculate fair expense splits among trip participants
- **User Management**: Create and manage user profiles

## Technology Stack
- **Language**: Java 17+
- **Database**: SQLite (with sqlite-jdbc 3.43.0.0)
- **Build Tools**: Maven 3.8+ or Gradle 8+
- **Architecture**: Monolithic with DAO, Service, and CLI layers
- **Design Patterns**: Singleton, Dependency Injection, DAO, MVC

## Project Structure
```
RoamEase/
├── src/com/cust/roamease/
│   ├── Main.java                          # Entry point
│   ├── models/                            # Data models
│   │   ├── User.java
│   │   ├── Trip.java
│   │   ├── Expense.java
│   │   └── ItineraryEvent.java
│   ├── interfaces/                        # Contracts
│   │   ├── ICalculable.java               # Expense calculation
│   │   └── IPersistable.java              # Persistence contract
│   ├── db/                                # Data Access Layer
│   │   ├── DatabaseManager.java           # SQLite connection & schema
│   │   ├── UserDAO.java
│   │   ├── TripDAO.java
│   │   ├── ExpenseDAO.java
│   │   └── ItineraryEventDAO.java
│   ├── services/                          # Business Logic Layer
│   │   ├── TripService.java               # Trip orchestration
│   │   └── ExpenseManager.java            # Expense logic (implements ICalculable)
│   └── ui/                                # Presentation Layer
│       └── ConsoleApp.java                # CLI interface
├── database/                              # SQLite database (auto-created)
├── lib/                                   # External libraries
├── pom.xml                                # Maven configuration
├── build.gradle                           # Gradle configuration
└── README.md                              # This file
```

## Building the Project

### Option 1: Using Maven
```bash
# Compile the project
mvn clean compile

# Package the application (creates JAR with dependencies)
mvn clean package

# Run the application
mvn exec:java -Dexec.mainClass="com.cust.roamease.Main"

# Or run the packaged JAR
java -jar target/roamease.jar
```

### Option 2: Using Gradle
```bash
# Compile the project
gradle build

# Create fat JAR with all dependencies
gradle fatJar

# Run the application
gradle run

# Or run the packaged JAR
java -jar build/libs/roamease.jar
```

### Option 3: Using IDE (IntelliJ IDEA / Eclipse)
1. Open the project in your IDE
2. Mark `src` folder as Sources Root
3. Configure project SDK as Java 17+
4. Right-click on `Main.java` → Run

## Running the Application

After building, run the application with:
```bash
java -jar roamease.jar
```

Or if using Maven:
```bash
mvn exec:java -Dexec.mainClass="com.cust.roamease.Main"
```

## CLI Menu Options

```
========== Main Menu ==========
1. Create Trip                    - Create a new travel trip
2. Add Itinerary Event            - Add events to trip itinerary
3. Log Expense                    - Record trip expenses
4. Calculate Expense Split        - Calculate who owes what
5. Manage Users                   - Create and view users
6. View Trips                     - View all trips and details
7. Exit                           - Close the application
```

## Usage Workflow

### 1. Create Users
- Go to **Manage Users** → **Create User**
- Add user name and email
- Note the generated User ID

### 2. Create a Trip
- Go to **Create Trip**
- Enter destination, start date, and end date
- Add participants from existing users

### 3. Add Itinerary Events
- Go to **Add Itinerary Event**
- Select trip ID
- Add events with title, date/time, and location

### 4. Log Expenses
- Go to **Log Expense**
- Select trip ID and payer
- Enter amount and description

### 5. Calculate Split
- Go to **Calculate Expense Split**
- Select trip ID
- View detailed settlement information

## Database Schema

### Users Table
```sql
CREATE TABLE users (
    user_id INTEGER PRIMARY KEY AUTOINCREMENT,
    name TEXT NOT NULL,
    email TEXT NOT NULL UNIQUE
);
```

### Trips Table
```sql
CREATE TABLE trips (
    trip_id INTEGER PRIMARY KEY AUTOINCREMENT,
    destination TEXT NOT NULL,
    start_date TEXT NOT NULL,
    end_date TEXT NOT NULL
);
```

### Itinerary Events Table
```sql
CREATE TABLE itinerary_events (
    event_id INTEGER PRIMARY KEY AUTOINCREMENT,
    trip_id INTEGER NOT NULL,
    title TEXT NOT NULL,
    datetime TEXT NOT NULL,
    location TEXT NOT NULL,
    FOREIGN KEY(trip_id) REFERENCES trips(trip_id)
);
```

### Expenses Table
```sql
CREATE TABLE expenses (
    expense_id INTEGER PRIMARY KEY AUTOINCREMENT,
    trip_id INTEGER NOT NULL,
    payer_id INTEGER NOT NULL,
    amount REAL NOT NULL,
    description TEXT NOT NULL,
    FOREIGN KEY(trip_id) REFERENCES trips(trip_id),
    FOREIGN KEY(payer_id) REFERENCES users(user_id)
);
```

### Trip Participants Table
```sql
CREATE TABLE trip_participants (
    participant_id INTEGER PRIMARY KEY AUTOINCREMENT,
    trip_id INTEGER NOT NULL,
    user_id INTEGER NOT NULL,
    FOREIGN KEY(trip_id) REFERENCES trips(trip_id),
    FOREIGN KEY(user_id) REFERENCES users(user_id),
    UNIQUE(trip_id, user_id)
);
```

## SOLID Principles Implementation

### Single Responsibility Principle (SRP)
- **DatabaseManager**: Only manages database connection
- **DAOs**: Each DAO handles only its entity's database operations
- **Services**: Business logic separated from data access
- **ConsoleApp**: UI logic only

### Open/Closed Principle (OCP)
- **ICalculable Interface**: New calculation strategies can extend without modifying existing code
- **IPersistable Interface**: New persistable objects without changing existing implementations

### Liskov Substitution Principle (LSP)
- **ExpenseManager implements ICalculable**: Can be used anywhere ICalculable is expected
- Interfaces ensure consistent contract compliance

### Interface Segregation Principle (ISP)
- **ICalculable**: Specific to calculation operations
- **IPersistable**: Specific to persistence operations
- Clients depend only on interfaces they use

### Dependency Inversion Principle (DIP)
- **Services**: Depend on DAO abstractions, not concrete implementations
- **ConsoleApp**: Uses Service layer, decoupled from database layer

## Design Patterns Used

1. **Singleton Pattern**: DatabaseManager ensures single database connection
2. **DAO Pattern**: Data access abstraction layer
3. **Dependency Injection**: Services receive dependencies through constructor
4. **Service Layer**: Business logic orchestration
5. **MVC Pattern**: Model-View-Controller adapted for CLI

## Date/Time Format

- **Dates**: `yyyy-MM-dd` (e.g., 2026-05-26)
- **DateTime**: `yyyy-MM-dd'T'HH:mm` (e.g., 2026-05-26T14:30)

## Error Handling

The application includes comprehensive error handling for:
- Invalid date formats
- Non-existent users/trips
- Duplicate email addresses
- SQL operation failures
- User input validation

## Sample Test Data

To test the application, create:
- **Users**: Ali Ahmed, Zainab Khan, Haris Ali
- **Trip**: Paris 2026-06-01 to 2026-06-08
- **Add Participants**: All three users
- **Events**: Eiffel Tower, Louvre Museum, Seine Cruise
- **Expenses**: Various amounts from different payers

## Troubleshooting

### Dependency Issues
```bash
# Maven
mvn clean install -U

# Gradle
gradle clean build --refresh-dependencies
```

### SQLite Driver Not Found
Ensure `pom.xml` or `build.gradle` has the sqlite-jdbc dependency

### Port/Database Locked
Delete `roamease.db` file to reset the database

## Requirements Met

✅ Complete, production-ready codebase  
✅ No placeholders - fully implemented  
✅ Strict OOP principles  
✅ SOLID design patterns  
✅ SQLite database integration  
✅ CLI interface with Scanner  
✅ Proper encapsulation  
✅ DAO pattern for SQL isolation  
✅ Service layer for business logic  
✅ Header comments with course information  

## Future Enhancements

- GUI interface with JavaFX
- Multi-currency support
- Trip sharing and collaboration
- Advanced reporting and analytics
- Mobile app version
- Cloud database support

## License

Academic Project - SE3512 Course Assignment

## Support

For issues or questions, contact the development team at the university.
