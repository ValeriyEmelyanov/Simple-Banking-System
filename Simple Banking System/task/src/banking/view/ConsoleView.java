package banking.view;

public class ConsoleView {

    public void menu(boolean logged) {
        String menuNotLogged = "" +
                "1. Create an account\n" +
                "2. Log into account\n" +
                "0. Exit";
        String menuLogged = "" +
                "1. Balance\n" +
                "2. Add income\n" +
                "3. Do transfer\n" +
                "4. Close account\n" +
                "5. Log out\n" +
                "0. Exit";
        System.out.println(logged ? menuLogged : menuNotLogged);
    }

    public void message(String msg) {
        System.out.println(msg);
    }
}
