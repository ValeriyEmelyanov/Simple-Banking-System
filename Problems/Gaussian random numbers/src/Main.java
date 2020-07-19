import java.util.*;

public class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        String[] strings = scanner.nextLine().split("[\\u00A0\\s]+");
        scanner.close();
        
        final int k = Integer.parseInt(strings[0]);
        final int n = Integer.parseInt(strings[1]);
        final double m = Double.parseDouble(strings[2]);

        int seed = k;
        int count = 10_000_000;
        do {
            Random random = new Random(seed);
            boolean fit = true;
            for (int i = 0; i < n; i++) {
                double num = random.nextGaussian();
                if (num > m) {
                    fit = false;
                    break;
                }
            }
            if (fit) {
                break;
            }
            seed++;
        } while (count-- > 0);

        System.out.println(seed);
    }
}