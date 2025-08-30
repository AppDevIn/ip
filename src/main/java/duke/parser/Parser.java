package duke.parser;

import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.EventCommand;
import duke.command.ExitCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.TodoCommand;
import duke.command.UnmarkCommand;
import duke.exception.DeadlineException;
import duke.exception.DukeException;
import duke.exception.EventException;
import duke.exception.InvalidCommandException;
import duke.exception.InvalidTaskNumberException;
import duke.exception.TodoException;

public class Parser {
    
    public static Command parse(String input, int taskCount) throws DukeException {
        if (input == null || input.trim().isEmpty()) {
            throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
        
        String command = input.toLowerCase().trim().split(" ")[0];
        
        switch (command) {
            case "todo":
                validateTodoInput(input);
                return new TodoCommand(input);
            case "deadline":
                validateDeadlineInput(input);
                return new DeadlineCommand(input);
            case "event":
                validateEventInput(input);
                return new EventCommand(input);
            case "list":
                return new ListCommand();
            case "mark":
                validateTaskNumber(input, taskCount);
                return new MarkCommand(input);
            case "unmark":
                validateTaskNumber(input, taskCount);
                return new UnmarkCommand(input);
            case "delete":
                validateTaskNumber(input, taskCount);
                return new DeleteCommand(input);
            case "bye":
                return new ExitCommand();
            default:
                throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
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
}