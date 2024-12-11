package com.DBProject.gui.records;

import com.DBProject.gui.enums.Seat_type;

import java.awt.*;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;

public record Ticket(String event_name, Seat_type seat_type) implements Printable {
    @Override
    public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) throws PrinterException {
        return 0;
    }
}
