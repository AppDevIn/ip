import java.io.IOException;

public class UnmarkCommand extends Command {
    private String input;
    
    public UnmarkCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String[] parts = input.split(" ");
        int unmarkNum = Integer.parseInt(parts[1]);
        tasks.unmarkTask(unmarkNum - 1);
        ui.showMessages(
            " OK, I've marked this task as not done yet:",
            "   " + tasks.get(unmarkNum - 1)
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