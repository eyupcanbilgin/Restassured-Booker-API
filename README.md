# Restful Booker API Tests

This repository contains automated API tests for the restful-booker service. The tests are written in Java and use Cucumber (with PicoContainer for dependency injection), JUnit, and RestAssured for HTTP interactions.

## 📌 Table of Contents
- [Overview](#overview)
- [Tech Stack](#tech-stack)
- [Project Structure](#project-structure)
- [How to Run Tests](#how-to-run-tests)
- [Endpoints Covered](#endpoints-covered)
- [Key Classes and Files](#key-classes-and-files)
- [Further Notes](#further-notes)

## 📝 Overview

The restful-booker API is a sample application that allows testing CRUD operations on bookings. This project implements automated tests for each endpoint (Auth, Booking, Ping) using Behavior-Driven Development (BDD) style.

- Each endpoint has its own scenario in the feature file, ensuring every API call is tested.
- The tests are data-driven: they create, update, and delete bookings dynamically using the TestContext to share data within each scenario.
- Basic Auth is used for updating, patching, and deleting bookings (the token approach is also demonstrated in `AuthSteps`, but the final solution uses Basic Auth to avoid issues with ephemeral tokens).

## 🛠️ Tech Stack

- **Java 11+**
- **Maven** (Build and dependency management)
- **JUnit 4** (Test runner integration with Cucumber)
- **Cucumber + PicoContainer** (BDD style, DI for step classes)
- **RestAssured** (HTTP client for testing the API)
- **Log4j2** (Logging to console and file)

## 📂 Project Structure

```
DIAS-API/
├── pom.xml
├── src
│   └── test
│       ├── java
│       │   ├── runners
│       │   │   └── RunCucumberTest.java
│       │   ├── steps
│       │   │   ├── AuthSteps.java
│       │   │   ├── BookingSteps.java
│       │   │   └── Hooks.java
│       │   └── utils
│       │       └── TestContext.java
│       └── resources
│           ├── features
│           │   └── booking.feature
│           └── log4j2.xml
└── README.md
```

- **`runners/`**: Contains `RunCucumberTest.java`, the JUnit runner for Cucumber.
- **`steps/`**: Holds step definition classes (`AuthSteps.java`, `BookingSteps.java`) and `Hooks.java` for scenario setup/teardown.
- **`utils/`**: Contains `TestContext.java` which shares data (response, token, bookingId) among steps.
- **`features/`**: Cucumber Gherkin feature file(s) describing scenarios for each API endpoint.
- **`log4j2.xml`**: Logging configuration (console + file logs).

## ▶️ How to Run Tests

### Clone the Repo:
```bash
git clone https://github.com/eyupcanbilgin/DIAS-API.git
cd DIAS-API
```

### Build and Test Using Maven:
```bash
mvn clean test
```

### View Logs:
- Logs are written to the console and also to `logs/test.log` (configured in `log4j2.xml`).
- If using IntelliJ, simply open the project and run `RunCucumberTest` from the IDE.

## 🔗 Endpoints Covered

### **Auth**
- `POST /auth` (Create token)

### **Booking**
- `GET /booking` (Retrieve all booking IDs)
- `GET /booking/{id}` (Retrieve a single booking)
- `POST /booking` (Create a booking)
- `PUT /booking/{id}` (Update a booking)
- `PATCH /booking/{id}` (Partially update a booking)
- `DELETE /booking/{id}` (Delete a booking)

### **Ping**
- `GET /ping` (Health check)

Each API call has a corresponding scenario in `booking.feature`. For calls requiring a `bookingId` (like update, patch, delete), the scenario first creates a booking, stores the ID, then performs the required action.

## 🏗️ Key Classes and Files

### **RunCucumberTest.java**
- Configures Cucumber with `@CucumberOptions` and uses PicoContainer for dependency injection.
- This is the main test runner.

### **AuthSteps.java**
- Handles token creation via `POST /auth`.
- Stores the token in `TestContext`.

### **BookingSteps.java**
- Contains all steps to test `GET /booking`, `POST /booking`, `PUT /booking/{id}`, `PATCH /booking/{id}`, `DELETE /booking/{id}`, as well as the health check (`GET /ping`).
- Uses Basic Auth for update/patch/delete calls.

### **Hooks.java**
- `@Before` sets `RestAssured.baseURI`.
- `@After` logs scenario results.

### **TestContext.java**
- Holds shared data among steps within a single scenario (e.g., response, token, bookingId).

### **booking.feature**
- Gherkin file describing each scenario (Create Token, Get Booking IDs, Create Booking, Get Single Booking, Update, Patch, Delete, Ping).

### **log4j2.xml**
- Log4j2 config: prints logs to console and writes them to `logs/test.log`.

## 📌 Further Notes

### **Basic Auth vs. Token**
- We demonstrate token creation in `AuthSteps` but use Basic Auth (`admin / password123`) for update/patch/delete due to ephemeral environment issues with tokens.

### **Parallel Execution**
- Each scenario is independent. If running tests in parallel, each scenario creates and tears down its own booking data as needed.

### **Customization**
- Feel free to modify `bookingPayload` or add new validations.
- For real-world projects, you might expand the Gherkin steps to handle more negative tests or data-driven scenarios.

---
### **Author**
👤 **Eyüpcan Bilgin** | Role: **SDET**  
📂 GitHub: [eyupcanbilgin](https://github.com/eyupcanbilgin)
