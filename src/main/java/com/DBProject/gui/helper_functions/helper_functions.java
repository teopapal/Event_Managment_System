package com.DBProject.gui.helper_functions;


import javax.swing.*;
import java.awt.*;

public class helper_functions {
    public static JLabel create_label(String text) {
        JLabel label = new JLabel(text);
        label.setForeground(Color.WHITE);
        return label;
    }

    public static JTextField create_field() {
        JTextField text_field = new JTextField();
        text_field.setBackground(Color.GRAY);
        text_field.setForeground(Color.WHITE);
        text_field.setCaretColor(Color.WHITE);
        text_field.setFont(new Font("Arial", Font.BOLD, 12));
        text_field.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        text_field.setPreferredSize(new Dimension(100, 20));
        return text_field;
    }

    public static JButton create_button(String text) {
        JButton button = new JButton(text);
        button.setPreferredSize(new Dimension(150, 30));
        button.setBackground(Color.DARK_GRAY);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setBorder(BorderFactory.createLineBorder(Color.GRAY));
        return button;
    }
}