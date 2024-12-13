package com.DBProject.gui.customer;

import com.DBProject.data.DBManager;
import com.DBProject.gui.PanelManager.PanelManager;
import com.DBProject.gui.records.Customer;

import javax.swing.*;
import java.awt.*;

public class customer {
    public static void registration_form(PanelManager panel_manager) {
        JPanel registration_panel = new JPanel(new BorderLayout());

        JPanel top_panel = new JPanel(new BorderLayout());
        JButton back_button = new JButton("Back");
        back_button.addActionListener(_ -> panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel"));
        top_panel.add(back_button, BorderLayout.WEST);

        JLabel title = new JLabel("User Registration", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        top_panel.add(title, BorderLayout.CENTER);

        registration_panel.add(top_panel, BorderLayout.NORTH);

        JPanel user_form = new JPanel(new GridBagLayout());
        user_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel first_name_label = new JLabel("First Name:");
        JTextField first_name = new JTextField();
        first_name.setPreferredSize(new Dimension(100, 20));

        JLabel last_name_label = new JLabel("Last Name:");
        JTextField last_name = new JTextField();
        last_name.setPreferredSize(new Dimension(100, 20));

        JLabel email_label = new JLabel("Email:");
        JTextField email = new JTextField();
        email.setPreferredSize(new Dimension(100, 20));

        JLabel credit_card_label = new JLabel("Credit Card:");
        JTextField credit_card = new JTextField();
        credit_card.setPreferredSize(new Dimension(100, 20));

        gbc.gridx = 0;
        gbc.gridy = 0;
        user_form.add(first_name_label, gbc);
        gbc.gridx = 1;
        user_form.add(first_name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        user_form.add(last_name_label, gbc);
        gbc.gridx = 1;
        user_form.add(last_name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        user_form.add(email_label, gbc);
        gbc.gridx = 1;
        user_form.add(email, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        user_form.add(credit_card_label, gbc);
        gbc.gridx = 1;
        user_form.add(credit_card, gbc);

        registration_panel.add(user_form, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new BorderLayout());
        JButton submit_button = new JButton("Register");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        bottom_panel.add(submit_button, BorderLayout.CENTER);
        bottom_panel.add(message, BorderLayout.SOUTH);

        registration_panel.add(bottom_panel, BorderLayout.SOUTH);

        submit_button.addActionListener(_ -> {
            if (first_name.getText().isEmpty() || last_name.getText().isEmpty() || email.getText().isEmpty() || credit_card.getText().isEmpty()) {
                message.setText("All fields are required!");
                message.setForeground(Color.RED);
            } else {
                message.setText("Registration successful!");
                message.setForeground(Color.GREEN);

                System.out.println("User Details:");
                System.out.println("First Name: " + first_name.getText());
                System.out.println("Last Name: " + last_name.getText());
                System.out.println("Email: " + email.getText());
                System.out.println("Credit Card: " + credit_card.getText());

                Customer customer = new Customer(first_name.getText(), last_name.getText(), email.getText(), credit_card.getText());

                DBManager.registerCustomer(customer);

                Timer timer = new Timer(1000, _ -> {
                    panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel");
                });
                timer.setRepeats(false);
                timer.start();
            }
        });

        panel_manager.get_content_panel().add(registration_panel, "registration_panel");
        panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "registration_panel");
    }
}
