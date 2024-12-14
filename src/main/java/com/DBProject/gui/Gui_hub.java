package com.DBProject.gui;

import com.DBProject.gui.PanelManager.PanelManager;

import javax.swing.*;
import java.awt.*;

import static com.DBProject.gui.customer.customer.registration_form;
import static com.DBProject.gui.event.event.cancel_event;
import static com.DBProject.gui.event.event.create_event_form;
import static com.DBProject.gui.reservation.reservation.add_reservation;
import static com.DBProject.gui.reservation.reservation.cancel_reservation;
import static com.DBProject.gui.sql_input.sql_input.create_sql_input;


public class Gui_hub {
    public Gui_hub() {
        PanelManager panel_manager = new PanelManager();
        JFrame main_frame = new JFrame("HY-360 Project");
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        main_frame.add(panel_manager.get_content_panel());
        JPanel content_panel = panel_manager.get_content_panel();
        CardLayout card_layout = panel_manager.get_card_layout();
        main_frame.setSize(700, 500);

        JPanel main_panel = create_main_menu(panel_manager);
        main_frame.setVisible(true);
        content_panel.add(main_panel, "main_panel");
        card_layout.show(content_panel, "main_panel");
    }


    private JPanel create_main_menu(PanelManager panel_manager) {
        JPanel panel = new JPanel(new BorderLayout());
        JLabel welcome_label = new JLabel("Welcome to HY-360 Project", SwingConstants.CENTER);
        welcome_label.setFont(new Font("Arial", Font.BOLD, 16));
        welcome_label.setBorder(BorderFactory.createEmptyBorder(50, 0, 0, 0));
        panel.add(welcome_label, BorderLayout.NORTH);

        JPanel button_panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        JButton register_button = new JButton("Register User");
        register_button.setPreferredSize(new Dimension(200, 40));

        JButton create_event_button = new JButton("Create Event");
        create_event_button.setPreferredSize(new Dimension(200, 40));

        JButton add_reservation_button = new JButton("Add Reservation");
        add_reservation_button.setPreferredSize(new Dimension(200, 40));

        JButton cancel_reservation_button = new JButton("Cancel Reservation");
        cancel_reservation_button.setPreferredSize(new Dimension(200, 40));

        JButton cancel_event_button = new JButton("Cancel Event");
        cancel_event_button.setPreferredSize(new Dimension(200, 40));

        JButton Sql_button = new JButton("Write SQL");
        Sql_button.setPreferredSize(new Dimension(200, 40));

        gbc.gridx = 0;
        gbc.gridy = 0;
        button_panel.add(register_button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        button_panel.add(create_event_button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        button_panel.add(add_reservation_button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        button_panel.add(cancel_reservation_button, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        button_panel.add(cancel_event_button, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        button_panel.add(Sql_button, gbc);

        panel.add(button_panel, BorderLayout.CENTER);

        register_button.addActionListener(_ -> registration_form(panel_manager));
        create_event_button.addActionListener(_ -> create_event_form(panel_manager));
        add_reservation_button.addActionListener(_ -> add_reservation(panel_manager));
        cancel_reservation_button.addActionListener(_ -> cancel_reservation(panel_manager));
        cancel_event_button.addActionListener(_ -> cancel_event(panel_manager));
        Sql_button.addActionListener(_ -> create_sql_input(panel_manager));

        return panel;
    }


}
