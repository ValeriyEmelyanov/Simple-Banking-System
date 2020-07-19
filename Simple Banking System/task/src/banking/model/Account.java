package banking.model;

public class Account {
    private long cardNumber;
    private int pin;

    public Account(long cardNumber, int pin) {
        this.cardNumber = cardNumber;
        this.pin = pin;
    }

    public long getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(int cardNumber) {
        this.cardNumber = cardNumber;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }
}
