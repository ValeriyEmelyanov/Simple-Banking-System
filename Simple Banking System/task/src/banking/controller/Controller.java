package banking.controller;

import banking.entities.Account;
import banking.service.AccountService;
import banking.view.ConsoleView;

import java.util.Scanner;

public class Controller {
    private final Scanner scanner;
    private final ConsoleView view;
    private final AccountService service;

    private Account curAccount = null;

    public Controller(String filename) {
        this.scanner = new Scanner(System.in);
        this.view = new ConsoleView();
        this.service = new AccountService(filename);
    }

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
        Account newAccount = service.createAccount();
        if (newAccount == null) {
            view.message("An error occurred when creating the account.");
            return;
        }
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
        curAccount = service.login(cardNumber, pin);
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
