import java.math.BigInteger;
import java.util.Scanner;

public class Main {

    /**
     * It returns true if at least two of three given numbers are equal, otherwise - false.
     */
    public static boolean atLeastTwoAreEqual(BigInteger num1, BigInteger num2, BigInteger num3) {
        if (num1 != null && num1.equals(num2)) {
            return true;
        }
        if (num2 != null && num2.equals(num3)) {
            return true;
        }
        return num3 != null && num3.equals(num1);
    }

    /* Do not change code below */
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);

        final String[] parts = scanner.nextLine().split("\\s+");

        BigInteger num1 = null;
        BigInteger num2 = null;
        BigInteger num3 = null;

        try {
            num1 = new BigInteger(parts[0]);
            num2 = new BigInteger(parts[1]);
            num3 = new BigInteger(parts[2]);
        } catch (Exception e) {
            System.out.println("Can't parse a big integer value");
            e.printStackTrace();
        }

        System.out.println(atLeastTwoAreEqual(num1, num2, num3) ? "YES" : "NO");
    }
}