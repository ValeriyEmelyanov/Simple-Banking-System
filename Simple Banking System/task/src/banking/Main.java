package banking;

import banking.controller.Controller;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Database filename should be passed to the program" +
                    "using -fileName argument, for example, -fileName db.s3db");
            return;
        }

        String filename = null;
        if ("-fileName".equals(args[0])) {
            filename = args[1];
            Path pathToDb = Path.of(filename);
            if (!Files.exists(pathToDb)) {
                System.out.printf("File %s does not exist!\n", filename);
                return;
            }
        } else {
            System.out.println("Database filename not specified!");
            return;
        }

        Controller controller = new Controller(filename);
        controller.run();
    }
}
