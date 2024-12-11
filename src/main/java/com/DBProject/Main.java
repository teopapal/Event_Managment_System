package com.DBProject;

import com.DBProject.data.DBManager;
import com.DBProject.gui.Gui_hub;

import static com.DBProject.gui.enums.Seat_type.*;


public class Main {

    public static void main(String[] args) {
        DBManager.initialize();


        //DBManager.addTickets(1, "VIP", 100.0, 100);
        //DBManager.addReservation("VIP", 2, 1, 50);
        //DBManager.cancelReservation(1,true);

        DBManager.addTickets(1, Regular, 100.0, 100);
//        DBManager.addReservation(VIP, 2, 1, 50);
//        DBManager.cancelReservation(1,true);

        new Gui_hub();


//        DBManager.cancelEvent(1);
    }
}