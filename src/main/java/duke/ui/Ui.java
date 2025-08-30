package duke.ui;
import java.util.ArrayList;
import java.util.Scanner;
import duke.task.Task;

public class Ui {
    private Scanner scanner;

    public Ui() {
        this.scanner = new Scanner(System.in);
    }

    public void showWelcome() {
        String logo = " _____ ____  ___ _____ _   _ \n"
                + "|  ___|  _ \\|_ _|_   _| | | |\n"
                + "| |_  | | | || |  | | | |_| |\n"
                + "|  _| | |_| || |  | | |  _  |\n"
                + "|____ |____/|___| |_| |_| |_|\n";
        System.out.println("Hello from\n" + logo);
        
        showMessages(
            " Hello! I'm E.D.I.T.H.",
            " What can I do for you?"
        );
    }

    public String readCommand() {
        return scanner.nextLine();
    }

    public void showLine() {
        System.out.println("____________________________________________________________");
    }

    public void showMessage(String message) {
        showLine();
        System.out.println(message);
        showLine();
    }

    public void showMessages(String... messages) {
        showLine();
        for (String message : messages) {
            System.out.println(message);
        }
        showLine();
    }

    public void showTaskList(ArrayList<Task> items) {
        System.out.println(" Here are the tasks in your list:");
        for (int i = 0; i < items.size(); i++) {
            System.out.println(" " + (i + 1) + "." + items.get(i));
        }
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
            System.out.println(" No matching tasks found.");
        } else {
            System.out.println(" Here are the matching tasks in your list:");
            for (int i = 0; i < matchingTasks.size(); i++) {
                System.out.println(" " + originalIndices.get(i) + "." + matchingTasks.get(i));
            }
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
            " Got it. I've added this task:",
            "   " + task,
            " Now you have " + taskCount + " tasks in the list."
        );
    }

    public void showLoadingError() {
        showMessage(" Could not load saved tasks from file.");
    }

    public void showError(String errorMessage) {
        showMessage(" " + errorMessage);
    }

    public void showGoodbye() {
        showMessage(" Bye. Hope to see you again soon!");
    }

    public void close() {
        scanner.close();
    }
}