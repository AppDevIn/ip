package edith.command;

import edith.task.Task;
import edith.task.Todo;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;
import edith.exception.EdithException;


/**
 * Command for creating a simple todo task.
 * Takes the user's description and creates a basic task with no time constraints.
 */
public class TodoCommand extends Command {
    private String input;
    
    /**
     * Creates a todo command from the user's input.
     * 
     * @param input the full command string including "todo" and description
     */
    public TodoCommand(String input) {
        this.input = input;
    }
    
    /**
     * Executes the todo command by creating a new Todo task.
     * Strips off the "todo" part and uses the rest as the description.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        String remainingInput = input.substring(4).trim();
        
        String description;
        String duration = null;
        
        if (remainingInput.contains(" /duration ")) {
            String[] parts = remainingInput.split(" /duration ");
            description = parts[0].trim();
            duration = parts[1].trim();
        } else {
            description = remainingInput;
        }
        
        Task newTask = new Todo(description);
        if (duration != null && !duration.isEmpty()) {
            try {
                newTask.setDuration(duration);
            } catch (IllegalArgumentException e) {
                throw new EdithException("Invalid duration format: " + duration + 
                    ". Use formats like '2h', '30m', '1h 30m', or '90' (minutes)");
            }
        }
        
        tasks.add(newTask);
        ui.showTaskAdded(newTask, tasks.size());
        saveTasksToFile(tasks, ui, storage);
    }
}
