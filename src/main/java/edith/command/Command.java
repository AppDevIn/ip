package edith.command;

import java.io.IOException;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;

/**
 * Abstract base class for all command types in the Edith application.
 * Implements the Command pattern to encapsulate different user actions.
 */
public abstract class Command {
    /**
     * Executes the command with the provided task list, user interface, and storage.
     * Each concrete command implements this method to perform its specific action.
     * 
     * @param tasks the task list to operate on
     * @param ui the user interface for displaying messages
     * @param storage the storage system for saving/loading data
     * @throws EdithException if an error occurs during command execution
     */
    public abstract void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException;
    
    /**
     * Checks if this command should terminate the application.
     * 
     * @return true if the command should exit the application, false otherwise
     */
    public boolean isExit() {
        return false;
    }

    /**
     * Saves all tasks to file using the provided storage system.
     * Shows an error message through the UI if saving fails.
     *
     * @param tasks the task list to save
     * @param ui the user interface for displaying error messages
     * @param storage the storage system to use for saving
     */
    protected void saveTasksToFile(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasksToFile(tasks.getList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}