# 🕯️ Candle Factory Management System

An internal Enterprise Resource Planning (ERP) system developed for candle manufacturing businesses. This full-stack application digitizes the entire workflow: from production logging and inventory management to sales tracking, customer credit management, and employee leave scheduling.

The system is built as a **monolithic Spring Boot application** with an embedded **React frontend**, ensuring a single deployment unit.

---

## ✨ Key Features

Based on the implemented data models and API endpoints, the system offers the following modules:

### 📦 Product & Inventory Management
* **Product Catalog:** Define candle types using unique codes (e.g., "No1", "L40") and material types (Brown, White, Pure).
* **Pricing Strategy:** Support for pricing by **Weight** (kg) or by **Piece** (unit).
* **Live Storage:** Real-time tracking of finished goods quantity via the `Storage` module.
* **Soft Deletion:** Products can be marked as deleted without losing historical data.

### 🏭 Production Line
* **Daily Logs:** Record production output (`Production` entity) by date and product type.
* **Reporting:** Filter production history by date ranges or specific product types to analyze factory output.

### 💰 Sales & Customer Management (CRM)
* **Customer Profiles:** Manage customer details and contact information.
* **Debt Tracking:** Automatically track customer balances (`debt` field) based on sales and payments.
* **Sales History:** Detailed logs of every transaction including date, product, quantity, and cost.

### 👥 Human Resources (HR)
* **Worker Registry:** Manage employee details and contact info.
* **Leave Management:** Track employee leaves (`Leave` entity) with start/end dates.
* **Calculations:** Automatic calculation of total days used per worker.

---

## 🛠️ Technology Stack

### Backend
* **Java 17**
* **Spring Boot 3.x** (Web, Data JPA, Validation)
* **Database:** MySQL 8.0
* **ORM:** Hibernate
* **Tools:** Lombok, Maven

### Frontend
* **Framework:** React.js (located in `/frontend`)
* **Build Integration:** `frontend-maven-plugin` (Seamless Java + JS build)
* **Node.js:** v22.12.0 (managed locally by Maven)

---

## 📋 Prerequisites

To run the project locally, ensure you have:

1.  **Java JDK 17** installed.
2.  **Docker Desktop** (running).
3.  **Git**.

> **Note:** Node.js and NPM are **not** required globally. The build process installs a local version automatically.

---

## 🚀 Installation & Setup

### 1. Clone the Repository
```bash
git clone [https://github.com/nkaraisk/candle-factory.git](https://github.com/nkaraisk/candle-factory.git)
cd candle-factory
```

### 2. Start the Database (Docker)
The project includes a docker-compose.yml to spin up the MySQL database.
```bash
docker-compose up -d
```

* **Port**: 3306 (Default)
* **Container Name**: candle-factory-db

### 3. Build & Run
Use the Maven Wrapper to build the React frontend and start the Spring Boot server in one go:
* **Windows**:
```DOS
  ./mvnw spring-boot:run
```

* **Linux/macOS**:
```bash
chmod +x mvnw
./mvnw spring-boot:run
```
The first run may take a few minutes to download Node.js and NPM dependencies.

---
## 🌐 Access the Application
* **Dashboard**: http://localhost:8080
* **API Documentation**: http://localhost:8080/api (if Swagger/OpenAPI is enabled)
---
## 🗄️ Database Credentials
The application connects to the Docker container using these settings (from application.properties):

* **Database**: candle_factory
* **Username**: candle_user
* **Password**: i-Ker!
* **Root Pass**: root
* **Port**: 3306
---
## 👨‍💻 Development API Endpoints
Here are the main resources exposed by the backend:
* `/product`: Manage candle types (Add, Edit, Soft/Hard Delete).
* `/storage`: Manage inventory quantities.
* `/production`: Log daily output.
* `/sale`: Record transactions and retrieve history by Customer/Date.
* `/customer`: Manage clients and view debts.
* `/worker` & `/leave`: Manage personnel and leave requests.
  
---

* Author: Nick Karaiskos