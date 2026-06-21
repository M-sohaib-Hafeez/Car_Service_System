# 🚗 Car Service Center Management System

A **JavaFX + MySQL** desktop application built as a group project for the **Data Structures** course at Dawood University of Engineering and Technology (DUET), Karachi.

---

## 👥 Team Members

| Name | Roll No | Contribution |
|------|---------|-------------|
| Muhammad Sohaib Hafeez | 24F-CS-085 | Customer module (frontend + backend), overall code review & bug fixes |
| Hammad-ur-Rehman | 24F-CS-091 | Mechanic module (frontend + backend) |
| Shayan Farrukh | 24F-CS-080 | Admin module (frontend + backend) |

---

## 📋 Project Overview

The Car Service Center Management System is a full-featured desktop application that manages the operations of an automotive service center. It supports three user roles — **Admin**, **Mechanic**, and **Customer** — each with their own dedicated dashboard and functionality.

---

## ✨ Features

### 🔐 Authentication
- Role-based login system (Admin, Mechanic, Customer)
- Sign-up with role selection (Customer or Mechanic applicant)
- Animated login screen with car entrance animation

### 👑 Admin Panel
- **Dashboard** — Live statistics: total customers, vehicles, appointments, revenue, mechanic availability, pending requests, pie chart for appointment status, revenue trend line chart
- **Services Management** — Add, view, and delete service types with category-based icons
- **Appointments** — View all appointments as styled cards with status indicators (Pending, In Progress, Completed, Cancelled)
- **Mechanics Management** — View all mechanics, delete mechanics (with safety check for active appointments), manage mechanic join requests (Approve / Reject)

### 🔧 Mechanic Panel
- **Overview** — Weekly jobs bar chart, total/active/completed job stats
- **My Jobs** — View assigned appointments, start and complete jobs in real time
- **Completed Jobs** — History of completed service records

### 👤 Customer Panel
- **Dashboard** — Welcome screen with current active appointment status, queue position, and service history
- **Book Appointment** — Select vehicle, service, date/time, and priority level
- **Add Vehicle** — Register new vehicles to the account
- **Loyalty Points** — Earn points with every service

---

## 🏗️ Project Structure

```
CarServiceCenter_DS/
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/carservicecenter_ds/
│       │       ├── DAO/                        # Database Access Objects
│       │       │   ├── AdminDAO.java
│       │       │   ├── CustomerDAO.java
│       │       │   ├── DataBaseConnection.java
│       │       │   ├── DataBaseInitializer.java
│       │       │   ├── MechanicDAO.java
│       │       │   ├── MechanicRequestDAO.java
│       │       │   ├── ServiceAppointmentDAO.java
│       │       │   └── UserDAO.java
│       │       ├── controllers/                # JavaFX Controllers
│       │       │   ├── LoginController.java
│       │       │   ├── SignUpController.java
│       │       │   ├── CustomerController.java
│       │       │   ├── CustomerBookingController.java
│       │       │   ├── AddVehicleController.java
│       │       │   ├── MechanicMainController.java
│       │       │   ├── MechanicJobsController.java
│       │       │   ├── MechanicOverviewController.java
│       │       │   ├── MechanicCompletedJobsController.java
│       │       │   ├── adminSideBarController.java
│       │       │   ├── adminDashboardController.java
│       │       │   ├── adminServiceController.java
│       │       │   ├── adminMechanicController.java
│       │       │   ├── adminAppointmentController.java
│       │       │   ├── addServiceFormController.java
│       │       │   └── mechanicRequestsController.java
│       │       ├── model/                      # Data Models
│       │       │   ├── User.java
│       │       │   ├── Customer.java
│       │       │   ├── Mechanic.java
│       │       │   ├── mechanicRequests.java
│       │       │   ├── Service.java
│       │       │   ├── ServiceAppointment.java
│       │       │   └── Vehicle.java
│       │       ├── util/
│       │       │   └── NotificationService.java
│       │       └── Main.java
│       └── resources/
│           ├── css/                            # Stylesheets
│           │   ├── styles.css
│           │   ├── adminDashboard.css
│           │   ├── AppointmentCards.css
│           │   └── mechanicRequestCard.css
│           ├── fxml/                           # UI Layout Files
│           ├── images/                         # Application Assets
│           ├── Dummy_data.sql                  # Sample data for testing
│           └── car_service_sch1.sql            # Database schema export
├── pom.xml
└── README.md
```

