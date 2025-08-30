import java.io.IOException;

public class TodoCommand extends Command {
    private String input;
    
    public TodoCommand(String input) {
        this.input = input;
    }
    
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        String description = input.substring(4).trim();
        Task newTask = new Todo(description);
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
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