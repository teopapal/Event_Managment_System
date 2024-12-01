package src.gui.event;

import src.gui.records.Event;
import src.gui.records.Event_type;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class event {
    public static void create_event_form() {
        ArrayList<Event> events = new ArrayList<>();

        JFrame frame = new JFrame("Event Creation");
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.setSize(400, 300);
        frame.setLayout(new BorderLayout());

        JLabel title = new JLabel("Create a new Event", SwingConstants.CENTER);
        title.setFont(new Font("Arial", Font.BOLD, 16));
        title.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        frame.add(title, BorderLayout.NORTH);

        JPanel event_form = new JPanel(new GridBagLayout());
        event_form.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.insets = new Insets(5, 5, 5, 5);


        JLabel event_name_Label = new JLabel("Event Name:");
        JTextField event_name = new JTextField();

        JLabel event_date_Label = new JLabel("Event Date:");
        JTextField event_date = new JTextField();

        JLabel event_time_Label = new JLabel("Event Time:");
        JTextField event_time = new JTextField();

        JLabel event_type_Label = new JLabel("Event Type:");
        JComboBox<Event_type> event_type_box = new JComboBox<>(Event_type.values());
        event_type_box.addActionListener(_ -> event_type_box.getSelectedItem());

        JLabel event_capacity_Label = new JLabel("Event Capacity:");
        JTextField event_capacity = new JTextField();

        gbc.gridx = 0;
        gbc.gridy = 0;
        event_form.add(event_name_Label, gbc);
        gbc.gridx = 1;
        event_form.add(event_name, gbc);

        gbc.gridx = 0;
        gbc.gridy = 1;
        event_form.add(event_date_Label, gbc);
        gbc.gridx = 1;
        event_form.add(event_date, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        event_form.add(event_time_Label, gbc);
        gbc.gridx = 1;
        event_form.add(event_time, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        event_form.add(event_type_Label, gbc);
        gbc.gridx = 1;
        event_form.add(event_type_box, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        event_form.add(event_capacity_Label, gbc);
        gbc.gridx = 1;
        event_form.add(event_capacity, gbc);

        frame.add(event_form, BorderLayout.CENTER);

        JPanel bottom_panel = new JPanel(new BorderLayout());
        JButton submit_button = new JButton("Create Event");
        JLabel message = new JLabel("", SwingConstants.CENTER);

        bottom_panel.add(submit_button, BorderLayout.CENTER);
        bottom_panel.add(message, BorderLayout.SOUTH);

        frame.add(bottom_panel, BorderLayout.SOUTH);

        // ACTION LISTENER
        submit_button.addActionListener(_ -> {
            if (event_name.getText().isEmpty() || event_date.getText().isEmpty() || event_time.getText().isEmpty() || event_capacity.getText().isEmpty()) {
                message.setText("All fields are required!");
                message.setForeground(Color.RED);
            } else {
                message.setText("Event creation successful!");
                message.setForeground(Color.GREEN);

                System.out.println("Event Details:");
                System.out.println("Event Name: " + event_name.getText());
                System.out.println("Event Date: " + event_date.getText());
                System.out.println("Event Time: " + event_time.getText());
                System.out.println("Event Type: " + event_type_box.getSelectedItem());
                System.out.println("Event Capacity: " + event_capacity.getText());

                Event event = new Event(event_name.getText(), Integer.parseInt(event_date.getText()), Integer.parseInt(event_time.getText()), (Event_type) event_type_box.getSelectedItem(), Integer.parseInt(event_capacity.getText()));

                events.add(event);

                System.out.println(events);
            }
        });

        frame.setVisible(true);

    }
}
