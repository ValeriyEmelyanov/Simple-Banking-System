package banking.model;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Model {
    private static final int INN = 400_000;
    private static final int CARD_NUMBER_RANGE = 999_999_999;
    private static final int PIN_RANGE = 9_999;

    private final Map<Long, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public Account createAccount() {
        int checksum = 1;
        long cardNumber = 0;
        do {
            cardNumber = INN * 10_000_000_000L +
                    random.nextInt(CARD_NUMBER_RANGE + 1) * 10L +
                    checksum;
        } while (accounts.containsKey(cardNumber));

        int pin = random.nextInt(PIN_RANGE + 1);
        Account account = new Account(cardNumber, pin);
        accounts.put(cardNumber, account);

        return account;
    }

    public Account login(long cardNumber, int pin) {
        Account account = accounts.get(cardNumber);
        if (account == null) {
            return null;
        }

        if (account.getPin() == pin) {
            return account;
        }

        return null;
    }
}
