package edith.parser;

import edith.command.Command;
import edith.command.DeadlineCommand;
import edith.command.DeleteCommand;
import edith.command.EventCommand;
import edith.command.ExitCommand;
import edith.command.ListCommand;
import edith.command.MarkCommand;
import edith.command.TodoCommand;
import edith.command.UnmarkCommand;
import edith.exception.DeadlineException;
import edith.exception.EdithException;
import edith.exception.EventException;
import edith.exception.InvalidCommandException;
import edith.exception.InvalidTaskNumberException;
import edith.exception.TodoException;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ParserTest {
    
    @Test
    public void parse_validListCommand_returnsListCommand() throws EdithException {
        Command command = Parser.parse("list", 0);
        assertTrue(command instanceof ListCommand);
    }
    
    @Test
    public void parse_validTodoCommand_returnsTodoCommand() throws EdithException {
        Command command = Parser.parse("todo read book", 0);
        assertTrue(command instanceof TodoCommand);
    }
    
    @Test
    public void parse_validDeadlineCommand_returnsDeadlineCommand() throws EdithException {
        Command command = Parser.parse("deadline return book /by 2024-12-01", 0);
        assertTrue(command instanceof DeadlineCommand);
    }
    
    @Test
    public void parse_validEventCommand_returnsEventCommand() throws EdithException {
        Command command = Parser.parse("event project meeting /from 2pm /to 4pm", 0);
        assertTrue(command instanceof EventCommand);
    }
    
    @Test
    public void parse_validMarkCommand_returnsMarkCommand() throws EdithException {
        Command command = Parser.parse("mark 1", 5);
        assertTrue(command instanceof MarkCommand);
    }
    
    @Test
    public void parse_validUnmarkCommand_returnsUnmarkCommand() throws EdithException {
        Command command = Parser.parse("unmark 2", 5);
        assertTrue(command instanceof UnmarkCommand);
    }
    
    @Test
    public void parse_validDeleteCommand_returnsDeleteCommand() throws EdithException {
        Command command = Parser.parse("delete 3", 5);
        assertTrue(command instanceof DeleteCommand);
    }
    
    @Test
    public void parse_validExitCommand_returnsExitCommand() throws EdithException {
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
    
    @Test
    public void parse_todoAlias_returnsTodoCommand() throws EdithException {
        Command command = Parser.parse("t read book", 0);
        assertTrue(command instanceof TodoCommand);
    }
    
    @Test
    public void parse_deadlineAlias_returnsDeadlineCommand() throws EdithException {
        Command command = Parser.parse("d return book /by Sunday", 0);
        assertTrue(command instanceof DeadlineCommand);
    }
    
    @Test
    public void parse_eventAlias_returnsEventCommand() throws EdithException {
        Command command = Parser.parse("e meeting /from 2pm /to 4pm", 0);
        assertTrue(command instanceof EventCommand);
    }
    
    @Test
    public void parse_listAlias_returnsListCommand() throws EdithException {
        Command command = Parser.parse("l", 0);
        assertTrue(command instanceof ListCommand);
    }
    
    @Test
    public void parse_markAlias_returnsMarkCommand() throws EdithException {
        Command command = Parser.parse("m 1", 5);
        assertTrue(command instanceof MarkCommand);
    }
    
    @Test
    public void parse_unmarkAlias_returnsUnmarkCommand() throws EdithException {
        Command command = Parser.parse("u 2", 5);
        assertTrue(command instanceof UnmarkCommand);
    }
    
    @Test
    public void parse_deleteAlias_returnsDeleteCommand() throws EdithException {
        Command command = Parser.parse("del 3", 5);
        assertTrue(command instanceof DeleteCommand);
    }
    
    @Test
    public void parse_exitAliases_returnsExitCommand() throws EdithException {
        Command exitCommand = Parser.parse("exit", 0);
        Command quitCommand = Parser.parse("quit", 0);
        Command qCommand = Parser.parse("q", 0);
        
        assertTrue(exitCommand instanceof ExitCommand);
        assertTrue(quitCommand instanceof ExitCommand);
        assertTrue(qCommand instanceof ExitCommand);
    }
    
    @Test
    public void parse_fullFormCommands_stillWork() throws EdithException {
        assertTrue(Parser.parse("todo read", 0) instanceof TodoCommand);
        assertTrue(Parser.parse("deadline task /by Sunday", 0) instanceof DeadlineCommand);
        assertTrue(Parser.parse("event meeting /from 2pm /to 4pm", 0) instanceof EventCommand);
        assertTrue(Parser.parse("list", 0) instanceof ListCommand);
        assertTrue(Parser.parse("mark 1", 5) instanceof MarkCommand);
        assertTrue(Parser.parse("unmark 2", 5) instanceof UnmarkCommand);
        assertTrue(Parser.parse("delete 3", 5) instanceof DeleteCommand);
        assertTrue(Parser.parse("bye", 0) instanceof ExitCommand);
    }
}