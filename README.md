# Task: Travel Destination Application

## Overview

This project is a fully-functional full-stack application developed using **Spring Boot 3+**, **Java 17**, **Hibernate**, **MySQL**, and **Angular 16+**. Developed as the technical selection task for the Associate Software Engineer position, the application implements the required travel domain architecture. 

The application enables regular users to securely browse curated global destinations, perform live reactive searches, view comprehensive geographic/monetary metadata, and manage a custom "WishList". Concurrently, it grants administrative access to an Admin Dashboard where recruiters or admins can live-fetch external recommendations from a third-party countries API, review them, and perform individual or transactional transactional bulk-saves into the local relational database.

---

## Features

- **User Management**: Secure system authentication mapping unique user routing to respective specialized dashboards based on assigned structural profiles (`ROLE_USER` / `ROLE_ADMIN`).
- **Admin Dashboard**: Admins can view all internally approved configurations, pull real-time live country data suggestions directly from the external REST Countries API, execute clean single inserts, run transactional batch bulk-inserts, or cleanly remove destinations from the local persistent storage layer.
- **Pagination for the Destinations List**: Regular users can effortlessly browse approved travel cards sliced cleanly into performant pages, ensuring rapid image and rendering response times.
- **Search bar**: Built with a debounced, reactive input stream allowing regular users to look up and filter active internal records by Country name dynamically.
- **Wishlist**: Standard users can bookmark specific cards with an interactive bookmark toggle, updating relational database map links instantly.

---

## Design

### Database Design

  The following diagram represents the Entity-Relationship Diagram (ERD) mapping the system schema entities (`User`, `Destination`, and `Wishlist` relational mappings).

![ERD Diagram](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/ERD.png)

### Project Architecture

The project follows a well-structured architectural pattern:

- **Model-View-Controller (MVC)**: The application isolates system concerns cleanly across an MVC design pattern layout:
    - **Model**: Represents the core data structures and underlying operational business logic (Entities, Repositories, DTO Contracts, Services).
    - **View**: Implements the user-facing responsive view state layout engines powered by Angular (HTML, CSS/SCSS, TypeScript components).
    - **Controller**: Orchestrates communication mapping, handling incoming requests and outputting standard JSON payloads (Spring Boot REST Controllers).

- **Client-Server Architecture**: The application separates execution boundaries cleanly:
    - The **client** (frontend application) acts as an independent detached system interacting over HTTP channels to mutate state records.
    - The **server** (backend instance) securely filters operations, updates database connections, and packs predictable JSON responses.

- **REST API**: The application exposes structured RESTful endpoints facilitating strict data exchange contracts between frontend services and backend architectures.

---

## Technologies Used

