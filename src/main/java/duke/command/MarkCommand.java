package duke.command;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import duke.task.Task;
import duke.task.Todo;
import duke.task.Deadline;
import duke.task.Event;
import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;
import duke.exception.DukeException;


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
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String[] split = input.split(" ");
        int taskNum = Integer.parseInt(split[1]);
        tasks.markTask(taskNum - 1);
        ui.showMessages(
            " Nice! I've marked this task as done:",
            "   " + tasks.get(taskNum - 1)
        );
        saveTasksToFile(tasks, ui, storage);
    }
    
    private void saveTasksToFile(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasksToFile(tasks.getList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}
