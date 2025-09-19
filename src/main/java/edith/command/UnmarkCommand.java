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

        if (parts.length < 2) {
            throw new EdithException("OOPS!!! Please provide a task number to unmark.");
        }

        int unmarkNum;
        try {
            unmarkNum = Integer.parseInt(parts[1]);
        } catch (NumberFormatException e) {
            throw new EdithException("OOPS!!! Task number must be a valid number.");
        }

        if (unmarkNum < 1 || unmarkNum > tasks.size()) {
            throw new EdithException("OOPS!!! Task number " + unmarkNum + " is out of range. "
                    + "Valid range: 1 to " + tasks.size());
        }

        tasks.unmarkTask(unmarkNum - 1);
        ui.showMessages(
                " OK, I've marked this task as not done yet:",
                "   " + tasks.get(unmarkNum - 1)
        );
        saveTasksToFile(tasks, ui, storage);
    }
}
