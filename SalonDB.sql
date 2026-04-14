CREATE DATABASE SalonDB;
GO
 
USE SalonDB;
GO
 
-- ============================================================
--  TABLE: Users
--  Used by: LoginForm, AdminUsers, SessionManager, AuditLogger
-- ============================================================
CREATE TABLE Users (
    UserID      INT           IDENTITY(1,1) PRIMARY KEY,
    Username    NVARCHAR(100) NOT NULL UNIQUE,
    PasswordHash NVARCHAR(64) NOT NULL,         -- MD5 hex string (32 chars), stored as NVARCHAR for safety
    Role        NVARCHAR(20)  NOT NULL           -- 'Admin' | 'Staff'
                    CHECK (Role IN ('Admin', 'Staff')),
    IsActive    BIT           NOT NULL DEFAULT 1,
    CreatedAt   DATETIME      NOT NULL DEFAULT GETDATE()
);
GO
 
-- ============================================================
--  TABLE: Staff
--  Used by: AdminStylists, StaffStylists, Appointments joins
--  Note: Staff.UserID links a staff member to their login account
-- ============================================================
CREATE TABLE Staff (
    StaffID        INT           IDENTITY(1,1) PRIMARY KEY,
    UserID         INT           NULL REFERENCES Users(UserID),
    FirstName      NVARCHAR(100) NOT NULL,
    LastName       NVARCHAR(100) NOT NULL,
    Specialization NVARCHAR(200) NULL,
    Phone          NVARCHAR(20)  NULL,
    IsActive       BIT           NOT NULL DEFAULT 1
);
GO
 
-- ============================================================
--  TABLE: Customers
--  Used by: AdminCustomers, StaffCustomers, Appointments joins
-- ============================================================
CREATE TABLE Customers (
    CustomerID     INT           IDENTITY(1,1) PRIMARY KEY,
    FirstName      NVARCHAR(100) NOT NULL,
    LastName       NVARCHAR(100) NOT NULL,
    Phone          NVARCHAR(20)  NOT NULL,
    Email          NVARCHAR(200) NULL,
    DateRegistered DATETIME      NOT NULL DEFAULT GETDATE()
);
GO
 
-- ============================================================
--  TABLE: Services
--  Used by: AdminServices, StaffServices, AppointmentServices join
-- ============================================================
CREATE TABLE Services (
    ServiceID       INT            IDENTITY(1,1) PRIMARY KEY,
    ServiceName     NVARCHAR(200)  NOT NULL,
    Category        NVARCHAR(100)  NOT NULL,
    Price           DECIMAL(10, 2) NOT NULL,
    DurationMinutes INT            NOT NULL,
    IsActive        BIT            NOT NULL DEFAULT 1
);
GO
 
-- ============================================================
--  TABLE: Appointments
--  Used by: AdminAppointments, StaffAppointments, Dashboards
-- ============================================================
CREATE TABLE Appointments (
    AppointmentID   INT          IDENTITY(1,1) PRIMARY KEY,
    CustomerID      INT          NOT NULL REFERENCES Customers(CustomerID),
    StaffID         INT          NOT NULL REFERENCES Staff(StaffID),
    AppointmentDate DATETIME     NOT NULL,
    Status          NVARCHAR(20) NOT NULL DEFAULT 'Pending'
                        CHECK (Status IN ('Pending', 'Confirmed', 'In Progress', 'Done', 'Cancelled'))
);
GO
 
-- ============================================================
--  TABLE: AppointmentServices  (junction / line-items)
--  Used by: All appointment and payment queries
--  PriceAtTime captures the service price at booking time
-- ============================================================
CREATE TABLE AppointmentServices (
    AppointmentServiceID INT            IDENTITY(1,1) PRIMARY KEY,
    AppointmentID        INT            NOT NULL REFERENCES Appointments(AppointmentID) ON DELETE CASCADE,
    ServiceID            INT            NOT NULL REFERENCES Services(ServiceID),
    PriceAtTime          DECIMAL(10, 2) NOT NULL
);
GO
 
-- ============================================================
--  TABLE: Payments
--  Used by: AdminPayments, StaffPayments, AdminReports
-- ============================================================
CREATE TABLE Payments (
    PaymentID     INT            IDENTITY(1,1) PRIMARY KEY,
    AppointmentID INT            NOT NULL REFERENCES Appointments(AppointmentID),
    PaymentMethod NVARCHAR(50)   NOT NULL,       -- e.g. 'Cash', 'GCash', 'Card'
    AmountPaid    DECIMAL(10, 2) NOT NULL,
    TotalAmount   DECIMAL(10, 2) NOT NULL,
    Status        NVARCHAR(20)   NOT NULL DEFAULT 'Paid'
                      CHECK (Status IN ('Paid', 'Partial', 'Unpaid')),
    PaymentDate   DATETIME       NOT NULL DEFAULT GETDATE()
);
GO
 
-- ============================================================
--  TABLE: AuditLog
--  Used by: AuditLogger, AdminReports (Activity Log)
-- ============================================================
CREATE TABLE AuditLog (
    LogID   INT            IDENTITY(1,1) PRIMARY KEY,
    UserID  INT            NOT NULL REFERENCES Users(UserID),
    Action  NVARCHAR(100)  NOT NULL,    -- e.g. 'LOGIN', 'LOGOUT', 'CREATE', 'UPDATE'
    Details NVARCHAR(MAX)  NULL,
    LogTime DATETIME       NOT NULL DEFAULT GETDATE()
);
GO
 
-- ============================================================
--  INDEXES  (for frequent filter / join columns)
-- ============================================================
CREATE INDEX IX_Appointments_Date       ON Appointments(AppointmentDate);
CREATE INDEX IX_Appointments_Status     ON Appointments(Status);
CREATE INDEX IX_Appointments_CustomerID ON Appointments(CustomerID);
CREATE INDEX IX_Appointments_StaffID    ON Appointments(StaffID);
CREATE INDEX IX_AppointmentServices_Apt ON AppointmentServices(AppointmentID);
CREATE INDEX IX_Payments_AppointmentID  ON Payments(AppointmentID);
CREATE INDEX IX_Payments_Status         ON Payments(Status);
CREATE INDEX IX_AuditLog_UserID         ON AuditLog(UserID);
CREATE INDEX IX_AuditLog_LogTime        ON AuditLog(LogTime);
CREATE INDEX IX_Customers_Name          ON Customers(LastName, FirstName);
CREATE INDEX IX_Staff_UserID            ON Staff(UserID);
GO