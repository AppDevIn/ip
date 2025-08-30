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


public class DeleteCommand extends Command {
    private String input;
    
    public DeleteCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String[] deleteParts = input.split(" ");
        int deleteNum = Integer.parseInt(deleteParts[1]);
        Task removedTask = tasks.delete(deleteNum - 1);
        ui.showMessages(
            " Noted. I've removed this task:",
            "   " + removedTask,
            " Now you have " + tasks.size() + " tasks in the list."
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
