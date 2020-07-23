package banking.controller;

import banking.model.Account;
import banking.view.ConsoleView;
import banking.model.Model;

import java.util.Scanner;

public class Controller {
    private final Scanner scanner = new Scanner(System.in);
    private final ConsoleView view = new ConsoleView();
    private final Model model = new Model();
    private Account curAccount = null;

    public void run() {
        while (true) {
            view.menu(curAccount != null);
            String choice = scanner.nextLine();

            if ("0".equals(choice)) {
                view.message("\nBye!");
                scanner.close();
                return;
            }

            if (curAccount == null) {
                switch (choice) {
                    case "1":
                        createAccount();
                        break;
                    case "2":
                        login();
                        break;
                    default:
                        view.message("\nInvalid input. Try again\n");
                }
            } else {
                switch (choice) {
                    case "1":
                        balance();
                        break;
                    case "2":
                        logout();
                        break;
                    default:
                        view.message("\nInvalid input. Try again\n");
                }
            }
        }
    }

    private void createAccount() {
        Account newAccount = model.createAccount();
        view.message(String.format("\n" +
                        "Your card has been created\n" +
                        "Your card number:\n%s\n" +
                        "Your card PIN:\n%s\n",
                newAccount.getCardNumber(),
                newAccount.getPin()));
    }

    private void login() {
        view.message("\nEnter your card number:");
        String cardNumber = scanner.nextLine();
        view.message("Enter your PIN:");
        String pin = scanner.nextLine();
        curAccount = model.login(cardNumber, pin);
        if (curAccount == null) {
            view.message("\nWrong card number or PIN!\n");
        } else {
            view.message("\nYou have successfully logged in!\n");
        }
    }

    private void balance() {
        view.message(String.format("\nBalance: %d\n", curAccount.getBalance()));
    }

    private void logout() {
        curAccount = null;
        view.message("\nYou have successfully logged out!\n");
    }
}
