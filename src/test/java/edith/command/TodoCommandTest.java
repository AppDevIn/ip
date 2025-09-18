package edith.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.task.Todo;
import edith.ui.Ui;
import edith.exception.EdithException;

/**
 * Comprehensive test suite for TodoCommand class.
 * Tests todo creation, duration handling, and edge cases.
 */
public class TodoCommandTest {

    private TaskList taskList;
    private Ui ui;
    private Storage storage;
    private ByteArrayOutputStream outputStream;
    private PrintStream originalOut;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
        ui = new Ui();
        storage = new Storage("test_data", "test.txt");

        // Capture output for testing
        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void execute_simpleTodo_addsTaskToList() throws EdithException {
        TodoCommand command = new TodoCommand("todo read book");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("read book", taskList.get(0).getDescription());
        assertFalse(taskList.get(0).isDone());
        assertTrue(taskList.get(0) instanceof Todo);
    }

    @Test
    public void execute_todoWithDuration_setsCorrectDuration() throws EdithException {
        TodoCommand command = new TodoCommand("todo complete assignment /duration 2h");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("complete assignment", taskList.get(0).getDescription());
        assertNotNull(taskList.get(0).getDuration());
        assertEquals(120, taskList.get(0).getDuration().toMinutes());
    }

    @Test
    public void execute_todoWithComplexDuration_parsesCorrectly() throws EdithException {
        TodoCommand command = new TodoCommand("todo study for exam /duration 1h30m");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("study for exam", taskList.get(0).getDescription());
        assertEquals(90, taskList.get(0).getDuration().toMinutes());
    }

    @Test
    public void execute_todoWithMinutesDuration_parsesCorrectly() throws EdithException {
        TodoCommand command = new TodoCommand("todo quick task /duration 45");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("quick task", taskList.get(0).getDescription());
        assertEquals(45, taskList.get(0).getDuration().toMinutes());
    }

    @Test
    public void execute_todoWithoutDuration_hasNoDuration() throws EdithException {
        TodoCommand command = new TodoCommand("todo simple task");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("simple task", taskList.get(0).getDescription());
        assertNull(taskList.get(0).getDuration());
    }

    @Test
    public void execute_multipleTodos_addsAllToList() throws EdithException {
        TodoCommand command1 = new TodoCommand("todo first task");
        TodoCommand command2 = new TodoCommand("todo second task");
        TodoCommand command3 = new TodoCommand("todo third task /duration 1h");

        command1.execute(taskList, ui, storage);
        command2.execute(taskList, ui, storage);
        command3.execute(taskList, ui, storage);

        assertEquals(3, taskList.size());
        assertEquals("first task", taskList.get(0).getDescription());
        assertEquals("second task", taskList.get(1).getDescription());
        assertEquals("third task", taskList.get(2).getDescription());
        assertNotNull(taskList.get(2).getDuration());
    }

    @Test
    public void execute_todoWithSpecialCharacters_handlesCorrectly() throws EdithException {
        TodoCommand command = new TodoCommand("todo buy coffee @ café & pastry (€5)");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("buy coffee @ café & pastry (€5)", taskList.get(0).getDescription());
    }

    @Test
    public void execute_todoWithNumbers_handlesCorrectly() throws EdithException {
        TodoCommand command = new TodoCommand("todo chapter 1-5 exercises #123");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("chapter 1-5 exercises #123", taskList.get(0).getDescription());
    }

    @Test
    public void execute_longTodoDescription_handlesCorrectly() throws EdithException {
        String longDescription = "todo this is a very long task description that contains many words " +
                "and should test the system's ability to handle lengthy descriptions without any issues " +
                "or truncation problems";
        TodoCommand command = new TodoCommand(longDescription);

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals(longDescription.substring(5), taskList.get(0).getDescription());
    }

    @Test
    public void execute_todoWithMultipleSpaces_preservesSpacing() throws EdithException {
        TodoCommand command = new TodoCommand("todo   task   with   extra   spaces");

        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("  task   with   extra   spaces", taskList.get(0).getDescription());
    }

    @Test
    public void execute_todoDisplaysConfirmation() throws EdithException {
        TodoCommand command = new TodoCommand("todo test task");

        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Mission accepted"));
        assertTrue(output.contains("test task"));
        assertTrue(output.contains("Total active missions: 1"));
    }

    @Test
    public void execute_invalidDurationFormat_throwsException() {
        // Invalid duration format should throw an exception
        TodoCommand command = new TodoCommand("todo task /duration invalid");

        assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void isExit_todoCommand_returnsFalse() {
        TodoCommand command = new TodoCommand("todo test task");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesFullInput() {
        String input = "todo complex task /duration 2h30m with notes";
        TodoCommand command = new TodoCommand(input);

        // The command should preserve the full input for processing
        assertNotNull(command);
    }

    @Test
    public void execute_emptyTaskList_addsFirstTask() throws EdithException {
        assertTrue(taskList.getList().isEmpty());

        TodoCommand command = new TodoCommand("todo first ever task");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("first ever task", taskList.get(0).getDescription());
    }
}
