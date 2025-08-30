import java.io.IOException;

public class MarkCommand extends Command {
    private String input;
    
    public MarkCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String[] split = input.split(" ");
        int taskNum = Integer.parseInt(split[1]);
        tasks.markTask(taskNum - 1);
        ui.showMessages(
            " Nice! I've marked this task as done:",
            "   " + tasks.get(taskNum - 1)
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