# Accounts Service

A Spring Boot application for managing accounts.

## Technology Stack

- **Spring Boot**: 3.3.5
- **Java**: 21
- **Build Tool**: Gradle 8.10
- **Database**: H2 (in-memory)

## Dependencies

- Spring Web
- Spring Data JPA
- Spring Boot Actuator
- Spring Boot DevTools
- Lombok
- H2 Database

## Setup

1. Make sure you have Java 21 installed
2. The Gradle wrapper will download Gradle automatically on first run
3. Build the project:
   ```bash
   ./gradlew build
   ```

## Running the Application

```bash
./gradlew bootRun
```

The application will start on port 8080.

## Endpoints

- **Hello World**: `GET http://localhost:8080/api/hello`
- **H2 Console**: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:accountsdb`
  - Username: `sa`
  - Password: (leave empty)
- **Actuator Health**: `http://localhost:8080/actuator/health`
- **Actuator Info**: `http://localhost:8080/actuator/info`

## Project Structure

```
src/
  main/
    java/
      com/deeseek/accounts/
        AccountsApplication.java        # Main Spring Boot application
        controller/
          HelloWorldController.java     # Hello World REST controller
    resources/
      application.properties            # Application configuration
```

## Group and Package

- **Group**: `com.deeseek`
- **Package**: `com.deeseek.accounts`

