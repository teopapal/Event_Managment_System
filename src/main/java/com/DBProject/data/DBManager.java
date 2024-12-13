package com.DBProject.data;

import com.DBProject.gui.enums.Seat_type;
import com.DBProject.gui.records.Customer;
import com.DBProject.gui.records.Event;
import com.DBProject.gui.records.Reservation;
import com.DBProject.gui.records.Ticket;

import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;

import static com.DBProject.gui.enums.Seat_type.*;

public class DBManager {
    private static Connection con;

    public static void initialize() {
        String createEventsTable = "CREATE TABLE IF NOT EXISTS events (" +
                "event_id INT AUTO_INCREMENT PRIMARY KEY," +
                "event_name VARCHAR(255) NOT NULL UNIQUE," +
                "event_date DATE NOT NULL," +
                "event_time TIME NOT NULL," +
                "event_type ENUM('CONCERT','SPORTS','THEATER','SEMINAR','CONFERENCE','MEETING','OTHER') NOT NULL," +
                "availability INT NOT NULL," +
                "capacity INT NOT NULL" +
        ");";

        String createCustomersTable =
                "CREATE TABLE IF NOT EXISTS Customers (" +
                        "customer_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "firstName VARCHAR(255) NOT NULL," +
                        "lastName VARCHAR(255) NOT NULL," +
                        "email VARCHAR(255) UNIQUE NOT NULL," +
                        "credit_card_details BIGINT NOT NULL" +
                        ");";

        String createTicketsTable =
                "CREATE TABLE IF NOT EXISTS Tickets (" +
                        "ticket_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "event_name VARCHAR(255) NOT NULL," +
                        "seat_type ENUM('VIP', 'Regular', 'Student') NOT NULL," +
                        "price DECIMAL(10, 2) NOT NULL," +
                        "reserved BOOLEAN NOT NULL DEFAULT FALSE," +
                        "FOREIGN KEY (event_name) REFERENCES Events(event_name) ON DELETE CASCADE" +
                        ");";

        String createReservationsTable =
                "CREATE TABLE IF NOT EXISTS Reservations (" +
                        "reservation_id INT AUTO_INCREMENT PRIMARY KEY," +
                        "customer_id INT NOT NULL," +
                        "event_name VARCHAR(255) NOT NULL," +
                        "ticket_id INT NOT NULL," +
                        "number_of_tickets INT NOT NULL," +
                        "reservation_date DATE NOT NULL DEFAULT CURRENT_DATE," +
                        "payment_amount DECIMAL(10, 2) NOT NULL," +
                        "FOREIGN KEY (customer_id) REFERENCES Customers(customer_id) ON DELETE CASCADE," +
                        "FOREIGN KEY (event_name) REFERENCES Events(event_name) ON DELETE CASCADE," +
                        "FOREIGN KEY (ticket_id) REFERENCES Tickets(ticket_id) ON DELETE CASCADE" +  // Foreign key to Tickets seat_type
                        ");";

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            String url = "jdbc:mysql://localhost";
            String databaseName = "test";
            int port = 3306;
            String username = "root";
            String password = "";
            con = DriverManager.getConnection(url + ":" + port + "/" + databaseName + "?characterEncoding=UTF-8", username, password);
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

    public static void registerCustomer(Customer client) {
        if (client.first_name().isEmpty() || client.last_name().isEmpty()  || client.email().isEmpty() || client.credit_card_info().isEmpty()) {
            return; // Validation check for empty fields
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
                return; // Return false to indicate failure (duplicate email)
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return; // Return false if there was an error executing the check query
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
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    // Method to add a new event
    public static void createEvent(Event event) {
        if (event.name().isEmpty() || event.date() == null  || event.time() == null || event.capacity() <= 0) {
            return; // Validation check for empty fields or invalid capacity
        }

        // SQL query to insert a new event
        String insertQuery = "INSERT INTO Events (event_name, event_type, event_date, event_time, availability, capacity) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertEvent = con.prepareStatement(insertQuery)) {

            // Set parameters for the SQL query
            insertEvent.setString(1, event.name());
            insertEvent.setString(2, event.type().toString());
            insertEvent.setDate(3, event.date());
            insertEvent.setTime(4, event.time());
            insertEvent.setInt(5, event.capacity());
            insertEvent.setInt(6, event.capacity());

            // Execute the insert query
            insertEvent.executeUpdate();
            System.out.println("Event created successfully!"); // Inform the user about successful event creation


            for(int i=0; i<=4; i++) {
                addTickets(event.name(), VIP, 100.0);
                addTickets(event.name(), Regular, 50.0);
                addTickets(event.name(), Student, 5.0);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static HashMap<Integer, HashMap<String, Object>> showAvailableTickets(Ticket tickets) {
        HashMap<Integer, HashMap<String, Object>> availableTickets = new HashMap<>();
        if(tickets.event_name() == null || tickets.event_name().isEmpty()) {
            System.out.println("Event name cannot be empty.");
            return availableTickets;
        }

        String query = "SELECT ticket_id, price, seat_type, event_name " +
                "FROM Tickets " +
                "WHERE event_name = ? AND seat_type = ? AND reserved = FALSE";

        try(PreparedStatement stmt = con.prepareStatement(query)) {
            stmt.setString(1, tickets.event_name());
            stmt.setString(2, String.valueOf(tickets.seat_type()));
            ResultSet rs = stmt.executeQuery();

            while(rs.next()) {
                int ticketId = rs.getInt("ticket_id");
                double price = rs.getDouble("price");
                String seatType = rs.getString("seat_type");
                String eventName = rs.getString("event_name");
                HashMap<String, Object> ticketDetails = new HashMap<>();
                ticketDetails.put("price", price);
                ticketDetails.put("seat_type", seatType);
                ticketDetails.put("event_name", eventName);
                availableTickets.put(ticketId, ticketDetails);
            }

            if(availableTickets.isEmpty()) {
                System.out.println("No tickets found for the event: " + tickets.event_name() + " with seat type: " + tickets.seat_type() + ".");
            }
        } catch (SQLException e) {
            System.out.println("Error fetching available tickets: " + e.getMessage() + ".");
        }
        return availableTickets;
    }

    public static void addTickets(String event_name, Seat_type seat_type, Double price) {
        String insertQuery = "INSERT INTO Tickets (event_name, seat_type, price) VALUES (?, ?, ?)";

        try (PreparedStatement insertEvent = con.prepareStatement(insertQuery)) {
            insertEvent.setString(1, event_name);
            insertEvent.setString(2, seat_type.toString());
            insertEvent.setDouble(3, price);

            insertEvent.executeUpdate();
            System.out.println("Ticket created successfully!");
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
        }

    }

    public static boolean addReservation(Reservation reservation) {
        String checkEventAvailabilityQuery = "SELECT availability FROM Events WHERE event_name = ?";
        String checkTicketsQuery = "SELECT ticket_id, price FROM Tickets WHERE event_name = ? AND seat_type = ? AND reserved = FALSE LIMIT ?";
        String updateEventAvailabilityQuery = "UPDATE Events SET availability = availability - ? WHERE event_name = ?";
        String insertReservationQuery = "INSERT INTO Reservations (customer_id, event_name, ticket_id, number_of_tickets, payment_amount) VALUES (?, ?, ?, ?, ?)";
        String updateTicketReservedStatusQuery = "UPDATE Tickets SET reserved = TRUE WHERE ticket_id = ?";

        try {
            int eventAvailability;
            ArrayList<Integer> ticketIds = new ArrayList<>();
            double ticketPrice;
            double totalPaymentAmount = 0;

            try (PreparedStatement eventStmt = con.prepareStatement(checkEventAvailabilityQuery)) {
                eventStmt.setString(1, reservation.event_name());
                ResultSet rs = eventStmt.executeQuery();
                if (rs.next()) {
                    eventAvailability = rs.getInt("availability");
                    if (eventAvailability < reservation.number_of_tickets()) {
                        System.out.println("Not enough tickets available for this event.");
                        return false;
                    }
                }
                else {
                    System.out.println("Event not found.");
                    return false;
                }
            }

            try (PreparedStatement ticketStmt = con.prepareStatement(checkTicketsQuery)) {
                ticketStmt.setString(1, reservation.event_name());
                ticketStmt.setString(2, String.valueOf(reservation.seat_type()));
                ticketStmt.setInt(3, reservation.number_of_tickets());

                ResultSet rs = ticketStmt.executeQuery();
                while (rs.next()) {
                    int ticketId = rs.getInt("ticket_id");
                    ticketPrice = rs.getDouble("price");
                    ticketIds.add(ticketId);
                    totalPaymentAmount += ticketPrice;
                }
            }

            if (ticketIds.size() < reservation.number_of_tickets()) {
                System.out.println("Not enough tickets available for the requested seat type.");
                return false;
            }

            try (PreparedStatement reservationStmt = con.prepareStatement(insertReservationQuery)) {
                for (Integer ticketId : ticketIds) {
                    reservationStmt.setInt(1, reservation.customer_id());
                    reservationStmt.setString(2, reservation.event_name());
                    reservationStmt.setInt(3, ticketId);
                    reservationStmt.setInt(4, reservation.number_of_tickets());
                    reservationStmt.setDouble(5, totalPaymentAmount);
                    reservationStmt.addBatch();
                }

                reservationStmt.executeBatch();
            }

            try (PreparedStatement updateTicketStmt = con.prepareStatement(updateTicketReservedStatusQuery)) {
                for (Integer ticketId : ticketIds) {
                    updateTicketStmt.setInt(1, ticketId);
                    updateTicketStmt.addBatch();
                }
                updateTicketStmt.executeBatch();
            }

            try (PreparedStatement updateEventStmt = con.prepareStatement(updateEventAvailabilityQuery)) {
                updateEventStmt.setInt(1, reservation.number_of_tickets());
                updateEventStmt.setString(2, reservation.event_name());
                int rowsUpdated = updateEventStmt.executeUpdate();
                if (rowsUpdated == 0) {
                    System.out.println("Failed to update event availability.");
                    return false;
                }
            }

            System.out.println("Reservation(s) created, tickets marked as reserved, and event availability updated successfully!");
            return true;

        } catch (SQLException e) {
            System.out.println("Error while creating reservation or updating availability: " + e.getMessage());
            return false;
        }
    }

    public static boolean cancelReservation(int reservation_id, boolean applyCancellationFee) {

        String getReservationDetailsQuery = "SELECT event_id, ticket_id, number_of_tickets, payment_amount FROM Reservations WHERE reservation_id = ?";
        String updateTicketAvailabilityQuery = "UPDATE Tickets SET availability = availability + ? WHERE ticket_id = ?";
        String deleteReservationQuery = "DELETE FROM Reservations WHERE reservation_id = ?";

        try {
            int event_id = 0;
            int ticketId;
            int number_of_tickets;
            double payment_amount;

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

            double refundAmount = payment_amount;
            if (applyCancellationFee) {
                double cancellationFee = 0.1; // 10% cancellation fee for example
                refundAmount = payment_amount * (1 - cancellationFee);
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