---

## 🛠️ Data Structures Used

This project was built for the **Data Structures** course and makes use of the following:

- **`PriorityQueue`** — Used for appointment queue management based on priority level (Emergency → High → Normal → Low)
- **`ArrayList` / `List`** — For storing and passing collections of appointments, services, mechanics, and vehicles
- **`ResultSet` (Database)** — Sequential traversal for database record processing

---

## 🗄️ Database Schema

The application uses **MySQL** with the following tables:

| Table | Description |
|-------|-------------|
| `users` | Login credentials and roles |
| `customers` | Customer profiles with loyalty points |
| `vehicles` | Vehicles registered by customers |
| `mechanics` | Approved mechanic profiles |
| `services` | Available service types and pricing |
| `appointments` | Service bookings with status tracking |
| `mechanic_requests` | Pending mechanic sign-up applications |

The database is **auto-initialized** on first launch via `DataBaseInitializer.java`.

---

## ⚙️ Tech Stack

| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 25 | Core language |
| JavaFX | 21.0.6 | GUI framework |
| Maven | 3.8.5 | Build & dependency management |
| MySQL | 8.x | Relational database |
| MySQL Connector/J | 8.0.33 | JDBC driver |
| JUnit Jupiter | 5.12.1 | Unit testing |

---

## 🚀 Getting Started

### Prerequisites

- Java 25 (JDK)
- Maven
- MySQL Server 8.x
- IntelliJ IDEA (recommended) or any Java IDE

### Database Setup

1. Start your MySQL server.
2. Create a database named `car_service_center`:
   ```sql
   CREATE DATABASE car_service_center;
   ```
3. The application will automatically create all required tables on first launch.
4. Optionally, load sample data:
   ```sql
   USE car_service_center;
   SOURCE src/main/resources/Dummy_data.sql;
   ```

### Configure Database Credentials

Open `src/main/java/com/example/carservicecenter_ds/DAO/DataBaseConnection.java` and update:

```java
String url      = "jdbc:mysql://localhost/car_service_center";
String username = "your_mysql_username";
String password = "your_mysql_password";
```

> ⚠️ **Important:** Do not commit real credentials to version control.

### Build & Run

```bash
# Clone the repository
git clone https://github.com/M-sohaib-Hafeez/Car_Service_System.git
cd Car_Service_System

# Run with Maven
mvn clean javafx:run
```

Or open the project in IntelliJ IDEA and run `Main.java` directly.

---

## 🔑 Test Credentials

Once sample data is loaded, use these credentials to log in:

| Role | Email | Password |
|------|-------|----------|
| Admin | admin@carservice.com | admin123 |
| Customer | ahmed.ali@email.com | pass123 |
| Customer | fatima.khan@email.com | pass123 |
| Mechanic | ali.mechanic@email.com | mech123 |
| Mechanic | hamza.mechanic@email.com | mech123 |

---

## 📸 Application Screens

- **Login** — Animated car entrance, role-based login
- **Admin Dashboard** — Stats cards, charts, tables, mechanic requests
- **Admin Services** — Service cards with SVG icons, add/delete flow
- **Admin Mechanics** — Mechanic cards, approve/reject applicants
- **Admin Appointments** — Status-colored appointment cards
- **Mechanic Dashboard** — Overview with bar chart, jobs list, completed history
- **Customer Dashboard** — Active service tracker, queue position, booking button, loyalty points
- **Booking Form** — Vehicle, service, date/time, priority, notes
- **Sign Up** — Mechanic overlay with specialization and experience fields

---

## 📌 Notes

- The application auto-initializes the database schema on startup — no manual SQL setup required beyond creating the database.
- Mechanic accounts require **admin approval** before they can log in.
- Deleting a mechanic or service is **blocked** if active appointments exist.
- Priority queue uses levels 1–4: **Emergency**, **High**, **Normal**, **Low**.

---

## 📄 License

This project was developed for academic purposes as part of the Data Structures course at DUET, Karachi. All rights reserved by the authors.