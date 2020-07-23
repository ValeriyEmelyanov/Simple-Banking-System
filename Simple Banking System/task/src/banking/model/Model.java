package banking.model;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class Model {
    private static final int INN = 400_000;
    private static final int CARD_NUMBER_RANGE = 999_999_999;
    private static final int PIN_RANGE = 9_999;

    private final Map<String, Account> accounts = new HashMap<>();
    private final Random random = new Random();

    public Account createAccount() {
        String cardNumber;
        do {
            long num = INN * 1_000_000_000L +
                    random.nextInt(CARD_NUMBER_RANGE + 1);
            int checksum = getChecksum(num);
            cardNumber = String.valueOf(num * 10L + checksum);
        } while (accounts.containsKey(cardNumber));

        String pin = String.valueOf(random.nextInt(PIN_RANGE + 1));
        Account account = new Account(cardNumber, pin);
        accounts.put(cardNumber, account);

        return account;
    }

    private int getChecksum(long cardNumber) {
        int[] digits = convertCardNumberToDigitsArray(cardNumber);

        // Luhn Algorithm in action

        // Drop the last digit - already without the last digit

        // Multiply odd digits by 2
        for (int i = 0; i < digits.length; i++) {
            if ((i + 1) % 2 == 1) {
                digits[i] *= 2;
            }
        }

        // Substract 9 to numbers over 9
        for (int i = 0; i < digits.length; i++) {
            if (digits[i] > 9) {
                digits[i] -= 9;
            }
        }

        // Add all numbers -> get checksum
        int sum = Arrays.stream(digits).sum();
        return (10 - sum % 10) % 10;
    }

    private int[] convertCardNumberToDigitsArray(long cardNumber) {
        final int len = 15;
        int[] digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[len - 1 - i] = (int) (cardNumber % 10L);
            cardNumber /= 10;
        }
        return digits;
    }

    public Account login(String cardNumber, String pin) {
        Account account = accounts.get(cardNumber);
        if (account == null) {
            return null;
        }

        if (account.getPin().equals(pin)) {
            return account;
        }

        return null;
    }
}
