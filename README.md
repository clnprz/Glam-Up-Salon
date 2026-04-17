# 💅 Salon Appointment Management System

A Java-based desktop application for managing salon appointments, customers, stylists, payments, and user accounts.

---

## 📖 Description

The Salon Appointment Management System is designed to replace manual record-keeping in a salon environment. It provides two user roles — **Admin** and **Staff** — each with their own dashboard and set of features. All data is persisted in a **Microsoft SQL Server database (SalonDB)**, and every user action is recorded through a built-in **audit logging system**.

---

## ✨ Features

- 🔐 **Login / Authentication** — Secure role-based login for Admin and Staff
- 👤 **Customer Registration** — Add and manage customer profiles
- 📅 **Appointment Scheduling** — Book, update, and track salon appointments
- 💳 **Payment Processing** — Record and manage customer payments
- ✂️ **Stylist Management** — Manage stylist information and assignments
- 🛠️ **User Management** *(Admin only)* — Create and manage system user accounts
- 📋 **Audit Logging** — Automatic logging of all user actions for accountability

---

## 🛠️ Technologies Used

| Technology | Purpose |
|---|---|
| Java | Core application language |
| Java Swing (NetBeans GUI Builder) | Desktop user interface |
| Microsoft SQL Server | Database |
| JDBC | Database connectivity |

---

## ⚙️ Installation & Setup Guide

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

## 📸 Screenshots

> *Place screenshots of the running application inside the `assets/` folder and reference them below.*

### Login Screen
![Login Screen](assets/login.png)

### Admin Dashboard
![Admin Dashboard](assets/admin-dashboard.png)

### Appointment Scheduling
![Appointments](assets/appointments.png)

### Payment Processing
![Payments](assets/payments.png)

---

## 👩‍💻 Contributors

| Name | GitHub |
|---|---|
| Raven Lou Bituin | — |
| Eujyll Vyndrieisne Abella | — |
| Clysce Ena Perez | — |
| Jasmine Tubilla | — |

---

## 📄 License

This project was developed as an academic requirement. All rights reserved by the contributors.