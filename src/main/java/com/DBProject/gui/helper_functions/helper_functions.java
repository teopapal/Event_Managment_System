package com.DBProject.gui.helper_functions;


import com.DBProject.data.DBManager;
import com.DBProject.gui.PanelManager.PanelManager;
import com.DBProject.gui.records.Customer;

import javax.swing.*;
import java.awt.*;

public class helper_functions {
    private static JPanel create_top_panel(PanelManager panelManager, Font titleFont) {
        JPanel topPanel = new JPanel(new BorderLayout());

        JButton backButton = new JButton("Back");
        backButton.addActionListener(_ -> panelManager.get_card_layout().show(panelManager.get_content_panel(), "main_panel"));
        topPanel.add(backButton, BorderLayout.WEST);

        JLabel titleLabel = new JLabel("User Registration", SwingConstants.CENTER);
        titleLabel.setFont(titleFont);
        topPanel.add(titleLabel, BorderLayout.CENTER);

        return topPanel;
    }

    private static JPanel create_bottom_panel(PanelManager panelManager, Customer customer) {
        JPanel bottomPanel = new JPanel(new BorderLayout());

        JLabel messageLabel = new JLabel("", SwingConstants.CENTER);
        JButton submitButton = new JButton("Register");

        submitButton.addActionListener(e -> handle_registration(panelManager, messageLabel, customer));

        bottomPanel.add(submitButton, BorderLayout.CENTER);
        bottomPanel.add(messageLabel, BorderLayout.SOUTH);

        return bottomPanel;
    }

    private static JPanel create_user_form(int field_width, int field_height, Insets field_insets) {
        JPanel userForm = new JPanel(new GridBagLayout());
        userForm.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = field_insets;

        add_field(userForm, gbc, "First Name:", 0, new JTextField(), field_width, field_height);
        add_field(userForm, gbc, "Last Name:", 1, new JTextField(), field_width, field_height);
        add_field(userForm, gbc, "Email:", 2, new JTextField(), field_width, field_height);
        add_field(userForm, gbc, "Credit Card:", 3, new JTextField(), field_width, field_height);

        return userForm;
    }

    private static void add_field(JPanel form, GridBagConstraints gbc, String labelText, int row, JTextField field, int width, int height) {
        JLabel label = new JLabel(labelText);
        field.setPreferredSize(new Dimension(width, height));

        gbc.gridx = 0;
        gbc.gridy = row;
        form.add(label, gbc);

        gbc.gridx = 1;
        form.add(field, gbc);
    }

    private static void handle_registration(PanelManager panelManager, JLabel messageLabel, Customer customer) {

        if (customer.first_name().isEmpty() || customer.last_name().isEmpty() || customer.email().isEmpty() || customer.credit_card_info().isEmpty()) {
            messageLabel.setText("All fields are required!");
            messageLabel.setForeground(Color.RED);
        } else {
            messageLabel.setText("Registration successful!");
            messageLabel.setForeground(Color.GREEN);

            System.out.println("User Details:");
            System.out.println("First Name: " + customer.first_name());
            System.out.println("Last Name: " + customer.last_name());
            System.out.println("Email: " + customer.email());
            System.out.println("Credit Card: " + customer.credit_card_info());

            DBManager.registerCustomer(customer);

            Timer timer = new Timer(1000, _ -> panelManager.get_card_layout().show(panelManager.get_content_panel(), "main_panel"));
            timer.setRepeats(false);
            timer.start();
        }
    }
}