package com.DBProject.gui.records;

import com.DBProject.gui.enums.Seat_type;

public record Ticket(String event_name, Seat_type seat_type) {
}
