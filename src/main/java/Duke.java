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


        printMessages(
            " Hello! I'm E.D.I.T.H.",
            " What can I do for you?"
        );
        
        Scanner scanner = new Scanner(System.in);
        int index = 0;


        while (true) {
            String input = scanner.nextLine();
            if (input.equalsIgnoreCase("list")) {
                printLineSeparator();
                printArrayOfItems(listOfItems);
                printLineSeparator();
            } else if (input.matches("mark \\d+")) {
                String[] split = input.split(" ");
                int taskNum = Integer.parseInt(split[1]);
                if (taskNum >= 1 && taskNum <= index) {
                    listOfItems[taskNum - 1].markAsDone();
                    printMessages(
                        " Nice! I've marked this task as done:",
                        "   " + listOfItems[taskNum - 1]
                    );
                } else {
                    printInvalidTaskNumberError();
                }
            } else if (input.matches("unmark \\d+")) {
                String[] parts = input.split(" ");
                int taskNum = Integer.parseInt(parts[1]);
                if (taskNum >= 1 && taskNum <= index) {
                    listOfItems[taskNum - 1].markAsUndone();
                    printMessages(
                        " OK, I've marked this task as not done yet:",
                        "   " + listOfItems[taskNum - 1]
                    );
                } else {
                    printInvalidTaskNumberError();
                }
            } else if (input.matches("todo .+")) {
                String description = input.substring(5);
                listOfItems[index] = new Todo(description);
                index += 1;
                printTaskAddedMessage(listOfItems[index - 1], index);
            } else if (input.matches("deadline .+ /by .+")) {
                String[] parts = input.split(" /by ");
                String description = parts[0].substring(9);
                String by = parts[1];
                listOfItems[index] = new Deadline(description, by);
                index += 1;
                printTaskAddedMessage(listOfItems[index - 1], index);
            } else if (input.matches("event .+ /from .+ /to .+")) {
                String[] fromSplit = input.split(" /from ");
                String description = fromSplit[0].substring(6);
                String[] toSplit = fromSplit[1].split(" /to ");
                String from = toSplit[0];
                String to = toSplit[1];
                listOfItems[index] = new Event(description, from, to);
                index += 1;
                printTaskAddedMessage(listOfItems[index - 1], index);
            } else if (input.equalsIgnoreCase("bye")) {
                break;
            } else {
                printMessage(" I don't understand that command!");
            }
        }

        printMessage(" Bye. Hope to see you again soon!");

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
    
    private static void printLineSeparator() {
        System.out.println("____________________________________________________________");
    }
    
    private static void printMessage(String message) {
        printLineSeparator();
        System.out.println(message);
        printLineSeparator();
    }
    
    private static void printMessages(String... messages) {
        printLineSeparator();
        for (String message : messages) {
            System.out.println(message);
        }
        printLineSeparator();
    }
    
    private static void printInvalidTaskNumberError() {
        printMessage(" Invalid task number!");
    }
    
    private static void printTaskAddedMessage(Task task, int taskCount) {
        printMessages(
            " Got it. I've added this task:",
            "   " + task,
            " Now you have " + taskCount + " tasks in the list."
        );
    }
}
