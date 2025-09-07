package edith.command;

import java.io.IOException;
import edith.task.Task;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;


public class DeleteCommand extends Command {
    private String input;
    
    public DeleteCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
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
