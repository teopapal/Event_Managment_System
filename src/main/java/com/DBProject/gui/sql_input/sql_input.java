package com.DBProject.gui.sql_input;

import com.DBProject.data.DBManager;
import com.DBProject.gui.PanelManager.PanelManager;

import javax.swing.*;
import java.awt.*;

import static com.DBProject.gui.helper_functions.helper_functions.create_button;

public class sql_input {
    public static void create_sql_input(PanelManager panel_manager) {
        JPanel sql_input_panel = new JPanel(new BorderLayout());
        sql_input_panel.setBackground(Color.DARK_GRAY);

        JPanel top_panel = new JPanel(new BorderLayout());
        top_panel.setBackground(Color.DARK_GRAY);

        JButton back_button = create_button("Back");
        back_button.setPreferredSize(new Dimension(60, 30));
        back_button.addActionListener(_ -> {
            panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "main_panel");
        });
        top_panel.add(back_button, BorderLayout.WEST);

        JLabel title = new JLabel("SQL Input", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setForeground(Color.WHITE);
        top_panel.add(title, BorderLayout.CENTER);

        sql_input_panel.add(top_panel, BorderLayout.NORTH);

        JPanel sql_input_form = new JPanel(new GridBagLayout());
        sql_input_form.setBackground(Color.DARK_GRAY);
        sql_input_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);

        JTextArea sql_input = new JTextArea(15, 55);
        sql_input.setBackground(new Color(60, 60, 60));
        sql_input.setForeground(Color.WHITE);
        sql_input.setCaretColor(Color.WHITE);
        sql_input.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        sql_input.setFont(new Font("Arial", Font.BOLD, 14));

        gbc.gridx = 0;
        gbc.gridy = 0;
        sql_input_form.add(new JScrollPane(sql_input), gbc);

        sql_input_panel.add(sql_input_form, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new FlowLayout());
        bottom_panel.setBackground(Color.DARK_GRAY);

        JButton submit_button = create_button("Submit");

        submit_button.addActionListener(_ -> {
            String sql = sql_input.getText();
            if (!sql.isEmpty()) {
                System.out.println("SQL Input: " + sql);
                Object text = DBManager.executeQuery(sql);
                JOptionPane.showMessageDialog(panel_manager.get_content_panel(), text, "SQL Output", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(panel_manager.get_content_panel(), "Please enter SQL input!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });

        bottom_panel.add(submit_button);
        sql_input_panel.add(bottom_panel, BorderLayout.SOUTH);

        JScrollPane scroll_bar = new JScrollPane(sql_input_form);
        scroll_bar.setBackground(Color.DARK_GRAY);
        scroll_bar.getViewport().setBackground(Color.DARK_GRAY);
        scroll_bar.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scroll_bar.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scroll_bar.getVerticalScrollBar().setUnitIncrement(16);
        scroll_bar.setBorder(null);

        sql_input_panel.add(scroll_bar, BorderLayout.CENTER);

        panel_manager.get_content_panel().add(sql_input_panel, "sql_input_panel");
        panel_manager.get_card_layout().show(panel_manager.get_content_panel(), "sql_input_panel");
    }
}
