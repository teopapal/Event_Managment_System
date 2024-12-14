package com.DBProject.gui.event;

import com.DBProject.data.DBManager;
import com.DBProject.gui.PanelManager.PanelManager;
import com.DBProject.gui.enums.Event_type;
import com.DBProject.gui.records.Event;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

import static com.DBProject.gui.helper_functions.helper_functions.*;

public class event {
    public static void create_event_form(PanelManager panel_manager) {
        JPanel create_event_panel = new JPanel(new BorderLayout());
        create_event_panel.setBackground(Color.DARK_GRAY);

        JPanel top_panel = new JPanel(new BorderLayout());
        top_panel.setBackground(Color.DARK_GRAY);
        JButton back_button = create_button("Back");
        back_button.setPreferredSize(new Dimension(60, 30));
        back_button.addActionListener(_ -> panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel"));
        top_panel.add(back_button, BorderLayout.WEST);

        JLabel title = new JLabel("Create a new Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        top_panel.add(title, BorderLayout.CENTER);

        create_event_panel.add(top_panel, BorderLayout.NORTH);

        JPanel event_form = new JPanel(new GridBagLayout());
        event_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        event_form.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel event_name_label = create_label("Event Name:");
        JTextField event_name = create_field();

        JLabel event_date_label = create_label("Event Date (dd/mm/yyyy):");
        JTextField event_date = create_field();

        JLabel event_time_label = create_label("Event Time (hh:mm):");
        JTextField event_time = create_field();

        JLabel event_type_label = create_label("Event Type:");
        JComboBox<Event_type> event_type_box = new JComboBox<>(Event_type.values());
        event_type_box.setBackground(Color.DARK_GRAY);
        event_type_box.setForeground(Color.WHITE);

        JLabel event_capacity_label = create_label("Event Capacity:");
        JTextField event_capacity = create_field();

        gbc.gridx = 0;
        gbc.gridy = 0;
        event_form.add(event_name_label, gbc);
        gbc.gridx = 1;
        event_form.add(event_name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        event_form.add(event_date_label, gbc);
        gbc.gridx = 1;
        event_form.add(event_date, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        event_form.add(event_time_label, gbc);
        gbc.gridx = 1;
        event_form.add(event_time, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        event_form.add(event_type_label, gbc);
        gbc.gridx = 1;
        event_form.add(event_type_box, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        event_form.add(event_capacity_label, gbc);
        gbc.gridx = 1;
        event_form.add(event_capacity, gbc);

        create_event_panel.add(event_form, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new BorderLayout());
        bottom_panel.setBackground(Color.DARK_GRAY);
        JButton submit_button = create_button("Create Event");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        bottom_panel.add(submit_button, BorderLayout.CENTER);
        bottom_panel.add(message, BorderLayout.SOUTH);

        create_event_panel.add(bottom_panel, BorderLayout.SOUTH);

        submit_button.addActionListener(_ -> {
            if (event_name.getText().isEmpty() || event_date.getText().isEmpty() || event_time.getText().isEmpty() || event_capacity.getText().isEmpty()) {
                message.setText("All fields are required!");
                message.setForeground(Color.RED);
            } else {
                try {
                    DateTimeFormatter date_formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                    LocalDate local_date = LocalDate.parse(event_date.getText(), date_formatter);

                    DateTimeFormatter time_formatter = DateTimeFormatter.ofPattern("HH:mm");
                    LocalTime local_time = LocalTime.parse(event_time.getText(), time_formatter);

                    Event event = new Event(
                            event_name.getText(),
                            (Event_type) event_type_box.getSelectedItem(),
                            Date.valueOf(local_date),
                            Time.valueOf(local_time),
                            Integer.parseInt(event_capacity.getText())
                    );

                    DBManager.createEvent(event);

                    message.setText("Event created successfully!");
                    message.setForeground(Color.GREEN);

                    Timer timer = new Timer(1000, _ -> panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel"));
                    timer.setRepeats(false);
                    timer.start();
                } catch (Exception ex) {
                    message.setText("Invalid input! Please check the fields.");
                    message.setForeground(Color.RED);
                }
            }
        });

        panel_manager.get_content_panel().add(create_event_panel, "create_event_panel");
        panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "create_event_panel");
    }

    public static void cancel_event(PanelManager panel_manager) {
        JPanel cancel_event_panel = new JPanel(new BorderLayout());
        cancel_event_panel.setBackground(Color.DARK_GRAY);

        JPanel top_panel = new JPanel(new BorderLayout());
        top_panel.setBackground(Color.DARK_GRAY);
        JButton back_button = create_button("Back");
        back_button.setPreferredSize(new Dimension(60, 30));
        back_button.addActionListener(_ -> panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel"));
        top_panel.add(back_button, BorderLayout.WEST);

        JLabel title = new JLabel("Cancel an Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        top_panel.add(title, BorderLayout.CENTER);

        cancel_event_panel.add(top_panel, BorderLayout.NORTH);

        JPanel event_form = new JPanel(new GridBagLayout());
        event_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        event_form.setBackground(Color.DARK_GRAY);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel event_name_label = create_label("Event Name:");
        JTextField event_name = create_field();

        gbc.gridx = 0;
        gbc.gridy = 0;
        event_form.add(event_name_label, gbc);
        gbc.gridx = 1;
        event_form.add(event_name, gbc);

        cancel_event_panel.add(event_form, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new BorderLayout());
        bottom_panel.setBackground(Color.DARK_GRAY);
        JButton submit_button = create_button("Cancel Event");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        bottom_panel.add(submit_button, BorderLayout.CENTER);
        bottom_panel.add(message, BorderLayout.SOUTH);

        cancel_event_panel.add(bottom_panel, BorderLayout.SOUTH);

        submit_button.addActionListener(_ -> {
            if (event_name.getText().isEmpty()) {
                message.setText("Event Name is required!");
                message.setForeground(Color.RED);
            } else {
                try {
                    if (DBManager.cancelEvent(event_name.getText())) {
                        message.setText("Event cancelled successfully!");
                        message.setForeground(Color.GREEN);
                    } else {
                        message.setText("Event cancellation failed!");
                        message.setForeground(Color.RED);
                    }

                    Timer timer = new Timer(1000, _ -> panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel"));
                    timer.setRepeats(false);
                    timer.start();
                } catch (Exception ex) {
                    message.setText("Invalid input! Please check the fields.");
                    message.setForeground(Color.RED);
                }
            }
        });

        panel_manager.get_content_panel().add(cancel_event_panel, "cancel_event_panel");
        panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "cancel_event_panel");
    }
}