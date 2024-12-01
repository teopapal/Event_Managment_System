package src.gui;

import javax.swing.*;
import java.awt.*;

import static src.gui.client.client.registration_form;

public class Gui_hub {

    public Gui_hub() {
        JFrame main_frame = new JFrame("Main Menu");
        main_frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        main_frame.setSize(400, 200);
        main_frame.setLayout(new BorderLayout());


        JLabel welcome = new JLabel("Welcome to HY-360 Project", SwingConstants.CENTER);
        welcome.setFont(new Font("Arial", Font.BOLD, 16));
        main_frame.add(welcome, BorderLayout.CENTER);


        JButton register_button = new JButton("Register User");
        main_frame.add(register_button, BorderLayout.SOUTH);


        register_button.addActionListener(_ -> registration_form());

        main_frame.setVisible(true);
    }



}
