package edith.command;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;


/**
 * Command for marking a task as completed.
 * Takes a task number and flips that task's status to done.
 */
public class MarkCommand extends Command {
    private String input;
    
    /**
     * Creates a mark command from the user's input.
     * 
     * @param input the full command string like "mark 3"
     */
    public MarkCommand(String input) {
        this.input = input;
    }
    
    /**
     * Marks the specified task as done and shows confirmation.
     * Converts from 1-based user numbering to 0-based array indexing.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        String[] split = input.split(" ");

        if (split.length < 2) {
            throw new EdithException("OOPS!!! Please provide a task number to mark.");
        }

        int taskNum;
        try {
            taskNum = Integer.parseInt(split[1]);
        } catch (NumberFormatException e) {
            throw new EdithException("OOPS!!! Task number must be a valid number.");
        }

        if (taskNum < 1 || taskNum > tasks.size()) {
            throw new EdithException("OOPS!!! Task number " + taskNum + " is out of range. "
                    + "Valid range: 1 to " + tasks.size());
        }

        tasks.markTask(taskNum - 1);
        ui.showMessages(
                " Nice! I've marked this task as done:",
                "   " + tasks.get(taskNum - 1)
        );
        saveTasksToFile(tasks, ui, storage);
    }
}
