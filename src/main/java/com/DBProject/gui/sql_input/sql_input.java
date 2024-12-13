package com.DBProject.gui.sql_input;

import com.DBProject.data.DBManager;
import com.DBProject.gui.PanelManager.PanelManager;

import javax.swing.*;
import java.awt.*;

public class sql_input {
    public static void create_sql_input(PanelManager panel_manager) {
        // Create SQL Input panel
        JPanel sql_input_panel = new JPanel(new BorderLayout());

        // Top panel with Back button and title
        JPanel top_panel = new JPanel(new BorderLayout());
        top_panel.setBackground(new Color(45, 45, 45)); // Dark background for top panel

        JButton back_button = new JButton("Back");
        back_button.setBackground(new Color(60, 60, 60)); // Dark button background
        back_button.setForeground(Color.WHITE); // White text
        back_button.addActionListener(_ -> {
            // Go back to the main panel
            panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel");
        });
        top_panel.add(back_button, BorderLayout.WEST);

        JLabel title = new JLabel("SQL Input", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE); // Light text for title
        top_panel.add(title, BorderLayout.CENTER);

        sql_input_panel.add(top_panel, BorderLayout.NORTH);

        // Create form panel for SQL input
        JPanel sql_input_form = new JPanel(new GridBagLayout());
        sql_input_form.setBackground(new Color(45, 45, 45)); // Dark background for form panel
        sql_input_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        // SQL Input Label and TextField
        JLabel sql_input_label = new JLabel("SQL Input:");
        sql_input_label.setForeground(Color.WHITE); // Light text for label
        JTextArea sql_input = new JTextArea(10, 50); // Adjust size for textarea
        sql_input.setPreferredSize(new Dimension(500, 100));
        sql_input.setBackground(new Color(60, 60, 60)); // Dark background for text area
        sql_input.setForeground(Color.WHITE); // White text
        sql_input.setCaretColor(Color.WHITE); // White cursor

        gbc.gridx = 0;
        gbc.gridy = 0;
        sql_input_form.add(sql_input_label, gbc);

        gbc.gridx = 1;
        sql_input_form.add(new JScrollPane(sql_input), gbc); // Wrap text area in JScrollPane

        // Submit Button
        JButton submit_button = new JButton("Submit");
        submit_button.setBackground(new Color(60, 60, 60)); // Dark background
        submit_button.setForeground(Color.WHITE); // White text
        submit_button.addActionListener(_ -> {
            String sql = sql_input.getText();
            if (!sql.isEmpty()) {
                // Print SQL input to console or process it
                System.out.println("SQL Input: " + sql);
                DBManager.executeQuery(sql);
                // You can also add further handling here, such as saving or executing the SQL.
                JOptionPane.showMessageDialog(panel_manager.get_content_panel(), "SQL Submitted: " + sql, "SQL Input", JOptionPane.INFORMATION_MESSAGE);
            } else {
                // Display error message if SQL input is empty
                JOptionPane.showMessageDialog(panel_manager.get_content_panel(), "Please enter SQL input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 2;
        sql_input_form.add(submit_button, gbc);

        sql_input_panel.add(sql_input_form, BorderLayout.CENTER);

        // Add the SQL input panel to the content panel and show it
        panel_manager.get_content_panel().add(sql_input_panel, "sql_input_panel");

        // Show the new panel
        panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "sql_input_panel");
    }


}
