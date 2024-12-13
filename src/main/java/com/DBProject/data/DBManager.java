package com.DBProject.data;

import com.DBProject.gui.enums.Seat_type;
import com.DBProject.gui.records.Customer;
import com.DBProject.gui.records.Event;
import com.DBProject.gui.records.Reservation;
import com.DBProject.gui.records.Ticket;
import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import static com.DBProject.gui.enums.Seat_type.*;

public class DBManager {
    private static Connection con;

    public static void initialize() {
        String dropReservationsTable = "DROP TABLE IF EXISTS Reservations;";
        String dropTicketsTable = "DROP TABLE IF EXISTS Tickets;";
        String dropCustomersTable = "DROP TABLE IF EXISTS Customers;";
        String dropEventsTable = "DROP TABLE IF EXISTS Events;";

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

            //statement.execute(dropReservationsTable);
            //statement.execute(dropTicketsTable);
            //statement.execute(dropCustomersTable);
            //statement.execute(dropEventsTable);

            statement.execute(createEventsTable);
            statement.execute(createCustomersTable);
            statement.execute(createTicketsTable);
            statement.execute(createReservationsTable);

            System.out.println("Database and tables initialized successfully, with empty tables.");
        }
        catch (Exception e) {
            System.out.println("Failed to initialize the database." + e);
        }
    }

    public static void registerCustomer(Customer client) {
        if (client.first_name().isEmpty() || client.last_name().isEmpty()  || client.email().isEmpty() || client.credit_card_info().isEmpty()) {
            return;
        }

        String checkEmailQuery = "SELECT COUNT(*) FROM Customers WHERE email = ?";

        try (PreparedStatement checkStmt = con.prepareStatement(checkEmailQuery)) {
            checkStmt.setString(1, client.email());
            ResultSet resultSet = checkStmt.executeQuery();

            if (resultSet.next() && resultSet.getInt(1) > 0) {
                System.out.println("Email already exists!");
                return;
            }
        }
        catch (SQLException e) {
            System.out.println(e.getMessage());
            return;
        }

        String insertQuery = "INSERT INTO Customers (firstName, lastName, email, credit_card_details) VALUES (?, ?, ?, ?)";

        try (PreparedStatement insertCustomer = con.prepareStatement(insertQuery)) {
            insertCustomer.setString(1, client.first_name());
            insertCustomer.setString(2, client.last_name());
            insertCustomer.setString(3, client.email());
            insertCustomer.setString(4, client.credit_card_info());

            insertCustomer.executeUpdate();
            System.out.println("Customer registered successfully!");
        }
        catch (Exception e) {
            System.out.println(e.getMessage());
        }

    }

    public static void createEvent(Event event) {
        if (event.name().isEmpty() || event.date() == null  || event.time() == null || event.capacity() <= 0) {
            return;
        }

        String insertQuery = "INSERT INTO Events (event_name, event_type, event_date, event_time, availability, capacity) VALUES (?, ?, ?, ?, ?, ?)";

        try (PreparedStatement insertEvent = con.prepareStatement(insertQuery)) {
            insertEvent.setString(1, event.name());
            insertEvent.setString(2, event.type().toString());
            insertEvent.setDate(3, event.date());
            insertEvent.setTime(4, event.time());
            insertEvent.setInt(5, event.capacity());
            insertEvent.setInt(6, event.capacity());

            insertEvent.executeUpdate();
            System.out.println("Event created successfully!");


            for(int i=0; i<=4; i++) {
                addTickets(event.name(), VIP, 100.0);
                addTickets(event.name(), Regular, 50.0);
                addTickets(event.name(), Student, 5.0);
            }
        }
        catch (SQLException e) {
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
        }
        catch (SQLException e) {
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

    public static void addReservation(Reservation reservation) {
        String checkEventAvailabilityQuery = "SELECT availability FROM Events WHERE event_name = ?";
        String checkTicketsQuery = "SELECT ticket_id, price FROM Tickets WHERE event_name = ? AND seat_type = ? AND reserved = FALSE LIMIT ?";
        String updateEventAvailabilityQuery = "UPDATE Events SET availability = availability - ? WHERE event_name = ?";
        String insertReservationQuery = "INSERT INTO Reservations (customer_id, event_name, ticket_id, number_of_tickets, payment_amount) VALUES (?, ?, ?, ?, ?)";
        String updateTicketReservedStatusQuery = "UPDATE Tickets SET reserved = TRUE WHERE ticket_id = ?";
        double totalPaymentAmount = 0;

        try {
            int eventAvailability;
            ArrayList<Integer> ticketIds = new ArrayList<>();
            double ticketPrice;

            try (PreparedStatement eventStmt = con.prepareStatement(checkEventAvailabilityQuery)) {
                eventStmt.setString(1, reservation.event_name());
                ResultSet rs = eventStmt.executeQuery();
                if (rs.next()) {
                    eventAvailability = rs.getInt("availability");
                    if (eventAvailability < reservation.number_of_tickets()) {
                        System.out.println("Not enough tickets available for this event.");
                        return;
                    }
                }
                else {
                    System.out.println("Event not found.");
                    return;
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
                return;
            }

            try (PreparedStatement reservationStmt = con.prepareStatement(insertReservationQuery)) {
                for (Integer ticketId : ticketIds) {
                    reservationStmt.setInt(1, reservation.customer_id());
                    reservationStmt.setString(2, reservation.event_name());
                    reservationStmt.setInt(3, ticketId);
                    reservationStmt.setInt(4, reservation.number_of_tickets());
                    reservationStmt.setDouble(5, totalPaymentAmount/reservation.number_of_tickets());
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
                    return;
                }
            }

            System.out.println("User" + reservation.customer_id() + " charged " + totalPaymentAmount);

        }
        catch (SQLException e) {
            System.out.println("Error while creating reservation or updating availability: " + e.getMessage());
        }
    }

    public static boolean cancelReservation(Reservation reservation) {
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            con.setAutoCommit(false);

            String fetchTicketIdsQuery = "SELECT ticket_id FROM Reservations WHERE customer_id = ? AND event_name = ?";
            stmt = con.prepareStatement(fetchTicketIdsQuery);
            stmt.setInt(1, reservation.customer_id());
            stmt.setString(2, reservation.event_name());
            rs = stmt.executeQuery();

            List<Integer> ticketIds = new ArrayList<>();
            while (rs.next()) {
                ticketIds.add(rs.getInt("ticket_id"));
            }

            if(ticketIds.isEmpty()) {
                System.out.println("No tickets found for this customer and event.");
                return false;
            }

            String fetchFilteredTicketsQuery = "SELECT ticket_id, price FROM Tickets WHERE seat_type = ? AND ticket_id IN (" +
                    ticketIds.stream().map(String::valueOf).collect(Collectors.joining(",")) + ")";
            stmt = con.prepareStatement(fetchFilteredTicketsQuery);
            stmt.setString(1, String.valueOf(reservation.seat_type()));
            rs = stmt.executeQuery();

            List<Integer> filteredTicketIds = new ArrayList<>();
            double totalPrice = 0;
            while (rs.next()) {
                int ticketId = rs.getInt("ticket_id");
                double ticketPrice = rs.getDouble("price");
                filteredTicketIds.add(ticketId);
                totalPrice += ticketPrice;
            }

            if(filteredTicketIds.isEmpty()) {
                System.out.println("Error: No tickets found for the requested seat_type that match the reservation.");
                con.rollback();
                return false;
            }

            String deleteReservationsQuery = "DELETE FROM Reservations WHERE ticket_id = ?";
            stmt = con.prepareStatement(deleteReservationsQuery);
            for(int ticketId : filteredTicketIds) {
                stmt.setInt(1, ticketId);
                stmt.executeUpdate();
            }

            String unreserveTicketsQuery = "UPDATE Tickets SET reserved = 0 WHERE ticket_id = ?";
            stmt = con.prepareStatement(unreserveTicketsQuery);
            for(int ticketId : filteredTicketIds) {
                stmt.setInt(1, ticketId);
                stmt.executeUpdate();
            }

            String updateEventAvailabilityQuery = "UPDATE Events SET availability = availability + ? WHERE event_name = ?";
            stmt = con.prepareStatement(updateEventAvailabilityQuery);
            stmt.setInt(1, filteredTicketIds.size());
            stmt.setString(2, reservation.event_name());
            stmt.executeUpdate();

            double cancellationFee = 10.0;
            double refundAmount = totalPrice - cancellationFee;
            if(refundAmount < 0) {
                refundAmount = 0;
            }
            System.out.println("Refunded " + refundAmount + " euros to User" + reservation.customer_id());

            con.commit();
            return true;
        }
        catch (Exception e) {
            if(con != null) {
                try {
                    con.rollback();
                } catch (SQLException rollbackEx) {
                    logExceptionWithMessage("Rollback failed due to an error.", rollbackEx);
                }
            }
            logExceptionWithMessage("An error occurred while cancelling the reservation.", e);
            return false;
        }
        finally {
            try {
                if(rs != null) rs.close();
                if(stmt != null) stmt.close();
            }
            catch (SQLException closeEx) {
                logExceptionWithMessage("Error occurred while closing database resources", closeEx);
            }
        }
    }

    public static boolean cancelEvent(String event_name) {
        String getReservationsQuery = "SELECT customer_id, SUM(payment_amount) AS total_refund FROM Reservations WHERE event_name = ? GROUP BY customer_id";
        String deleteReservationsQuery = "DELETE FROM Reservations WHERE event_name = ?";
        String deleteTicketsQuery = "DELETE FROM Tickets WHERE event_name = ?";
        String deleteEventQuery = "DELETE FROM Events WHERE event_name = ?";

        try {
            con.setAutoCommit(false);

            HashMap<Integer, Double> refunds = new HashMap<>();
            try (PreparedStatement getReservationsStmt = con.prepareStatement(getReservationsQuery)) {
                getReservationsStmt.setString(1, event_name);
                ResultSet rs = getReservationsStmt.executeQuery();

                while (rs.next()) {
                    int customerId = rs.getInt("customer_id");
                    double totalRefund = rs.getDouble("total_refund");
                    refunds.put(customerId, totalRefund);
                }
            }

            try (PreparedStatement deleteReservationsStmt = con.prepareStatement(deleteReservationsQuery)) {
                deleteReservationsStmt.setString(1, event_name);
                deleteReservationsStmt.executeUpdate();
            }

            try (PreparedStatement deleteTicketsStmt = con.prepareStatement(deleteTicketsQuery)) {
                deleteTicketsStmt.setString(1, event_name);
                deleteTicketsStmt.executeUpdate();
            }

            try (PreparedStatement deleteEventStmt = con.prepareStatement(deleteEventQuery)) {
                deleteEventStmt.setString(1, event_name);
                deleteEventStmt.executeUpdate();
            }

            for(Map.Entry<Integer, Double> refund : refunds.entrySet()) {
                System.out.println("Refunded " + refund.getValue() + " euros to User" + refund.getKey());
            }

            con.commit();
            System.out.println("Event cancelled and all related data deleted successfully.");
            return true;
        }
        catch (SQLException e) {
            try {
                if(con != null) {
                    con.rollback();
                }
            } catch (SQLException rollbackEx) {
                logExceptionWithMessage("Rollback failed due to an error.", rollbackEx);
            }
            logExceptionWithMessage("An error occurred while cancelling the event.", e);
            return false;
        }
        finally {
            try {
                if(con != null) {
                    con.setAutoCommit(true);
                }
            } catch (SQLException ex) {
                logExceptionWithMessage("Failed to reset auto-commit.", ex);
            }
        }
    }

    private static void logExceptionWithMessage(String message, Exception e) {
        System.err.println(message);
        e.printStackTrace(System.err);
    }

    public static boolean executeQuery(String sqlQuery) {
        try (PreparedStatement stmt = con.prepareStatement(sqlQuery)){
            stmt.executeUpdate();
            return true;
        }
        catch (SQLException e) {
            logExceptionWithMessage("An error occurred while executing the query: " + sqlQuery, e);
            return false;
        }
    }
}