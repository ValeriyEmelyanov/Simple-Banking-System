import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        final Scanner scanner = new Scanner(System.in);
        String input = scanner.nextLine();
        scanner.close();

        String reversed = new StringBuilder(input).reverse().toString();

        System.out.println(input.equals(reversed) ? "yes" : "no");
    }
}