package com.DBProject.data;

import com.DBProject.gui.records.Client;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;



public class DBManager {
    private static Connection con;

    public static void initialize() {
        String createEventsTable = "CREATE TABLE IF NOT EXISTS events (" +
                "event_id INT AUTO_INCREMENT PRIMARY KEY," +
                "name VARCHAR(255) NOT NULL," +
                "event_date DATE NOT NULL," +
                "event_time TIME NOT NULL," +
                "event_type ENUM('Concert', 'Theater', 'Other') NOT NULL," +
                "capacity INT NOT NULL" +
                ");";

        String createCustomersTable =
                "CREATE TABLE IF NOT EXISTS Customers (" +
                        "customer_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "firstName VARCHAR(255) NOT NULL," +
                        "lastName VARCHAR(255) NOT NULL," +
                        "email VARCHAR(255) UNIQUE NOT NULL," +
                        "credit_card_details VARCHAR(16) NOT NULL" +
                        ");";

        String createTicketsTable =
                "CREATE TABLE IF NOT EXISTS Tickets (" +
                        "ticket_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "event_id INT NOT NULL," +
                        "seat_type ENUM('VIP', 'General Admission', 'Other') NOT NULL," +
                        "price DECIMAL(10, 2) NOT NULL," +
                        "availability INT NOT NULL," +
                        "FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE CASCADE" +
                        ");";

        String createReservationsTable =
                "CREATE TABLE IF NOT EXISTS Reservations (" +
                        "reservation_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "customer_id INT NOT NULL," +
                        "event_id INT NOT NULL," +
                        "ticket_id INT NOT NULL," +
                        "number_of_tickets INT NOT NULL," +
                        "reservation_date DATE NOT NULL DEFAULT CURRENT_DATE," +
                        "payment_amount DECIMAL(10, 2) NOT NULL," +
                        "FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE," +
                        "FOREIGN KEY (event_id) REFERENCES Events(event_id) ON DELETE CASCADE," +
                        "FOREIGN KEY (ticket_id) REFERENCES Tickets(ticket_id) ON DELETE CASCADE" +  // Foreign key to Tickets seat_type
                        ");";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost";
            String databaseName = "test";
            int port = 3306;
            String username = "root";
            String password = "";
            con = DriverManager.getConnection(
                    url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
            Statement statement = con.createStatement();

            // Execute database and table creation queries
            statement.execute(createEventsTable);
            statement.execute(createCustomersTable);
            statement.execute(createTicketsTable);
            statement.execute(createReservationsTable);

            System.out.println("Database and tables initialized successfully.");
        } catch (Exception e) {
            System.out.println("Failed to initialize the database." + e);
        }
    }

    public static boolean registerCustomer(Client client) {
        if (client.first_name().isEmpty() || client.last_name().isEmpty()  || client.email().isEmpty() || client.credit_card_info().isEmpty()) {
            return false; // Validation check for empty fields
        }

        // SQL query to check if the email already exists in the database
        String checkEmailQuery = "SELECT COUNT(*) FROM Customers WHERE email = ?";

        try (PreparedStatement checkStmt = con.prepareStatement(checkEmailQuery)) {
            // Set the email parameter
            checkStmt.setString(1, client.email());

            // Execute the query to check if the email already exists
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Email already exists!"); // Inform the user about the duplicate email
                return false; // Return false to indicate failure (duplicate email)
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Return false if there was an error executing the check query
        }


        // Proceed with the insertion since the email is unique
        String insertQuery = "INSERT INTO Customers (firstName, lastName, email, credit_card_details) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertCustomer = con.prepareStatement(insertQuery)) {
            // Set the customer details in the prepared statement
            insertCustomer.setString(1, client.first_name());
            insertCustomer.setString(2, client.last_name());
            insertCustomer.setString(3, client.email());
            insertCustomer.setString(4, client.credit_card_info());

            // Execute the insertion query
            insertCustomer.executeUpdate();
            System.out.println("Customer registered successfully!"); // Inform the user about successful registration
            return true; // Return true to indicate success
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return false; // Return false if there was an error during the insert
    }

