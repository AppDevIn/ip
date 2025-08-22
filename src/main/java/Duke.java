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
            try {
                String input = scanner.nextLine();
                if (input.equalsIgnoreCase("list")) {
                    printLineSeparator();
                    printArrayOfItems(listOfItems);
                    printLineSeparator();
                } else if (input.startsWith("mark ")) {
                    validateTaskNumber(input, index);
                    String[] split = input.split(" ");
                    int taskNum = Integer.parseInt(split[1]);
                    listOfItems[taskNum - 1].markAsDone();
                    printMessages(
                        " Nice! I've marked this task as done:",
                        "   " + listOfItems[taskNum - 1]
                    );
                } else if (input.startsWith("unmark ")) {
                    validateTaskNumber(input, index);
                    String[] parts = input.split(" ");
                    int taskNum = Integer.parseInt(parts[1]);
                    listOfItems[taskNum - 1].markAsUndone();
                    printMessages(
                        " OK, I've marked this task as not done yet:",
                        "   " + listOfItems[taskNum - 1]
                    );
                } else if (input.startsWith("todo")) {
                    validateTodoInput(input);
                    String description = input.substring(5);
                    listOfItems[index] = new Todo(description);
                    index += 1;
                    printTaskAddedMessage(listOfItems[index - 1], index);
                } else if (input.startsWith("deadline")) {
                    validateDeadlineInput(input);
                    String[] parts = input.split(" /by ");
                    String description = parts[0].substring(9);
                    String by = parts[1];
                    listOfItems[index] = new Deadline(description, by);
                    index += 1;
                    printTaskAddedMessage(listOfItems[index - 1], index);
                } else if (input.startsWith("event")) {
                    validateEventInput(input);
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
                    throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that means :-( Try: todo, deadline, event, list, mark, unmark, bye");
                }
            } catch (DukeException e) {
                printMessage(" " + e.getMessage());
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
    
    
    private static void printTaskAddedMessage(Task task, int taskCount) {
        printMessages(
            " Got it. I've added this task:",
            "   " + task,
            " Now you have " + taskCount + " tasks in the list."
        );
    }
    
    private static void validateTodoInput(String input) throws TodoException {
        if (input.equals("todo") || input.trim().equals("todo")) {
            throw new TodoException("OOPS!!! The description of a todo cannot be empty.");
        }
    }
    
    private static void validateDeadlineInput(String input) throws DeadlineException {
        if (input.equals("deadline") || input.trim().equals("deadline")) {
            throw new DeadlineException("OOPS!!! The description of a deadline cannot be empty.");
        }

        if (!input.contains(" /by ")) {
            throw new DeadlineException("OOPS!!! Deadline format should be: deadline <description> /by <time>");
        }

        String[] parts = input.split(" /by ");
        if (parts.length != 2 || parts[1].trim().isEmpty()) {
            throw new DeadlineException("OOPS!!! The deadline time cannot be empty.");
        }
    }
    
    private static void validateEventInput(String input) throws EventException {
        if (input.equals("event") || input.trim().equals("event")) {
            throw new EventException("OOPS!!! The description of an event cannot be empty.");
        }

        if (!input.contains(" /from ") || !input.contains(" /to ")) {
            throw new EventException("OOPS!!! Event format should be: event <description> /from <start> /to <end>");
        }

        String[] fromSplit = input.split(" /from ");
        if (fromSplit.length != 2) {
            throw new EventException("OOPS!!! Event format should be: event <description> /from <start> /to <end>");
        }

        String[] toSplit = fromSplit[1].split(" /to ");
        if (toSplit.length != 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new EventException("OOPS!!! Event times cannot be empty.");
        }
    }
    
    private static void validateTaskNumber(String input, int maxTasks) throws InvalidTaskNumberException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new InvalidTaskNumberException("OOPS!!! Please provide a task number.");
        }

        try {
            int taskNum = Integer.parseInt(parts[1]);
            if (taskNum < 1 || taskNum > maxTasks) {
                throw new InvalidTaskNumberException("OOPS!!! Task number " + taskNum + " is out of range. You have " + maxTasks + " tasks.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("OOPS!!! Task number must be a valid number.");
        }
    }
}
