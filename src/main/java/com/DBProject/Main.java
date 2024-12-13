package com.DBProject;

import com.DBProject.data.DBManager;
import com.DBProject.gui.Gui_hub;


public class Main {

    public static void main(String[] args) {
        DBManager.initialize();

        new Gui_hub();

        //DBManager.addTickets(1, "VIP", 100.0, 100);
        //DBManager.addReservation("VIP", 2, 1, 50);
        //DBManager.cancelReservation(1,true);

//        DBManager.addReservation(VIP, 2, 1, 50);
//        DBManager.cancelReservation(1,true);



//        DBManager.cancelEvent(1);
    }
}