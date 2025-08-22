import java.util.Scanner;

public class Duke {
    public static void main(String[] args) {
        String logo = " _____ ____  ___ _____ _   _ \n"
                + "|  ___|  _ \\|_ _|_   _| | | |\n"
                + "| |_  | | | || |  | | | |_| |\n"
                + "|  _| | |_| || |  | | |  _  |\n"
                + "|____ |____/|___| |_| |_| |_|\n";
        System.out.println("Hello from\n" + logo);

        Task[] listOfItems = new Task[100];


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
            } else if (input.matches("mark \\d+")) {
                String[] split = input.split(" ");
                int taskNum = Integer.parseInt(split[1]);
                if (taskNum >= 1 && taskNum <= index) {
                    listOfItems[taskNum - 1].markAsDone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" Nice! I've marked this task as done:");
                    System.out.println("   " + listOfItems[taskNum - 1]);
                    System.out.println("____________________________________________________________");
                } else {
                    printInvalidTaskNumberError();
                }
                continue;
            } else if (input.matches("unmark \\d+")) {
                String[] parts = input.split(" ");
                int taskNum = Integer.parseInt(parts[1]);
                if (taskNum >= 1 && taskNum <= index) {
                    listOfItems[taskNum - 1].markAsUndone();
                    System.out.println("____________________________________________________________");
                    System.out.println(" OK, I've marked this task as not done yet:");
                    System.out.println("   " + listOfItems[taskNum - 1]);
                    System.out.println("____________________________________________________________");
                } else {
                    printInvalidTaskNumberError();
                }
                continue;
            }
            else if (input.equalsIgnoreCase("bye")) {
                break;
            }
            listOfItems[index] = new Task(input);
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

    private static void printArrayOfItems(Task[] items) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < items.length; i++) {
            if (items[i] != null) {
                System.out.println(" " + (i + 1) + "." + items[i]);
            }
        }
    }
    
    private static void printInvalidTaskNumberError() {
        System.out.println("____________________________________________________________");
        System.out.println(" Invalid task number!");
        System.out.println("____________________________________________________________");
    }
}