    // Method to add a new event
    public static boolean createEvent(String name, String eventDate, String eventTime, String eventType, int capacity) {
        if (name.isEmpty() || eventType.isEmpty() || eventDate.isEmpty() || eventTime.isEmpty() || capacity <= 0) {
            return false; // Validation check for empty fields or invalid capacity
        }

        // SQL query to insert a new event
        String insertQuery = "INSERT INTO Events (name, event_type, event_date, event_time, capacity) VALUES (?, ?, ?, ?, ?)";

        try (PreparedStatement insertEvent = con.prepareStatement(insertQuery)) {
            // Set parameters for the SQL query
            insertEvent.setString(1, name);
            insertEvent.setString(2, eventType);
            insertEvent.setString(3, eventDate);
            insertEvent.setString(4, eventTime);
            insertEvent.setInt(5, capacity);

            // Execute the insert query
            insertEvent.executeUpdate();
            System.out.println("Event created successfully!"); // Inform the user about successful event creation
            return true; // Return true to indicate success
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false; // Return false if there was an error during the insert
    }

    // Method to add a new ticket
    public static boolean addTickets(int event_id, String seat_type, Double price, int availability) {
        if (seat_type.isEmpty()) {
            return false; // Validation check for empty fields or invalid capacity
        }

        // SQL query to insert a new event
        String insertQuery = "INSERT INTO Tickets (event_id, seat_type, price, availability) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertEvent = con.prepareStatement(insertQuery)) {
            // Set parameters for the SQL query
            insertEvent.setInt(1, event_id);
            insertEvent.setString(2, seat_type);
            insertEvent.setDouble(3, price);
            insertEvent.setInt(4, availability);

            // Execute the insert query
            insertEvent.executeUpdate();
            System.out.println("Ticket created successfully!"); // Inform the user about successful ticket creation
            return true; // Return true to indicate success
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return false; // Return false if there was an error during the insert
    }

    // Method to add a new reservation
    public static boolean addReservation(String seat_type, int customer_id, int event_id, int number_of_tickets) {
        // SQL query to insert a new event
        String checkAvailabilityQuery = "SELECT ticket_id,availability,price FROM Tickets WHERE event_id = ? AND seat_type = ?";
        String updateAvailabilityQuery = "UPDATE Tickets SET availability = availability - ? WHERE event_id = ? AND seat_type = ?";
        String insertReservationQuery = "INSERT INTO Reservations (customer_id, event_id, number_of_tickets, payment_amount,ticket_id) VALUES (?, ?, ?, ?,?)";

        try {
            // Check ticket availability and get the ticket price
            int ticketId = 0;
            int currentAvailability = 0;
            double ticketPrice = 0.0;

            // Check ticket availability
            try (PreparedStatement checkStmt = con.prepareStatement(checkAvailabilityQuery)) {
                checkStmt.setInt(1, event_id);
                checkStmt.setString(2, seat_type);

                ResultSet rs = checkStmt.executeQuery();
                if (rs.next()) {
                    ticketId = rs.getInt("ticket_id");
                    currentAvailability = rs.getInt("availability");
                    ticketPrice = rs.getDouble("price");
                    if (currentAvailability < number_of_tickets) {
                        System.out.println("Not enough tickets available.");
                        return false; // Not enough tickets
                    }
                } else {
                    System.out.println("Ticket type not found.");
                    return false; // Ticket type does not exist
                }
            }


            // Insert reservation
            try (PreparedStatement insertStmt = con.prepareStatement(insertReservationQuery)) {
                double paymentAmount = number_of_tickets * ticketPrice;

                insertStmt.setInt(1, customer_id);
                insertStmt.setInt(2, event_id);
                insertStmt.setInt(3, number_of_tickets);
                insertStmt.setDouble(4, paymentAmount);
                insertStmt.setInt(5, ticketId);


                insertStmt.executeUpdate();
                System.out.println("Reservation created successfully!");
            } catch (SQLException e) {
                System.out.println("Error creating reservation: " + e.getMessage());
                return false;
            }

            // Update ticket availability
            try (PreparedStatement updateStmt = con.prepareStatement(updateAvailabilityQuery)) {
                updateStmt.setInt(1, number_of_tickets);
                updateStmt.setInt(2, event_id);
                updateStmt.setString(3, seat_type);

                int rowsUpdated = updateStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    System.out.println("Failed to update ticket availability.");
                    return false;
                }
                System.out.println("Ticket availability updated successfully!");
                return true; // Reservation and ticket update were successful
            } catch (SQLException e) {
                System.out.println("Error updating ticket availability: " + e.getMessage());
                return false;
            }

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean cancelReservation(int reservation_id, boolean applyCancellationFee) {
        // SQL queries
        String getReservationDetailsQuery = "SELECT event_id, ticket_id, number_of_tickets, payment_amount FROM Reservations WHERE reservation_id = ?";
        String updateTicketAvailabilityQuery = "UPDATE Tickets SET availability = availability + ? WHERE ticket_id = ?";
        String deleteReservationQuery = "DELETE FROM Reservations WHERE reservation_id = ?";

        try {
            // Get reservation details (event_id, seat_type, number_of_tickets, payment_amount)
            int event_id = 0;
            int ticketId = 0;
            int number_of_tickets = 0;
            double payment_amount = 0.0;

            try (PreparedStatement getReservationStmt = con.prepareStatement(getReservationDetailsQuery)) {
                getReservationStmt.setInt(1, reservation_id);
                ResultSet rs = getReservationStmt.executeQuery();

                if (rs.next()) {
                    event_id = rs.getInt("event_id");
                    ticketId = rs.getInt("ticket_id");
                    number_of_tickets = rs.getInt("number_of_tickets");
                    payment_amount = rs.getDouble("payment_amount");
                } else {
                    System.out.println("Reservation not found.");
                    return false; // Reservation not found
                }
            }

            // Calculate the refund (if cancellation fee applies)
            double refundAmount = payment_amount;
            if (applyCancellationFee) {
                double cancellationFee = 0.1; // 10% cancellation fee for example
                refundAmount = payment_amount * (1 - cancellationFee); // Calculate the refund after the fee
                System.out.println("Cancellation fee applied. Refund: " + refundAmount);
            }

            // Update ticket availability by adding back the canceled tickets
            try (PreparedStatement updateAvailabilityStmt = con.prepareStatement(updateTicketAvailabilityQuery)) {
                updateAvailabilityStmt.setInt(1, number_of_tickets);
                updateAvailabilityStmt.setInt(2, ticketId);

                int rowsUpdated = updateAvailabilityStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    System.out.println("Failed to update ticket availability.");
                    return false;
                }
                System.out.println("Ticket availability updated successfully!");
            }

            // Delete the reservation
            try (PreparedStatement deleteReservationStmt = con.prepareStatement(deleteReservationQuery)) {
                deleteReservationStmt.setInt(1, reservation_id);
                deleteReservationStmt.executeUpdate();
                System.out.println("Reservation canceled successfully!");
            }

            // Handle refund (you can integrate with a payment gateway here for real refunds)
            System.out.println("Refund amount: " + refundAmount);
            // Implement the refund logic (e.g., update payment status or integrate with payment provider)

            return true; // Cancellation and refund successful

        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false;
        }
    }

    public static boolean cancelEvent(int event_id) {
        // SQL queries
        String getReservationsQuery = "SELECT reservation_id, customer_id, ticket_id, number_of_tickets, payment_amount FROM Reservations WHERE event_id = ?";
//        String updateTicketAvailabilityQuery = "UPDATE Tickets SET availability = availability + ? WHERE ticket_id = ?";
        String deleteTicketsQuery = "DELETE FROM Tickets WHERE event_id = ?";
        String deleteReservationsQuery = "DELETE FROM Reservations WHERE event_id = ?";

        try {
            // Get all reservations for the canceled event
            ArrayList<HashMap<String, Object>> reservations = new ArrayList<>();
            try (PreparedStatement getReservationsStmt = con.prepareStatement(getReservationsQuery)) {
                getReservationsStmt.setInt(1, event_id);
                ResultSet rs = getReservationsStmt.executeQuery();

                while (rs.next()) {
                    HashMap<String, Object> reservation = new HashMap<>();
                    reservation.put("customer_id", rs.getInt("customer_id"));
                    reservation.put("ticket_id", rs.getInt("ticket_id"));
                    reservation.put("number_of_tickets", rs.getInt("number_of_tickets"));
                    reservation.put("payment_amount", rs.getDouble("payment_amount"));

                    reservations.add(reservation);
                }
            }

            // Refund and update ticket availability
            for (HashMap<String, Object> reservation : reservations) {
                // Retrieve values from the map
                int customerId = (int) reservation.get("customer_id");
                double paymentAmount = (double) reservation.get("payment_amount");

                // Delete the reservations
                try (PreparedStatement deleteReservationStmt = con.prepareStatement(deleteReservationsQuery)) {
                    deleteReservationStmt.setInt(1, event_id);
                    deleteReservationStmt.executeUpdate();
                    System.out.println("Reservation canceled successfully!");
                }


                // Refund the customer (you can integrate with a payment gateway here)
                System.out.println("Refunding customer " + customerId + " for amount: " + paymentAmount);
                // Implement the refund logic here (e.g., payment gateway integration)

            }

            // Delete the Tickets
            try (PreparedStatement deleteReservationStmt = con.prepareStatement(deleteTicketsQuery)) {
                deleteReservationStmt.setInt(1, event_id);
                deleteReservationStmt.executeUpdate();
                System.out.println("Tickets canceled successfully!");
            }
            return true; // Event cancellation and refunds successful
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return false; // Error during cancellation process
        }
    }

}
