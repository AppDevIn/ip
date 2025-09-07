package edith.command;

import java.io.IOException;
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
        String deadlineDesc = deadlineParts[0].substring(8).trim();
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
    
    private void saveTasksToFile(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasksToFile(tasks.getList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}