package edith.ui;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.IntStream;
import edith.task.Task;

/**
 * Handles all user interface interactions for the Edith application.
 * Takes care of displaying messages, reading input, and formatting output nicely.
 */
public class Ui {
    private Scanner scanner;

    /**
     * Sets up the UI with a scanner ready to read user input.
     */
    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    /**
     * Displays the welcome message with the fancy ASCII art logo.
     * Because every good chatbot needs a dramatic entrance.
     */
    public void showWelcome() {
        String logo = " _____ ____  ___ _____ _   _ \n"
                + "|  ___|  _ \\|_ _|_   _| | | |\n"
                + "| |_  | | | || |  | | | |_| |\n"
                + "|  _| | |_| || |  | | |  _  |\n"
                + "|____ |____/|___| |_| |_| |_|\n";
        System.out.println("Hello from\n" + logo);

        showMessages(
                " E.D.I.T.H. systems online.",
                " How may I assist you today?"
        );
    }

    /**
     * Reads whatever the user types in as their next command.
     *
     * @return the user's input as a string
     */
    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    /**
     * Shows a single message wrapped in those nice divider lines.
     *
     * @param message the message to display to the user
     */
    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    /**
     * Shows multiple messages at once, all wrapped in the same divider block.
     * Saves some screen space when you have related things to say.
     *
     * @param messages variable number of messages to display
     */
    public void showMessages(String... messages) {
        showLine();
        for (String message : messages) {
            System.out.println(message);
        }
        showLine();
    }

    /**
     * Displays all tasks in a numbered list format.
     * Makes it easy for users to see what they've got on their plate.
     *
     * @param items the list of tasks to display
     */
    public void showTaskList(ArrayList<Task> items) {
        System.out.println(" Your current task arsenal:");
        IntStream.range(0, items.size())
                .forEach(i -> System.out.println(" " + (i + 1) + "." + items.get(i)));
    }

    /**
     * Shows the search results from a find command.
     * Displays matching tasks with their original numbering from the task list.
     *
     * @param matchingTasks the list of tasks that match the search criteria
     * @param originalIndices the original indices of the matching tasks in the full list
     */
    public void showFoundTasks(ArrayList<Task> matchingTasks, ArrayList<Integer> originalIndices) {
        if (matchingTasks.isEmpty()) {
            System.out.println(" Scan complete. No matching tasks found.");
        } else {
            System.out.println(" Scan results - matching tasks located:");
            IntStream.range(0, matchingTasks.size())
                    .forEach(i -> System.out.println(" " + originalIndices.get(i) + "." + matchingTasks.get(i)));
        }
    }

    /**
     * Shows confirmation when a task gets added successfully.
     * Includes the task details and updated count so users know what happened.
     *
     * @param task the task that was just added
     * @param taskCount the new total number of tasks
     */
    public void showTaskAdded(Task task, int taskCount) {
        showMessages(
                " Mission accepted. Task added to your arsenal:",
                "   " + task,
                " Total active missions: " + taskCount
        );
    }

    public void showLoadingError() {
        showMessage(" Warning: Unable to access saved data files.");
    }

    /**
     * Displays error messages when things go wrong.
     * Keeps the formatting consistent even for bad news.
     *
     * @param errorMessage the error message to show the user
     */
    public void showError(String errorMessage) {
        showMessage(" " + errorMessage);
    }

    public void showGoodbye() {
        showMessage(" E.D.I.T.H. systems shutting down. Stay safe out there.");
    }

    /**
     * Closes the scanner to clean up resources when the app shuts down.
     * Good practice to avoid resource leaks.
     */
    public void close() {
        scanner.close();
    }
}
