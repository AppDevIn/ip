import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String logo = " _____ ____  ___ _____ _   _ \n"
                + "|  ___|  _ \\|_ _|_   _| | | |\n"
                + "| |_  | | | || |  | | | |_| |\n"
                + "|  _| | |_| || |  | | |  _  |\n"
                + "|____ |____/|___| |_| |_| |_|\n";
        System.out.println("Hello from\n" + logo);

        String[] listOfItems = new String[100];


        System.out.println("____________________________________________________________");
        System.out.println(" Hello! I'm E.D.I.T.H.");
        System.out.println(" What can I do for you?");
        System.out.println("____________________________________________________________");
        
        Scanner scanner = new Scanner(System.in);
        int index = 0;


        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("list")) {
                System.out.println("____________________________________________________________");
                printArrayOfItems(listOfItems);
                System.out.println("____________________________________________________________");
                continue;
            } else if (input.equalsIgnoreCase("bye")) {
                break;
            }
            listOfItems[index] = input;
            index += 1;
            System.out.println("____________________________________________________________");
            System.out.println(" added: " + input);
            System.out.println("____________________________________________________________");
        }

        System.out.println("____________________________________________________________");
        System.out.println(" Bye. Hope to see you again soon!");
        System.out.println("____________________________________________________________");

        scanner.close();
    }

    private static void printArrayOfItems(String[] items) {
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                System.out.println(" " + (i + 1) + ". " + items[i]);
            }
        }
    }
}
