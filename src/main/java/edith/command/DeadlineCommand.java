package edith.command;

import java.time.format.DateTimeParseException;
import edith.task.Task;
import edith.task.Deadline;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;

public class DeadlineCommand extends Command {
    private String input;
    
    public DeadlineCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        String[] deadlineParts = input.split(" /by ");
        String[] commandParts = deadlineParts[0].split(" ", 2);
        String deadlineDesc = commandParts.length > 1 ? commandParts[1] : "";
        String by = deadlineParts[1];
        try {
            Task deadlineTask = new Deadline(deadlineDesc, by);
            tasks.add(deadlineTask);
            ui.showTaskAdded(deadlineTask, tasks.size());
            saveTasksToFile(tasks, ui, storage);
        } catch (DateTimeParseException e) {
            ui.showError("OOPS!!! " + e.getMessage());
        }
    }
}