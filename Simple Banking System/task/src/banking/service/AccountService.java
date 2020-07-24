package banking.service;

import banking.dao.AccountDao;
import banking.entities.Account;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.Random;

public class AccountService {
    private static final int INN = 400_000;
    private static final int CARD_NUMBER_RANGE = 999_999_999;
    private static final int PIN_RANGE = 9_999;

    private final Random random = new Random();
    private final AccountDao dao;

    public AccountService(String filename) {
        dao = new AccountDao(filename);
    }

    public Account createAccount() {
        String cardNumber = null;
        boolean exit = false;
        while (!exit) {
            long num = INN * 1_000_000_000L +
                    random.nextInt(CARD_NUMBER_RANGE + 1);
            int checksum = getChecksum(num);
            cardNumber = String.valueOf(num * 10L + checksum);

            try {
                if (dao.checkCardNumber(cardNumber)) {
                    exit = true;
                }
            } catch (SQLException throwables) {
                throwables.printStackTrace();
                return null;
            }
        }

        String pin = String.format("%04d", random.nextInt(PIN_RANGE + 1));

        Account account = null;
        try {
            account = dao.create(cardNumber, pin);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        return account;
    }

    private int getChecksum(long number) {
        int[] digits = convertNumberToDigitsArray(number);

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

    private int[] convertNumberToDigitsArray(long number) {
        final int len = 15;
        int[] digits = new int[len];
        for (int i = 0; i < len; i++) {
            digits[len - 1 - i] = (int) (number % 10L);
            number /= 10;
        }
        return digits;
    }

    public Account login(String cardNumber, String pin) {
        Account account = null;
        try {
            account = dao.getByCardnumber(cardNumber);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            return null;
        }

        if (account != null && account.getPin().equals(pin)) {
            return account;
        }
        return null;
    }
}
