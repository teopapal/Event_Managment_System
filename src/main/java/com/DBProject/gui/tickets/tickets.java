package com.DBProject.gui.tickets;

import com.DBProject.data.DBManager;
import com.DBProject.gui.enums.Seat_type;
import com.DBProject.gui.records.Ticket;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class tickets {
    public static void show_available_tickets() {
        JFrame frame = new JFrame("Available Tickets");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);

        JLabel title = new JLabel("Available Tickets", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(16.0f));
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

                // Fetch the tickets map
                HashMap<Integer, HashMap<String, Object>> tickets_map = DBManager.showAvailableTickets(tickets);
                System.out.println(tickets_map);
                if (tickets_map == null || tickets_map.isEmpty()) {
                    JOptionPane.showMessageDialog(frame, "No tickets available!", "Information", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    // Create a new JFrame to display the tickets_map
                    JFrame ticketsFrame = new JFrame("Available Tickets");
                    ticketsFrame.setSize(600, 400);

                    // Create column names for JTable
                    String[] columnNames = {"Ticket ID", "Details"};

                    // Prepare data for JTable
                    Object[][] tableData = new Object[tickets_map.size()][2];
                    int row = 0;
                    for (Map.Entry<Integer, HashMap<String, Object>> entry : tickets_map.entrySet()) {
                        tableData[row][0] = entry.getKey(); // Ticket ID
                        tableData[row][1] = entry.getValue().toString(); // Details as a flattened string
                        row++;
                    }

                    // Create JTable
                    JTable table = new JTable(tableData, columnNames);
                    table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS);

                    // Add the table to a scroll pane
                    JScrollPane scrollPane = new JScrollPane(table);
                    ticketsFrame.add(scrollPane);

                    // Show the frame
                    ticketsFrame.setVisible(true);
                }
            }
        });

        frame.setVisible(true);
    }
}