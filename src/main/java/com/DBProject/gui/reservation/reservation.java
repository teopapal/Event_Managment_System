package com.DBProject.gui.reservation;

import com.DBProject.data.DBManager;
import com.DBProject.gui.enums.Seat_type;
import com.DBProject.gui.records.Reservation;
import com.DBProject.gui.records.Ticket;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class reservation {

    public static void add_reservation(){
        JFrame frame = new JFrame("Add a Reservation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Add a Reservation", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(title, BorderLayout.NORTH);

        JPanel reservation_panel = new JPanel(new GridBagLayout());
        reservation_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(2, 2, 2, 2);

        JLabel event_name_label = new JLabel("Event Name:");
        JTextField event_name = new JTextField();

        JLabel seat_type_label = new JLabel("Seat Type:");
        JComboBox<Seat_type> seat_type_box = new JComboBox<>(Seat_type.values());
        seat_type_box.addActionListener(_ -> seat_type_box.getSelectedItem());

        gbc.gridx = 0;
        gbc.gridy = 0;
        reservation_panel.add(event_name_label, gbc);
        gbc.gridx = 1;
        reservation_panel.add(event_name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        reservation_panel.add(seat_type_label, gbc);
        gbc.gridx = 1;
        reservation_panel.add(seat_type_box, gbc);

        frame.add(reservation_panel, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new BorderLayout());
        JButton submit_button = new JButton("Search Tickets");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        bottom_panel.add(submit_button, BorderLayout.CENTER);
        bottom_panel.add(message, BorderLayout.SOUTH);

        frame.add(bottom_panel, BorderLayout.SOUTH);

        submit_button.addActionListener(_ -> {
            if (event_name.getText().isEmpty()) {
                message.setText("All fields are required!");
                message.setForeground(Color.RED);
            } else {
                message.setText("Fetching available tickets...");
                message.setForeground(Color.GREEN);

                Ticket tickets = new Ticket(event_name.getText(), (Seat_type) seat_type_box.getSelectedItem());

                HashMap<Integer, HashMap<String, Object>> tickets_map = DBManager.showAvailableTickets(tickets);
                System.out.println(tickets_map);

                if (tickets_map.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No tickets available!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    JFrame tickets_frame = new JFrame("Available Tickets");
                    tickets_frame.setSize(600, 400);

                    String[] column_names = {"Ticket ID", "Seat Type", "Price"};

                    Object[][] table_data = new Object[tickets_map.size()][3];
                    int row = 0;
                    for (Map.Entry<Integer, HashMap<String, Object>> entry : tickets_map.entrySet()) {
                        table_data[row][0] = entry.getKey();
                        table_data[row][1] = entry.getValue().get("seat_type");
                        table_data[row][2] = entry.getValue().get("price");
                        row++;
                    }

                    JTable table = new JTable(table_data, column_names);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                    JScrollPane scrollPane = new JScrollPane(table);
                    tickets_frame.add(scrollPane);

                    JPanel text_panel = new JPanel(new GridBagLayout());
                    text_panel.setBorder(BorderFactory.createTitledBorder("Book Your Ticket"));

                    GridBagConstraints gbc_text = new GridBagConstraints();
                    gbc_text.insets = new Insets(2, 2, 2, 2);
                    gbc_text.fill = GridBagConstraints.HORIZONTAL;

                    JLabel customer_id_label = new JLabel("Customer ID:");
                    JTextField customer_id = new JTextField(2);

                    JLabel number_of_tickets_label = new JLabel("Number of Tickets:");
                    JTextField number_of_tickets = new JTextField(2);

                    JButton book_button = new JButton("Book Ticket");

                    gbc_text.gridx = 0;
                    gbc_text.gridy = 0;
                    text_panel.add(customer_id_label, gbc_text);
                    gbc_text.gridx = 1;
                    text_panel.add(customer_id, gbc_text);

                    gbc_text.gridx = 0;
                    gbc_text.gridy = 1;
                    text_panel.add(number_of_tickets_label, gbc_text);
                    gbc_text.gridx = 1;
                    text_panel.add(number_of_tickets, gbc_text);

                    gbc_text.gridx = 1;
                    gbc_text.gridy = 2;
                    gbc_text.anchor = GridBagConstraints.CENTER;
                    text_panel.add(book_button, gbc_text);

                    tickets_frame.add(text_panel, BorderLayout.SOUTH);

                    book_button.addActionListener(_ -> {
                        String customer = customer_id.getText();
                        String tickets_quantity = number_of_tickets.getText();
                        if (!customer.isEmpty() && !tickets_quantity.isEmpty()) {
                            try {
                                int customerId = Integer.parseInt(customer);
                                int ticketsQuantity = Integer.parseInt(tickets_quantity);
                                if (ticketsQuantity <= 0) {
                                    JOptionPane.showMessageDialog(tickets_frame, "Invalid number of tickets.", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }
                                if (ticketsQuantity > tickets_map.size()) {
                                    JOptionPane.showMessageDialog(tickets_frame, "Not enough tickets available.", "Error", JOptionPane.ERROR_MESSAGE);
                                    return;
                                }


                                Reservation reservation = new Reservation((Seat_type) seat_type_box.getSelectedItem(), customerId, event_name.getText(), ticketsQuantity);
                                DBManager.addReservation(reservation);

                            } catch (NumberFormatException ex) {
                                JOptionPane.showMessageDialog(tickets_frame, "Invalid Ticket ID format.", "Error", JOptionPane.ERROR_MESSAGE);
                            }
                        } else {
                            JOptionPane.showMessageDialog(tickets_frame, "Please enter a Ticket ID.", "Error", JOptionPane.WARNING_MESSAGE);
                        }
                    });




                    tickets_frame.setVisible(true);
                }
            }
        });

        frame.setVisible(true);
    }



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
        event_id.setPreferredSize(new Dimension(100, 20));

        JLabel customer_id_label = new JLabel("Customer ID:");
        JTextField customer_id = new JTextField();
        customer_id.setPreferredSize(new Dimension(100, 20));

        JLabel seat_type_label = new JLabel("Seat Type:");
        JTextField seat_type = new JTextField();
        seat_type.setPreferredSize(new Dimension(100, 20));

        JLabel tickets_label = new JLabel("Tickets:");
        JTextField tickets = new JTextField();
        tickets.setPreferredSize(new Dimension(100, 20));

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