- **Backend**: Spring Boot 3+, Hibernate, Spring Data JPA, Spring Security, MySQL, RESTful APIs
- **Frontend**: Angular 16+, HTML5, CSS/SCSS, TypeScript, Bootstrap
- **Database**: MySQL
- **Third-Party Travel API**: [REST Countries API](https://restcountries.com/)
- **Tools & Libraries**:
   - Spring Data JPA for data persistence interfaces
   - RxJS Observables for debounced user search execution matrices
   - Bootstrap for fluid grid layouts and responsive cards

---

## Setup and Installation

### Prerequisites

- Java 17+
- Maven 3.6+
- MySQL 8.x+
- Angular 16+
- IDE: IntelliJ IDEA / VSCode / MySQL Workbench

### Steps

#### For Spring Boot (Backend)
1. Clone the repository:

```bash
    git clone https://github.com/markelmasry/Travel-Destinations-Management-Web-Application.git
    cd travel-app
   ```

2. Configure the MySQL database:

   Initialize your database instance inside your local MySQL shell:

```sql
    CREATE DATABASE travel_db;
   ```

   Update the `src/main/resources/application.properties` file configuration to match your local runtime environments:

```properties
    spring.datasource.url=jdbc:mysql://localhost:3306/travel_db
    spring.datasource.username=your_mysql_username
    spring.datasource.password=your_mysql_password
    spring.jpa.hibernate.ddl-auto=update
   ```
### Environment Variables

Before running the Spring Boot application, configure the following environment variables in your IDE or operating system:

| Variable | Description |
|-----------|-------------|
| JWT_SECRET | Secret key used for JWT authentication |
| REST_COUNTRIES_API_KEY | API key used for the REST Countries service |

These variables are referenced in `application.properties`:

```properties
application.security.jwt.secret-key=${JWT_SECRET}
restcountries.api.key=${REST_COUNTRIES_API_KEY}
```

#### IntelliJ IDEA Setup

1. Open **Run → Edit Configurations**.
2. Select your Spring Boot application.
3. Add the following environment variables:

```text
JWT_SECRET=your_jwt_secret
REST_COUNTRIES_API_KEY=your_api_key
```
4. Save the configuration and run the application.

> **Note:** Sensitive values such as JWT secrets and API keys are not included in the repository and must be configured locally before running the application.


3. Install dependencies and build target files:

```bash
    mvn clean install
   ```

4. Boot up the Spring application backend:

```bash
    mvn spring-boot:run
   ```
   *The server container will spin up successfully on `http://localhost:8080`.*

#### For Angular (Frontend)
Install dependencies and launch the live development interface:

```bash
    cd ../travel-app-frontend
    npm install
    ng serve --open
   ```
   *The client-side compilation layers will automatically initialize and open on `http://localhost:4200`.*

---

# API Endpoints

<details>
<summary>Auth Controller</summary>

### 1. `POST /api/auth/login`
- **Description**: Validates user credentials, roles, and provides an active stateless JWT authorization token.

</details>

<details>
<summary>Destination Controller</summary>

### 1. `GET /api/destinations/search`
- **Description**: Returns server-side paginated local database records matching optional country queries.

### 2. `GET /api/destinations/suggestions`
- **Description**: Reaches out to the external third-party travel API to fetch recommended data profiles based on a country keyword.

### 3. `POST /api/destinations`
- **Description**: Persists a single verified travel destination record configuration to the relational database.

### 4. `POST /api/destinations/bulk`
- **Description**: Executes a transactional batch insert to persist multiple suggestions concurrently (Bonus Feature).

### 5. `DELETE /api/destinations/{id}`
- **Description**: Permanently removes an approved destination configuration profile out of the database via its numerical unique ID.

</details>

<details>
<summary>Wishlist Controller</summary>

### 1. `GET /api/wishlist/user/{username}`
- **Description**: Retrieves all saved wishlist items currently bookmarked by a specific user profile.

### 2. `POST /api/wishlist`
- **Description**: Saves a relational tracking bookmark linking a user profile directly to an approved destination ID.

### 3. `DELETE /api/wishlist/{visitId}/user/{username}`
- **Description**: Removes an existing bookmarked tracking instance safely out of a user's personal tracking list.

</details>

---

# Preview

- **Login Layout**
![Login Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/Login.png)
- **Admin Dashboard Layout**
![Admin Dashboard Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/Admin.png)
![Admin Dashboard Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/FetchApi.png)
![Admin Dashboard Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/AddDestinations.png)
- **User Dashboard & Paginated Grid Views**
![User Dashboard Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/User.png)
![User Dashboard Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/SaveToList.png)
![User Dashboard Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/pages.png)
- **Reactive Debounced Search Execution**
![Search Execution Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/Searching.png)
- **Wishlist Toggle Interactions**
![wishlist Layout](https://github.com/markelmasry/Travel-Destinations-Management-Web-Application/blob/main/Images/WantToVisitList.png)

---

#### For any technical inquiries, architectural discussions, or review updates, let's connect on [LinkedIn](https://www.linkedin.com/in/mark-akram-25201a218/).
