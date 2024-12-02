package com.DBProject.gui.records;

import com.DBProject.gui.event.Event_type;

import java.sql.Date;
import java.sql.Time;

public record Event(String name, Event_type type, Date date, Time time, int capacity) {
}
