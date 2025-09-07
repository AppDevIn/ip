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
        assert tasks != null : "TaskList cannot be null";
        assert ui != null : "UI cannot be null";
        assert storage != null : "Storage cannot be null";
        
        String[] deleteParts = input.split(" ");
        int deleteNum = Integer.parseInt(deleteParts[1]);
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
    
    private void saveTasksToFile(TaskList tasks, Ui ui, Storage storage) {
        try {
            storage.saveTasksToFile(tasks.getList());
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}
