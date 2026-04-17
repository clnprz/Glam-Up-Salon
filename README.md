# Salon Appointment Management System

A Java-based desktop application for managing salon appointments, customers, stylists, payments, and user accounts.

---

## Description

The Salon Appointment Management System is designed to replace manual record-keeping in a salon environment. It provides two user roles — **Admin** and **Staff** — each with their own dashboard and set of features. All data is persisted in a **Microsoft SQL Server database (SalonDB)**, and every user action is recorded through a built-in **audit logging system**.

---

## Features

- **Login / Authentication** — Secure role-based login for Admin and Staff
- **Customer Registration** — Add and manage customer profiles
- **Appointment Scheduling** — Book, update, and track salon appointments
- **Payment Processing** — Record and manage customer payments
- **Stylist Management** — Manage stylist information and assignments
- **User Management** *(Admin only)* — Create and manage system user accounts
- **Audit Logging** — Automatic logging of all user actions for accountability

---

## Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application language |
| Java Swing (NetBeans GUI Builder) | Desktop user interface |
| Microsoft SQL Server | Database |
| JDBC | Database connectivity |

---

## Installation & Setup Guide

### Prerequisites
- [Java JDK 8+](https://www.oracle.com/java/technologies/downloads/)
- [NetBeans IDE](https://netbeans.apache.org/)
- [Microsoft SQL Server](https://www.microsoft.com/en-us/sql-server/sql-server-downloads) or SQL Server Express
- [SQL Server Management Studio (SSMS)](https://learn.microsoft.com/en-us/sql/ssms/download-sql-server-management-studio-ssms)

### Steps

**1. Clone the repository**
```bash
git clone https://github.com/clnprz/Glam-Up-Salon
```

**2. Open in NetBeans**
- Launch NetBeans IDE
- Go to `File` → `Open Project`
- Navigate to the cloned folder and open it

**3. Extract source files**
- Unzip `src.zip` inside the project folder if not already extracted

**4. Set up the database**
- Open **SQL Server Management Studio (SSMS)**
- Connect to your local SQL Server instance
- Open `SalonDB.sql` and execute the script to create and populate the database

**5. Configure the database connection**
- Open `DBConnection.java` in the project source files
- Update the connection details to match your local setup:
```java
String url = "jdbc:sqlserver://localhost;databaseName=SalonDB";
String username = "your_username";
String password = "your_password";
```

**6. Run the project**
- In NetBeans, right-click the project → `Clean and Build`
- Then click `Run Project` (or press `F6`)

---

## Screenshots

## Screenshots

### Login
![Login](assets/login.png)

### Admin Dashboard
![Admin Dashboard](assets/admin-dashboard.png)

### Appointments (Admin)
![Appointments Admin](assets/appointments-admin.png)

### Payments (Admin)
![Payments Admin](assets/payments-admin.png)

### Customers (Admin)
![Customers Admin](assets/customers-admin.png)

### Book New Appointment
![Book Appointment](assets/book-appointment.png)

### Update Appointment
![Update Appointment](assets/update-appointment.png)

### Register Customer
![Register Customer](assets/register-customer.png)

### 🖥️ Staff Dashboard
![Staff Dashboard](assets/staff-dashboard.png)

### Appointments (Staff)
![Appointments Staff](assets/appointments-staff.png)

### Payments (Staff)
![Payments Staff](assets/payments-staff.png)

### Customers (Staff)
![Customers Staff](assets/customers-staff.png)

---

## Contributors

| Name | GitHub |
|---|---|
| Raven Lou Bituin | [@vennbtwn](https://github.com/vennbtwn) |
| Eujyll Vyndrieisne Abella | [@euchill](https://github.com/euchill) |
| Jasmine Tubilla | [@jsmntbll_](https://github.com/jsmntbll_) |
| Clysce Ena Perez | [@clnprz](https://github.com/clnprz) |

---

## License

This project was developed as an academic requirement. All rights reserved by the contributors.
