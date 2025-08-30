import java.io.IOException;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;

public class Duke {
    public static void main(String[] args) {
        Ui ui = new Ui();
        ui.showWelcome();

        Storage storage = new Storage();
        TaskList tasks;
        Parser parser = new Parser();
        
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
                CommandType command = parser.parseCommand(input, tasks.size());
                
                switch (command) {
                    case LIST:
                        ui.showLine();
                        ui.showTaskList(tasks.getList());
                        ui.showLine();
                        break;
                        
                    case MARK:
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
                        try {
                            Task deadlineTask = new Deadline(deadlineDesc, by);
                            listOfItems.add(deadlineTask);
                            printTaskAddedMessage(deadlineTask, listOfItems.size());
                            saveTasksToFile(storage, listOfItems);
                        } catch (DateTimeParseException e) {
                            printMessage(" OOPS!!! " + e.getMessage());
                        }
                        break;
                        
                    case EVENT:
                        validateEventInput(input);
                        String[] fromSplit = input.split(" /from ");
                        String eventDesc = fromSplit[0].substring(5).trim();
                        String[] toSplit = fromSplit[1].split(" /to ");
                        String from = toSplit[0];
                        String to = toSplit[1];
                        try {
                            Task eventTask = new Event(eventDesc, from, to);
                            listOfItems.add(eventTask);
                            printTaskAddedMessage(eventTask, listOfItems.size());
                            saveTasksToFile(storage, listOfItems);
                        } catch (DateTimeParseException e) {
                            printMessage(" OOPS!!! " + e.getMessage());
                        }
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


    private static void saveTasksToFile(Storage storage, ArrayList<Task> tasks, Ui ui) {
        try {
            storage.saveTasksToFile(tasks);
        } catch (IOException e) {
            ui.showError("Warning: Could not save tasks to file. " + e.getMessage());
        }
    }
}
