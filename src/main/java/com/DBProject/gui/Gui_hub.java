package com.DBProject.gui;

import javax.swing.*;
import java.awt.*;

import static com.DBProject.gui.client.client.registration_form;
import static com.DBProject.gui.event.event.create_event_form;


public class Gui_hub {

    public Gui_hub() {
        JFrame main_frame = new JFrame("Main Menu");
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setSize(400, 200);
        main_frame.setLayout(new BorderLayout());


        JLabel welcome = new JLabel("Welcome to HY-360 Project", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        main_frame.add(welcome, BorderLayout.CENTER);


        //Button Panel
        JPanel button_panel = new JPanel();
        button_panel.setLayout(new GridLayout(3, 2, 10, 10));
        button_panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        //Buttons
        JButton register_button = new JButton("Register User");
        JButton create_event_button = new JButton("Create Event");


        button_panel.add(register_button);
        button_panel.add(create_event_button);

        main_frame.add(button_panel, BorderLayout.SOUTH);


        register_button.addActionListener(_ -> registration_form());
        create_event_button.addActionListener(_ -> create_event_form());

        main_frame.setVisible(true);
    }
}
