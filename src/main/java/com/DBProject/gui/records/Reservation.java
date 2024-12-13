package com.DBProject.gui.records;

import com.DBProject.gui.enums.Seat_type;

public record Reservation(Seat_type seat_type, int customer_id, String event_name, int number_of_tickets) {
}
