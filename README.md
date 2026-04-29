# Student Information Portal

A Spring Boot application that performs CRUD operations on a Student table using JDBC and PostgreSQL.

## Tech Stack

- Java 17
- Spring Boot 3.2.5
- Spring JDBC (JdbcTemplate)
- PostgreSQL
- HTML / CSS / JavaScript (Frontend)

## Project Structure

```
src/main/java/com/pagati/studentapp/
├── StudentAppApplication.java      # Main class
├── entity/Student.java             # Student model
├── dao/StudentDao.java             # DAO interface
├── dao/StudentDaoImpl.java         # JDBC implementation
├── manager/StudentManager.java     # Service interface
├── manager/StudentManagerImpl.java # Business logic
├── web/StudentRestController.java  # REST controller
└── error/                          # Exception handling
```

## How to Run

### 1. Install Prerequisites

- Java 17
- PostgreSQL

### 2. Create the Database

Open a terminal and run:

```sql
CREATE DATABASE studentdb;
```

### 3. Update Database Credentials

Edit `src/main/resources/application.properties` if your PostgreSQL username or password is different:

```properties
spring.datasource.username=postgres
spring.datasource.password=root
```

### 4. Run the Application

```bash
./mvnw.cmd clean spring-boot:run
```

The app will start on **http://localhost:8080**

### 5. Open the Frontend

Go to [http://localhost:8080](http://localhost:8080) in your browser.

## API Endpoints

| Method | Endpoint         | Description          |
|--------|------------------|----------------------|
| POST   | /students        | Add a new student    |
| GET    | /students        | Get all students     |
| GET    | /students/{id}   | Get student by ID    |
| PUT    | /students/{id}   | Update a student     |
| DELETE | /students/{id}   | Delete a student     |
