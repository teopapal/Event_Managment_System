# Event Management System

## Description
This project is an **Event Management System** that enables efficient management of:
- Customers
- Events
- Ticketing
- Reservations
- Cancellations

The system connects to a MySQL database and allows administrators to manage events, tickets, and customer interactions seamlessly. It also provides functionality for automating tasks such as ticket reservations, event cancellations, and automated updates of event capacity and ticket availability.

This project is structured into two main modules:
1. **Data Module**: Handles all backend database interactions.
2. **GUI Module**: Provides a graphical user interface for users to interact with the system.

---

## Features
- **Customer Management**:
    - Register customers with unique emails.
    - Store and validate customer data, including names and credit card details.

- **Event Management**:
    - Create events with details such as name, date, time, type, and capacity.
    - Automatically generate tickets for events, categorized into `VIP`, `Regular`, or `Student`, with associated pricing.

- **Ticket Management**:
    - Display available tickets based on event and seat type.
    - Reserve and release tickets as part of reservations or cancellations.

- **Reservation Management**:
    - Allow users to reserve tickets for events.
    - Dynamically adjust event capacity as tickets are sold.
    - Automate refunds when reservations are canceled.

- **Event Cancellation**:
    - Bulk cancellation of all reservations and tickets for a specific event.
    - Automated refunds to customers for canceled events.

---

## Project Structure
The project is organized into the following folders:

### 1. **Data Module**
The **data** module contains the backend logic implemented in the `DBManager` class. This module handles all database interactions, such as:
- Initializing the database and creating necessary tables (`events`, `customers`, `tickets`, and `reservations`).
- CRUD operations on events, customers, tickets, and reservations.
- Complex operations like event cancellation that include database updates and customer refunds.

#### Key Functions in `DBManager`:
- **Database Initialization**:
    - Automatically creates tables and resets them as needed (`initialize` method).
- **Customer Registration**:
    - Checks for duplicate emails before adding a new customer.
- **Event Creation**:
    - Creates events and auto-generates categorized tickets.
- **Ticket Operations**:
    - Fetches available tickets for specified seat types and events.
    - Updates ticket states based on reservations.
- **Adding Reservations**:
    - Manages reservation transactions, ensuring capacity and availability checks.
    - Reserves specific tickets for customers and updates event availability.
- **Event Cancellation**:
    - Deletes an event and all associated tickets and reservations.
    - Issues refunds automatically.

### 2. **GUI Module**
The **gui** module provides a user-friendly interface to interact with the Event Management System. It includes:
- **Enums**: Definitions for classification, such as `Seat_type`.
- **Records**: Lightweight structures like `Customer`, `Event`, `Reservation`, and `Ticket` for data organization and transfer.

The GUI allows:
- Admins to manage events and tickets.
- Customers to book or cancel tickets dynamically.

---

## Database Schema
The system connects to a MySQL database with the following schema:

1. **Events Table**:
    - Contains event details such as name, type, date/time, capacity, and availability.

2. **Customers Table**:
    - Stores customer details like names, email, and credit card information.

3. **Tickets Table**:
    - Includes ticket data (event association, price, seat type, reservation status).

4. **Reservations Table**:
    - Links customers and events, storing reservation and payment details.

#### Relationships:
- Tickets are linked to Events via foreign keys.
- Reservations are associated with both Customers and Events.

---

## Technologies Used
- **Java**: For backend logic and functionality.
- **MySQL**: To store and manage project data.
- **JDBC (Java Database Connectivity)**: To connect to and query the database.
- **IntelliJ IDEA**: As the integrated development environment.
- **Maven**: For dependency management and project organization.

---

## Prerequisites
To run this application, ensure the following:
- **MySQL** is installed and running.
- Modify the database credentials in the `DBManager` class (if necessary):
  ```java
  String url = "jdbc:mysql://localhost";
  String databaseName = "test";
  int port = 3306;
  String username = "root";  // Replace with your DB username
  String password = "";      // Replace with your DB password
  ```

---

## Installation

1. Clone the project:
   ```bash
   git clone https://github.com/teopapal/HY-360_project.git
   ```
2. Open the project in **IntelliJ IDEA** as a Maven project.
3. Configure the database:
    - Ensure your MySQL server is running.
    - Execute the initialization logic in `DBManager.initialize()` to create tables.
4. Run the application:
    - Use the GUI or invoke `DBManager` methods programmatically to test features.

---

## How to Use
Below are example usages of the core functionalities:

### Registering a Customer
Register a customer using the `registerCustomer()` method:
```java
Customer customer = new Customer("John", "Doe", "john.doe@example.com", "1234567890123456");
DBManager.registerCustomer(customer);
```

### Creating an Event
Add a new event using the `createEvent()` method:
```java
Event event = new Event("Music Concert", EventType.CONCERT, Date.valueOf("2023-12-31"), Time.valueOf("18:00:00"), 200);
DBManager.createEvent(event);
```

### Reserving Tickets
Reserve tickets with the `addReservation()` method:
```java
Reservation reservation = new Reservation(1, "Music Concert", Seat_type.VIP, 2);
DBManager.addReservation(reservation);
```

### Canceling an Event
Cancel an event and issue refunds:
```java
DBManager.cancelEvent("Music Concert");
```

---

### Authors
Developed by **John Protopsaltis**
**Theodoros Papaliagkas**
and **Stefanos Avramakis**.

---