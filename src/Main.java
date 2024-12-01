import data.DBManager;

public class Main {

    public static void main(String[] args) {
        DBManager.initialize();

//        DBManager.registerCustomer("koko fsef", "lala.test@fefef.gr", "fefefsf4sefs");
//        DBManager.createEvent("Concert", "2021-12-12", "20:00", "Concert", 1000);
//        DBManager.addTickets(1, "VIP", 100.0, 100);
//        DBManager.addReservation("VIP", 2, 1, 50);
//        DBManager.cancelReservation(1,true);

        DBManager.cancelEvent(1);
    }
}