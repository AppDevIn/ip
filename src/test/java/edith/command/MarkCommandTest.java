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
 * Comprehensive test suite for MarkCommand class.
 * Tests task marking functionality, edge cases, and error conditions.
 */
public class MarkCommandTest {

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


        taskList.add(new Todo("first task"));
        taskList.add(new Todo("second task"));
        taskList.add(new Todo("third task"));


        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void execute_validTaskNumber_marksTaskAsDone() throws EdithException {
        assertFalse(taskList.get(0).isDone());

        MarkCommand command = new MarkCommand("mark 1");
        command.execute(taskList, ui, storage);

        assertTrue(taskList.get(0).isDone());
        assertEquals("first task", taskList.get(0).getDescription());
    }

    @Test
    public void execute_secondTask_marksCorrectTask() throws EdithException {
        assertFalse(taskList.get(1).isDone());

        MarkCommand command = new MarkCommand("mark 2");
        command.execute(taskList, ui, storage);

        assertFalse(taskList.get(0).isDone());
        assertTrue(taskList.get(1).isDone());
        assertFalse(taskList.get(2).isDone());
    }

    @Test
    public void execute_lastTask_marksCorrectTask() throws EdithException {
        MarkCommand command = new MarkCommand("mark 3");
        command.execute(taskList, ui, storage);

        assertFalse(taskList.get(0).isDone());
        assertFalse(taskList.get(1).isDone());
        assertTrue(taskList.get(2).isDone());
    }

    @Test
    public void execute_alreadyDoneTask_remainsMarked() throws EdithException {
        MarkCommand firstMark = new MarkCommand("mark 1");
        firstMark.execute(taskList, ui, storage);
        assertTrue(taskList.get(0).isDone());

        MarkCommand secondMark = new MarkCommand("mark 1");
        secondMark.execute(taskList, ui, storage);

        assertTrue(taskList.get(0).isDone());
    }

    @Test
    public void execute_invalidTaskNumber_throwsException() {
        MarkCommand command = new MarkCommand("mark 5");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_negativeTaskNumber_throwsException() {
        MarkCommand command = new MarkCommand("mark -1");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_zeroTaskNumber_throwsException() {
        MarkCommand command = new MarkCommand("mark 0");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_nonNumericTaskNumber_throwsException() {
        MarkCommand command = new MarkCommand("mark abc");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }

    @Test
    public void execute_emptyTaskList_throwsException() {
        TaskList emptyList = new TaskList();
        MarkCommand command = new MarkCommand("mark 1");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(emptyList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_displaysConfirmationMessage() throws EdithException {
        MarkCommand command = new MarkCommand("mark 1");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Nice! I've marked this task as done:"));
        assertTrue(output.contains("first task"));
    }

    @Test
    public void execute_multipleMarks_allTasksMarked() throws EdithException {
        MarkCommand mark1 = new MarkCommand("mark 1");
        MarkCommand mark2 = new MarkCommand("mark 2");
        MarkCommand mark3 = new MarkCommand("mark 3");

        mark1.execute(taskList, ui, storage);
        mark2.execute(taskList, ui, storage);
        mark3.execute(taskList, ui, storage);

        assertTrue(taskList.get(0).isDone());
        assertTrue(taskList.get(1).isDone());
        assertTrue(taskList.get(2).isDone());
    }

    @Test
    public void isExit_markCommand_returnsFalse() {
        MarkCommand command = new MarkCommand("mark 1");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesInput() {
        String input = "mark 2";
        MarkCommand command = new MarkCommand(input);
        assertNotNull(command);

    }

    @Test
    public void execute_withExtraSpaces_throwsException() {
        MarkCommand command = new MarkCommand("mark  2");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }

    @Test
    public void execute_taskListSize_unchangedAfterMark() throws EdithException {
        int originalSize = taskList.size();

        MarkCommand command = new MarkCommand("mark 1");
        command.execute(taskList, ui, storage);

        assertEquals(originalSize, taskList.size());
    }

    @Test
    public void execute_missingTaskNumber_throwsException() {
        MarkCommand command = new MarkCommand("mark");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("provide a task number"));
    }

    @Test
    public void execute_onlySpacesAfterMark_throwsException() {
        MarkCommand command = new MarkCommand("mark   ");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("provide a task number"));
    }

    @Test
    public void execute_floatingPointNumber_throwsException() {
        MarkCommand command = new MarkCommand("mark 1.5");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }
}
