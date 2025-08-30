import java.io.IOException;
import java.util.ArrayList;

public class Duke {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage();
        TaskList tasks;
        
        try {
            tasks = new TaskList(storage.loadTasksFromFile());
        } catch (IOException e) {
            ui.showError("Could not load saved tasks. " + e.getMessage());
            tasks = new TaskList();
        }


        boolean isEndOfPrg = false;
        while (!isEndOfPrg) {
            try {
                String input = ui.readCommand();
                CommandType command = CommandType.fromString(input);
                
                switch (command) {
                    case LIST:
                        ui.showLine();
                        ui.showTaskList(tasks.getList());
                        ui.showLine();
                        break;
                        
                    case MARK:
                        validateTaskNumber(input, tasks.size());
                        String[] split = input.split(" ");
                        int taskNum = Integer.parseInt(split[1]);
                        tasks.markTask(taskNum - 1);
                        ui.showMessages(
                            " Nice! I've marked this task as done:",
                            "   " + tasks.get(taskNum - 1)
                        );
                        saveTasksToFile(storage, tasks.getList(), ui);
                        break;
                        
                    case UNMARK:
                        validateTaskNumber(input, tasks.size());
                        String[] parts = input.split(" ");
                        int unmarkNum = Integer.parseInt(parts[1]);
                        tasks.unmarkTask(unmarkNum - 1);
                        ui.showMessages(
                            " OK, I've marked this task as not done yet:",
                            "   " + tasks.get(unmarkNum - 1)
                        );
                        saveTasksToFile(storage, tasks.getList(), ui);
                        break;
                        
                    case TODO:
                        validateTodoInput(input);
                        String description = input.substring(4).trim();
                        Task newTask = new Todo(description);
                        tasks.add(newTask);
                        ui.showTaskAdded(newTask, tasks.size());
                        saveTasksToFile(storage, tasks.getList(), ui);
                        break;
                        
                    case DEADLINE:
                        validateDeadlineInput(input);
                        String[] deadlineParts = input.split(" /by ");
                        String deadlineDesc = deadlineParts[0].substring(8).trim();
                        String by = deadlineParts[1];
                        Task deadlineTask = new Deadline(deadlineDesc, by);
                        listOfItems.add(deadlineTask);
                        printTaskAddedMessage(deadlineTask, listOfItems.size());
                        saveTasksToFile(storage, listOfItems);
                        break;
                        
                    case EVENT:
                        validateEventInput(input);
                        String[] fromSplit = input.split(" /from ");
                        String eventDesc = fromSplit[0].substring(5).trim();
                        String[] toSplit = fromSplit[1].split(" /to ");
                        String from = toSplit[0];
                        String to = toSplit[1];
                        Task eventTask = new Event(eventDesc, from, to);
                        listOfItems.add(eventTask);
                        printTaskAddedMessage(eventTask, listOfItems.size());
                        saveTasksToFile(storage, listOfItems);
                        break;
                        
                    case DELETE:
                        validateTaskNumber(input, listOfItems.size());
                        String[] deleteParts = input.split(" ");
                        int deleteNum = Integer.parseInt(deleteParts[1]);
                        Task removedTask = listOfItems.remove(deleteNum - 1);
                        ui.showMessages(
                            " Noted. I've removed this task:",
                            "   " + removedTask,
                            " Now you have " + listOfItems.size() + " tasks in the list."
                        );
                        saveTasksToFile(storage, listOfItems, ui);
                        break;
                        
                    case BYE:
                        isEndOfPrg=true;
                        break;
                        
                    case INVALID:
                    default:
                        throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that means :-(");
                }
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }

        ui.showGoodbye();
        ui.close();
    }

    
    private static void validateTodoInput(String input) throws TodoException {
        String trimmed = input.trim().toLowerCase();
        if (trimmed.equals("todo") || trimmed.matches("todo\\s*")) {
            throw new TodoException("OOPS!!! The description of a todo cannot be empty.");
        }
    }
    
    private static void validateDeadlineInput(String input) throws DeadlineException {
        String trimmed = input.trim().toLowerCase();
        if (trimmed.equals("deadline") || trimmed.matches("deadline\\s*")) {
            throw new DeadlineException("OOPS!!! The description of a deadline cannot be empty.");
        }

        if (!input.toLowerCase().contains(" /by ")) {
            throw new DeadlineException("OOPS!!! Deadline format should be: deadline <description> /by <time>");
        }

        String[] parts = input.split(" /by ");
        if (parts.length != 2 || parts[1].trim().isEmpty()) {
            throw new DeadlineException("OOPS!!! The deadline time cannot be empty.");
        }
    }
    
    private static void validateEventInput(String input) throws EventException {
        String trimmed = input.trim().toLowerCase();
        if (trimmed.equals("event") || trimmed.matches("event\\s*")) {
            throw new EventException("OOPS!!! The description of an event cannot be empty.");
        }

        if (!input.toLowerCase().contains(" /from ") || !input.toLowerCase().contains(" /to ")) {
            throw new EventException("OOPS!!! Event format should be: event <description> /from <start> /to <end>");
        }

        String[] fromSplit = input.split(" /from ");
        if (fromSplit.length != 2) {
            throw new EventException("OOPS!!! Event format should be: event <description> /from <start> /to <end>");
        }

        String[] toSplit = fromSplit[1].split(" /to ");
        if (toSplit.length != 2 || toSplit[0].trim().isEmpty() || toSplit[1].trim().isEmpty()) {
            throw new EventException("OOPS!!! Event times cannot be empty.");
        }
    }
    
    private static void validateTaskNumber(String input, int maxTasks) throws InvalidTaskNumberException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new InvalidTaskNumberException("OOPS!!! Please provide a task number.");
        }

        try {
            int taskNum = Integer.parseInt(parts[1]);
            if (taskNum < 1 || taskNum > maxTasks) {
                throw new InvalidTaskNumberException("OOPS!!! Task number " + taskNum + " is out of range. You have " + maxTasks + " tasks.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("OOPS!!! Task number must be a valid number.");
        }
    }

    private static void saveTasksToFile(Storage storage, ArrayList<Task> tasks, Ui ui) {
        try {
            storage.saveTasksToFile(tasks);
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}
