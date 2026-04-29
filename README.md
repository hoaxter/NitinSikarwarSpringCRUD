
#  Student Information Portal

A full-stack **Spring Boot** application for managing student records using **JDBC** and **PostgreSQL**. The system supports complete CRUD operations through REST APIs along with a simple frontend interface.

---

##  Tech Stack

- **Java 17**
- **Spring Boot 3.2.5**
- **Spring JDBC (JdbcTemplate)**
- **PostgreSQL**
- **HTML, CSS, JavaScript**

---

## Project Structure

```

src/main/java/com/pagati/studentapp/
├── StudentAppApplication.java      # Application entry point
├── entity/Student.java             # Data model (POJO)
├── dao/StudentDao.java             # DAO interface
├── dao/StudentDaoImpl.java         # JDBC-based DAO implementation
├── manager/StudentManager.java     # Service interface
├── manager/StudentManagerImpl.java # Business logic layer
├── web/StudentRestController.java  # REST API controller
└── error/                          # Global exception handling

````

---

##  Setup & Installation

### 1. Prerequisites

Make sure the following are installed:

- Java 17
- PostgreSQL

---

### 2. Database Setup

Create a PostgreSQL database:

```sql
CREATE DATABASE studentdb;
````

---

### 3. Configure Database Credentials

Edit the following file:

```
src/main/resources/application.properties
```

Example configuration:

```properties
spring.datasource.username=postgres
spring.datasource.password=root
```

---

### 4. Run the Application

Start the application using Maven Wrapper:

```bash
./mvnw.cmd clean spring-boot:run
```

Application will run at:

```
http://localhost:8080
```

---

## Frontend Access

Open in your browser:

```
http://localhost:8080
```

---

## API Endpoints

| Method | Endpoint         | Description            |
| ------ | ---------------- | ---------------------- |
| POST   | `/students`      | Create a new student   |
| GET    | `/students`      | Retrieve all students  |
| GET    | `/students/{id}` | Retrieve student by ID |
| PUT    | `/students/{id}` | Update student details |
| DELETE | `/students/{id}` | Delete a student       |

---

## Architecture Overview

The application follows a layered architecture:

* **Controller Layer** → Handles HTTP requests
* **Service Layer (Manager)** → Contains business logic
* **DAO Layer** → Handles database operations using JDBC
* **Entity Layer** → Represents database models

---

##  Features

* Clean layered architecture
* Uses **JdbcTemplate** for database interaction
* RESTful API design
* Centralized exception handling
* Lightweight frontend integration

---

## Possible Improvements

* Add **Swagger/OpenAPI documentation**
* Migrate to **Spring Data JPA**
* Add **Docker support**
* Implement **authentication (JWT/Spring Security)**
* Improve UI/UX with modern frontend framework

---
