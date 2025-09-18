package edith.ui;

import java.util.ArrayList;
import edith.task.Task;

/**
 * A UI implementation that captures responses for GUI display instead of console output.
 * This class extends the regular UI by collecting messages in a string buffer
 * that can be retrieved for display in the GUI.
 */
public class GuiUi extends Ui {
    private StringBuilder responseBuilder;

    /**
     * Creates a new GuiUi instance.
     */
    public GuiUi() {
        responseBuilder = new StringBuilder();
    }

    /**
     * Gets the collected response messages.
     *
     * @return The accumulated response text
     */
    public String getResponse() {
        return responseBuilder.toString().trim();
    }

    /**
     * Shows a single message by adding it to the response buffer.
     *
     * @param message the message to add to the response
     */
    @Override
    public void showMessage(String message) {
        responseBuilder.append(message).append("\n");
    }

    /**
     * Shows multiple messages by adding them to the response buffer.
     *
     * @param messages variable number of messages to add to the response
     */
    @Override
    public void showMessages(String... messages) {
        for (String message : messages) {
            responseBuilder.append(message).append("\n");
        }
    }

    /**
     * Displays all tasks in a numbered list format in the response buffer.
     *
     * @param items the list of tasks to display
     */
    @Override
    public void showTaskList(ArrayList<Task> items) {
        responseBuilder.append("Your current task arsenal:\n");
        for (int i = 0; i < items.size(); i++) {
            responseBuilder.append((i + 1)).append(".").append(items.get(i)).append("\n");
        }
    }

    /**
     * Shows the search results from a find command in the response buffer.
     *
     * @param matchingTasks the list of tasks that match the search criteria
     * @param originalIndices the original indices of the matching tasks in the full list
     */
    @Override
    public void showFoundTasks(ArrayList<Task> matchingTasks, ArrayList<Integer> originalIndices) {
        if (matchingTasks.isEmpty()) {
            responseBuilder.append("Scan complete. No matching tasks found.\n");
        } else {
            responseBuilder.append("Scan results - matching tasks located:\n");
            for (int i = 0; i < matchingTasks.size(); i++) {
                responseBuilder.append(originalIndices.get(i)).append(".").append(matchingTasks.get(i)).append("\n");
            }
        }
    }

    /**
     * Shows confirmation when a task gets added successfully.
     *
     * @param task the task that was just added
     * @param taskCount the new total number of tasks
     */
    @Override
    public void showTaskAdded(Task task, int taskCount) {
        responseBuilder.append("Mission accepted. Task added to your arsenal:\n");
        responseBuilder.append(task).append("\n");
        responseBuilder.append("Total active missions: ").append(taskCount).append("\n");
    }

    /**
     * Shows an error message in the response buffer.
     *
     * @param errorMessage the error message to show
     */
    @Override
    public void showError(String errorMessage) {
        responseBuilder.append(errorMessage).append("\n");
    }

    /**
     * Shows loading error message.
     */
    @Override
    public void showLoadingError() {
        responseBuilder.append("Warning: Unable to access saved data files.\n");
    }

    /**
     * Shows goodbye message.
     */
    @Override
    public void showGoodbye() {
        responseBuilder.append("E.D.I.T.H. systems shutting down. Stay safe out there.\n");
    }

    @Override
    public String readCommand() {
        return "";
    }

    @Override
    public void close() {
    }

    @Override
    public void showWelcome() {
    }
}
