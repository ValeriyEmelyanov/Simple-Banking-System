import java.math.BigInteger;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        String[] parts = scanner.nextLine().split("[\\u00A0\\s]+");
        scanner.close();

        BigInteger a = new BigInteger(parts[0]);
        BigInteger b = new BigInteger(parts[1]);
        BigInteger c = new BigInteger(parts[2]);
        BigInteger d = new BigInteger(parts[3]);

        BigInteger result = a.negate().multiply(b).add(c).subtract(d);

        System.out.println(result);
    }
}