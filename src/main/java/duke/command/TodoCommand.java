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
 * Command for creating a simple todo task.
 * Takes the user's description and creates a basic task with no time constraints.
 */
public class TodoCommand extends Command {
    private String input;
    
    /**
     * Creates a todo command from the user's input.
     * 
     * @param input the full command string including "todo" and description
     */
    public TodoCommand(String input) {
        this.input = input;
    }
    
    /**
     * Executes the todo command by creating a new Todo task.
     * Strips off the "todo" part and uses the rest as the description.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String description = input.substring(4).trim();
        Task newTask = new Todo(description);
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
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
