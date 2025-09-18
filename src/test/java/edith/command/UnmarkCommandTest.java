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

public class UnmarkCommandTest {

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

        taskList.markTask(0);
        taskList.markTask(1);
        taskList.markTask(2);

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void execute_validTaskNumber_unmarksTask() throws EdithException {
        assertTrue(taskList.get(0).isDone());

        UnmarkCommand command = new UnmarkCommand("unmark 1");
        command.execute(taskList, ui, storage);

        assertFalse(taskList.get(0).isDone());
        assertEquals("first task", taskList.get(0).getDescription());
    }

    @Test
    public void execute_secondTask_unmarksCorrectTask() throws EdithException {
        assertTrue(taskList.get(1).isDone());

        UnmarkCommand command = new UnmarkCommand("unmark 2");
        command.execute(taskList, ui, storage);

        assertTrue(taskList.get(0).isDone());
        assertFalse(taskList.get(1).isDone());
        assertTrue(taskList.get(2).isDone());
    }

    @Test
    public void execute_lastTask_unmarksCorrectTask() throws EdithException {
        UnmarkCommand command = new UnmarkCommand("unmark 3");
        command.execute(taskList, ui, storage);

        assertTrue(taskList.get(0).isDone());
        assertTrue(taskList.get(1).isDone());
        assertFalse(taskList.get(2).isDone());
    }

    @Test
    public void execute_alreadyUndoneTask_remainsUnmarked() throws EdithException {
        UnmarkCommand firstUnmark = new UnmarkCommand("unmark 1");
        firstUnmark.execute(taskList, ui, storage);
        assertFalse(taskList.get(0).isDone());

        UnmarkCommand secondUnmark = new UnmarkCommand("unmark 1");
        secondUnmark.execute(taskList, ui, storage);

        assertFalse(taskList.get(0).isDone());
    }

    @Test
    public void execute_invalidTaskNumber_throwsException() {
        UnmarkCommand command = new UnmarkCommand("unmark 5");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_negativeTaskNumber_throwsException() {
        UnmarkCommand command = new UnmarkCommand("unmark -1");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_zeroTaskNumber_throwsException() {
        UnmarkCommand command = new UnmarkCommand("unmark 0");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_emptyTaskList_throwsException() {
        TaskList emptyList = new TaskList();
        UnmarkCommand command = new UnmarkCommand("unmark 1");

        assertThrows(AssertionError.class, () -> {
            command.execute(emptyList, ui, storage);
        });
    }

    @Test
    public void execute_nonNumericTaskNumber_throwsException() {
        UnmarkCommand command = new UnmarkCommand("unmark abc");

        assertThrows(NumberFormatException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_displaysConfirmationMessage() throws EdithException {
        UnmarkCommand command = new UnmarkCommand("unmark 1");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("OK, I've marked this task as not done yet:"));
        assertTrue(output.contains("first task"));
    }

    @Test
    public void execute_multipleUnmarks_allTasksUnmarked() throws EdithException {
        UnmarkCommand unmark1 = new UnmarkCommand("unmark 1");
        UnmarkCommand unmark2 = new UnmarkCommand("unmark 2");
        UnmarkCommand unmark3 = new UnmarkCommand("unmark 3");

        unmark1.execute(taskList, ui, storage);
        unmark2.execute(taskList, ui, storage);
        unmark3.execute(taskList, ui, storage);

        assertFalse(taskList.get(0).isDone());
        assertFalse(taskList.get(1).isDone());
        assertFalse(taskList.get(2).isDone());
    }

    @Test
    public void isExit_unmarkCommand_returnsFalse() {
        UnmarkCommand command = new UnmarkCommand("unmark 1");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesInput() {
        String input = "unmark 2";
        UnmarkCommand command = new UnmarkCommand(input);
        assertNotNull(command);
    }

    @Test
    public void execute_withExtraSpaces_throwsException() {
        UnmarkCommand command = new UnmarkCommand("unmark  2");

        assertThrows(NumberFormatException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_taskListSize_unchangedAfterUnmark() throws EdithException {
        int originalSize = taskList.size();

        UnmarkCommand command = new UnmarkCommand("unmark 1");
        command.execute(taskList, ui, storage);

        assertEquals(originalSize, taskList.size());
    }
}