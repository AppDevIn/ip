package duke.parser;

import duke.command.*;
import duke.exception.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    
    @Test
    public void parse_validListCommand_returnsListCommand() throws DukeException {
        Command command = Parser.parse("list", 0);
        assertTrue(command instanceof ListCommand);
    }
    
    @Test
    public void parse_validTodoCommand_returnsTodoCommand() throws DukeException {
        Command command = Parser.parse("todo read book", 0);
        assertTrue(command instanceof TodoCommand);
    }
    
    @Test
    public void parse_validDeadlineCommand_returnsDeadlineCommand() throws DukeException {
        Command command = Parser.parse("deadline return book /by 2024-12-01", 0);
        assertTrue(command instanceof DeadlineCommand);
    }
    
    @Test
    public void parse_validEventCommand_returnsEventCommand() throws DukeException {
        Command command = Parser.parse("event project meeting /from 2pm /to 4pm", 0);
        assertTrue(command instanceof EventCommand);
    }
    
    @Test
    public void parse_validMarkCommand_returnsMarkCommand() throws DukeException {
        Command command = Parser.parse("mark 1", 5);
        assertTrue(command instanceof MarkCommand);
    }
    
    @Test
    public void parse_validUnmarkCommand_returnsUnmarkCommand() throws DukeException {
        Command command = Parser.parse("unmark 2", 5);
        assertTrue(command instanceof UnmarkCommand);
    }
    
    @Test
    public void parse_validDeleteCommand_returnsDeleteCommand() throws DukeException {
        Command command = Parser.parse("delete 3", 5);
        assertTrue(command instanceof DeleteCommand);
    }
    
    @Test
    public void parse_validExitCommand_returnsExitCommand() throws DukeException {
        Command command = Parser.parse("bye", 0);
        assertTrue(command instanceof ExitCommand);
        assertTrue(command.isExit());
    }
    
    @Test
    public void parse_emptyInput_throwsInvalidCommandException() {
        InvalidCommandException exception = assertThrows(InvalidCommandException.class, () -> {
            Parser.parse("", 0);
        });
        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", exception.getMessage());
    }
    
    @Test
    public void parse_nullInput_throwsInvalidCommandException() {
        InvalidCommandException exception = assertThrows(InvalidCommandException.class, () -> {
            Parser.parse(null, 0);
        });
        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", exception.getMessage());
    }
    
    @Test
    public void parse_invalidCommand_throwsInvalidCommandException() {
        InvalidCommandException exception = assertThrows(InvalidCommandException.class, () -> {
            Parser.parse("invalid command", 0);
        });
        assertEquals("OOPS!!! I'm sorry, but I don't know what that means :-(", exception.getMessage());
    }
    
    @Test
    public void parse_emptyTodoDescription_throwsTodoException() {
        assertThrows(TodoException.class, () -> {
            Parser.parse("todo", 0);
        });
    }
    
    @Test
    public void parse_todoWithOnlySpaces_throwsTodoException() {
        assertThrows(TodoException.class, () -> {
            Parser.parse("todo   ", 0);
        });
    }
    
    @Test
    public void parse_deadlineWithoutBy_throwsDeadlineException() {
        assertThrows(DeadlineException.class, () -> {
            Parser.parse("deadline return book", 0);
        });
    }
    
    @Test
    public void parse_eventWithoutFrom_throwsEventException() {
        assertThrows(EventException.class, () -> {
            Parser.parse("event meeting /to 4pm", 0);
        });
    }
    
    @Test
    public void parse_markWithoutNumber_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class, () -> {
            Parser.parse("mark", 5);
        });
    }
    
    @Test
    public void parse_markWithInvalidNumber_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class, () -> {
            Parser.parse("mark abc", 5);
        });
    }
    
    @Test
    public void parse_markWithOutOfRangeNumber_throwsInvalidTaskNumberException() {
        assertThrows(InvalidTaskNumberException.class, () -> {
            Parser.parse("mark 10", 5);
        });
    }
}