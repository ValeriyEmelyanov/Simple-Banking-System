package banking.controller;

import banking.model.Account;
import banking.view.ConsoleView;
import banking.model.Model;

import java.util.Scanner;

public class Controller {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleView view = new ConsoleView();
    private final Model model = new Model();
    private boolean logged = false;

    public void run() {
        while (true) {
            view.menu(logged);
            String choice = scanner.nextLine();

            if ("0".equals(choice)) {
                view.message("\nBye!");
                scanner.close();
                return;
            }

            if (logged) {
                switch (choice) {
                    case "1":
                        balance();
                        break;
                    case "2":
                        logout();
                        break;
                    default:
                        view.message("Invalid input. Try again");
                }
            } else {
                switch (choice) {
                    case "1":
                        createAccount();
                        break;
                    case "2":
                        login();
                        break;
                    default:
                        view.message("Invalid input. Try again");
                }
            }
        }
    }

    private void createAccount() {
        Account newAccount = model.createAccount();
        view.message(String.format("\n" +
                        "Your card has been created\n" +
                        "Your card number:\n%d\n" +
                        "Your card PIN:\n%d\n",
                newAccount.getCardNumber(),
                newAccount.getPin()));
    }

    private void login() {
        view.message("\nEnter your card number:");
        long cardNumber = Long.parseLong(scanner.nextLine());
        view.message("Enter your PIN:");
        int pin = Integer.parseInt(scanner.nextLine());
        Account account = model.login(cardNumber, pin);
        if (account == null) {
            view.message("\nWrong card number or PIN!\n");
        } else {
            logged = true;
            view.message("\nYou have successfully logged in!\n");
        }
    }

    private void balance() {
        view.message("\nBalance: 0\n");
    }

    private void logout() {
        logged = false;
        view.message("\nYou have successfully logged out!\n");
    }
}
