package duke.command;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import duke.task.Task;
import duke.task.Event;
import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;
import duke.exception.DukeException;


public class EventCommand extends Command {
    private String input;
    
    public EventCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String[] fromSplit = input.split(" /from ");
        String eventDesc = fromSplit[0].substring(5).trim();
        String[] toSplit = fromSplit[1].split(" /to ");
        String from = toSplit[0];
        String to = toSplit[1];
        try {
            Task eventTask = new Event(eventDesc, from, to);
            tasks.add(eventTask);
            ui.showTaskAdded(eventTask, tasks.size());
            saveTasksToFile(tasks, ui, storage);
        } catch (DateTimeParseException e) {
            ui.showError("OOPS!!! " + e.getMessage());
        }
    }
    
    private void saveTasksToFile(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasksToFile(tasks.getList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}
