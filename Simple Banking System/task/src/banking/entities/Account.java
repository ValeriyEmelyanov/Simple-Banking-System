package banking.entities;

public class Account {
    private final int id;
    private final String cardNumber;
    private String pin;
    private int balance;

    public Account(int id, String cardNumber, String pin) {
        this.id = id;
        this.cardNumber = cardNumber;
        this.pin = pin;
        this.balance = 0;
    }

    public Account(int id, String cardNumber, String pin, int balance) {
        this(id, cardNumber, pin);
        this.balance = balance;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getPin() {
        return pin;
    }

    public int getBalance() {
        return balance;
    }
}
