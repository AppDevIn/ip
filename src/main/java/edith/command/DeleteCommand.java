package edith.command;

import edith.task.Task;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;


/**
 * Command for deleting a task from the task list.
 * Removes the specified task and shows confirmation to the user.
 */
public class DeleteCommand extends Command {
    private String input;
    
    /**
     * Creates a new DeleteCommand with the given input string.
     * The input should contain the task number to delete.
     *
     * @param input the command input containing the task number
     */
    public DeleteCommand(String input) {
        this.input = input;
    }
    
    /**
     * Executes the delete command by removing the specified task from the task list.
     * Displays confirmation message showing the deleted task and updated count.
     *
     * @param tasks the task list to delete from
     * @param ui the user interface for displaying messages
     * @param storage the storage system for saving changes
     * @throws EdithException if the task number is invalid or deletion fails
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "UI cannot be null";
        assert storage != null : "Storage cannot be null";
        
        String[] deleteParts = input.split(" ");

        if (deleteParts.length < 2) {
            throw new EdithException("OOPS!!! Please provide a task number to delete.");
        }

        int deleteNum;
        try {
            deleteNum = Integer.parseInt(deleteParts[1]);
        } catch (NumberFormatException e) {
            throw new EdithException("OOPS!!! Task number must be a valid number.");
        }

        if (deleteNum < 1 || deleteNum > tasks.size()) {
            throw new EdithException("OOPS!!! Task number " + deleteNum + " is out of range. "
                    + "Valid range: 1 to " + tasks.size());
        }
        int originalSize = tasks.size();
        Task removedTask = tasks.delete(deleteNum - 1);
        
        assert removedTask != null : "Removed task should not be null";
        assert tasks.size() == originalSize - 1 : "Task list size should decrease by 1 after deletion";
        
        ui.showMessages(
                " Noted. I've removed this task:",
                "   " + removedTask,
                " Now you have " + tasks.size() + " tasks in the list."
        );
        saveTasksToFile(tasks, ui, storage);
    }
}
