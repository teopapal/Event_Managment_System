package com.DBProject.gui.reservation;

import com.DBProject.data.DBManager;
import com.DBProject.gui.event.Event_type;
import com.DBProject.gui.records.Event;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class reservation {
    public static void cancel_reservation() {

        JFrame frame = new JFrame("Cancel Reservation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Cancel Reservation", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(title, BorderLayout.NORTH);

        JPanel reservation_panel = new JPanel(new GridBagLayout());
        reservation_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(4, 4, 4, 4);


        JLabel event_id_label = new JLabel("Event ID:");
        JTextField event_id = new JTextField();

        JLabel customer_id_label = new JLabel("Customer ID:");
        JTextField customer_id = new JTextField();

        JLabel seat_type_label = new JLabel("Seat Type:");
        JTextField seat_type = new JTextField();

        JLabel tickets_label = new JLabel("Tickets:");
        JTextField tickets = new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        reservation_panel.add(event_id_label, gbc);
        gbc.gridx = 1;
        reservation_panel.add(event_id, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        reservation_panel.add(customer_id_label, gbc);
        gbc.gridx = 1;
        reservation_panel.add(customer_id, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        reservation_panel.add(seat_type_label, gbc);
        gbc.gridx = 1;
        reservation_panel.add(seat_type, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        reservation_panel.add(tickets_label, gbc);
        gbc.gridx = 1;
        reservation_panel.add(tickets, gbc);

        frame.add(reservation_panel, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new BorderLayout());
        JButton submit_button = new JButton("Cancel Reservation");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        bottom_panel.add(submit_button, BorderLayout.CENTER);
        bottom_panel.add(message, BorderLayout.SOUTH);

        frame.add(bottom_panel, BorderLayout.SOUTH);


        submit_button.addActionListener(_ -> {
            if (event_id.getText().isEmpty() || customer_id.getText().isEmpty() || seat_type.getText().isEmpty() || tickets.getText().isEmpty()) {
                message.setText("All fields are required!");
                message.setForeground(Color.RED);
            } else {
                message.setText("Reservation cancelled successfully!");
                message.setForeground(Color.GREEN);

                System.out.println("Reservation Details:");
                System.out.println("Event ID: " + event_id.getText());
                System.out.println("Customer ID: " + customer_id.getText());
                System.out.println("Seat Type: " + seat_type.getText());
                System.out.println("Tickets: " + tickets.getText());

//                DBManager.cancelReservation(event_id.getText(), customer_id.getText(), seat_type.getText(), tickets.getText());
                frame.dispose();
            }
        });


        frame.setVisible(true);

    }
}
