package duke.parser;

import duke.command.Command;
import duke.command.DeadlineCommand;
import duke.command.DeleteCommand;
import duke.command.EventCommand;
import duke.command.ExitCommand;
import duke.command.FindCommand;
import duke.command.ListCommand;
import duke.command.MarkCommand;
import duke.command.TodoCommand;
import duke.command.UnmarkCommand;
import duke.exception.DeadlineException;
import duke.exception.DukeException;
import duke.exception.EventException;
import duke.exception.FindException;
import duke.exception.InvalidCommandException;
import duke.exception.InvalidTaskNumberException;
import duke.exception.TodoException;

/**
 * Utility class for parsing user input commands and converting them into Command objects.
 * Handles validation of command formats and parameters.
 */
public class Parser {

    /**
     * Parses the user input command string and returns the appropriate Command object.
     * Validates the input format and parameters based on the command type and current task count.
     *
     * @param input the command string entered by the user
     * @param taskCount the current number of tasks in the task list
     * @return the corresponding Command object based on the input
     * @throws DukeException if the input is invalid or command parameters are incorrect
     */
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
            case "find":
                validateFindInput(input);
                return new FindCommand(input);
            case "bye":
                return new ExitCommand();
            default:
                throw new InvalidCommandException("OOPS!!! I'm sorry, but I don't know what that means :-(");
        }
    }


    /**
     * Validates the format of a todo command input.
     *
     * @param input the todo command string to validate
     * @throws TodoException if the todo description is empty or contains only whitespace
     */
    private static void validateTodoInput(String input) throws TodoException {
        String trimmed = input.trim().toLowerCase();
        if (trimmed.equals("todo") || trimmed.matches("todo\\s*")) {
            throw new TodoException("OOPS!!! The description of a todo cannot be empty.");
        }
    }

    /**
     * Validates the format of a deadline command input.
     *
     * @param input the deadline command string to validate
     * @throws DeadlineException if the deadline format is incorrect or missing required parts
     */
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

    /**
     * Validates the format of an event command input.
     *
     * @param input the event command string to validate
     * @throws EventException if the event format is incorrect or missing required time parameters
     */
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

    /**
     * Validates that a task number command has a valid task number parameter.
     *
     * @param input the command string containing the task number
     * @param maxTasks the maximum number of tasks currently in the task list
     * @throws InvalidTaskNumberException if the task number is missing, invalid, or out of range
     */
    private static void validateTaskNumber(String input, int maxTasks) throws InvalidTaskNumberException {
        String[] parts = input.split(" ");
        if (parts.length != 2) {
            throw new InvalidTaskNumberException("OOPS!!! Please provide a task number.");
        }

        try {
            int taskNum = Integer.parseInt(parts[1]);
            if (taskNum < 1 || taskNum > maxTasks) {
                throw new InvalidTaskNumberException("OOPS!!! Task number " + taskNum
                        + " is out of range. You have " + maxTasks + " tasks.");
            }
        } catch (NumberFormatException e) {
            throw new InvalidTaskNumberException("OOPS!!! Task number must be a valid number.");
        }
    }

    /**
     * Validates the format of a find command input.
     *
     * @param input the find command string to validate
     * @throws FindException if the search keyword is empty or contains only whitespace
     */
    private static void validateFindInput(String input) throws FindException {
        String trimmed = input.trim().toLowerCase();
        if (trimmed.equals("find") || trimmed.matches("find\\s*")) {
            throw new FindException("OOPS!!! The search keyword cannot be empty.");
        }
    }
}
