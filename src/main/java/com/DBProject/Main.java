package com.DBProject;

import com.DBProject.data.DBManager;
import com.DBProject.gui.Gui_hub;


public class Main {

    public static void main(String[] args) {
        DBManager.initialize();
        new Gui_hub();
    }
}