package edith.command;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;


public class UnmarkCommand extends Command {
    private String input;
    
    public UnmarkCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        String[] parts = input.split(" ");
        int unmarkNum = Integer.parseInt(parts[1]);
        tasks.unmarkTask(unmarkNum - 1);
        ui.showMessages(
                " OK, I've marked this task as not done yet:",
                "   " + tasks.get(unmarkNum - 1)
        );
        saveTasksToFile(tasks, ui, storage);
    }
}
