package com.DBProject.gui.customer;

import com.DBProject.data.DBManager;
import com.DBProject.gui.records.Customer;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class customer {
    public static void registration_form() {
        ArrayList<Customer> customers = new ArrayList<>();

        JFrame frame = new JFrame("User Registration");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new GridLayout(5, 2));

        JLabel first_name_Label = new JLabel("First Name:");
        JTextField first_name = new JTextField();

        JLabel last_name_Label = new JLabel("Last Name:");
        JTextField last_name = new JTextField();

        JLabel email_Label = new JLabel("Email:");
        JTextField email = new JTextField();

        JLabel credit_card_info_Label = new JLabel("Credit Card:");
        JTextField credit_card_info = new JTextField();

        JButton submit_button = new JButton("Register");
        JLabel message = new JLabel("", SwingConstants.CENTER);


        frame.add(first_name_Label);
        frame.add(first_name);

        frame.add(last_name_Label);
        frame.add(last_name);

        frame.add(email_Label);
        frame.add(email);

        frame.add(credit_card_info_Label);
        frame.add(credit_card_info);

        frame.add(submit_button);
        frame.add(message);

        // ACTION LISTENER
        submit_button.addActionListener(_ -> {
            if (first_name.getText().isEmpty() || last_name.getText().isEmpty() || email.getText().isEmpty() || credit_card_info.getText().isEmpty()) {
                message.setText("All fields are required!");
                message.setForeground(Color.RED);
            } else {
                message.setText("Registration successful!");
                message.setForeground(Color.GREEN);

                System.out.println("User Details:");
                System.out.println("First Name: " + first_name.getText());
                System.out.println("Last Name: " + last_name.getText());
                System.out.println("Email: " + email.getText());
                System.out.println("Credit Card: " + credit_card_info.getText());

                Customer customer = new Customer(first_name.getText(), last_name.getText(), email.getText(), credit_card_info.getText());

                customers.add(customer);

                DBManager.registerCustomer(customer);
                System.out.println(customers);
                frame.dispose();

            }
        });



        frame.setVisible(true);
    }
}
