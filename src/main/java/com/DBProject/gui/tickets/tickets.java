package com.DBProject.gui.tickets;

import javax.swing.*;

public class tickets {
    public static void show_available_tickets() {
        JFrame frame = new JFrame("Available Tickets");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setVisible(true);

        JLabel title = new JLabel("Available Tickets", SwingConstants.CENTER);
        title.setFont(title.getFont().deriveFont(16.0f));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(title);

    }
}
