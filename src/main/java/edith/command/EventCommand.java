package edith.command;

import java.time.format.DateTimeParseException;
import edith.task.Task;
import edith.task.Event;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;


public class EventCommand extends Command {
    private String input;
    
    public EventCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        String[] fromSplit = input.split(" /from ");
        String[] commandParts = fromSplit[0].split(" ", 2);
        String eventDesc = commandParts.length > 1 ? commandParts[1] : "";
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
}
