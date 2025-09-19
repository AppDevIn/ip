package edith.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.task.Todo;
import edith.task.Deadline;
import edith.task.DateTimeParser;
import edith.ui.Ui;
import edith.exception.EdithException;

/**
 * Comprehensive test suite for DeleteCommand class.
 * Tests task deletion functionality, edge cases, and error conditions.
 */
public class DeleteCommandTest {

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
    public void execute_validTaskNumber_deletesTask() throws EdithException {
        assertEquals(3, taskList.size());

        DeleteCommand command = new DeleteCommand("delete 1");
        command.execute(taskList, ui, storage);

        assertEquals(2, taskList.size());
        assertEquals("second task", taskList.get(0).getDescription());
        assertEquals("third task", taskList.get(1).getDescription());
    }

    @Test
    public void execute_deleteMiddleTask_correctTaskRemoved() throws EdithException {
        DeleteCommand command = new DeleteCommand("delete 2");
        command.execute(taskList, ui, storage);

        assertEquals(2, taskList.size());
        assertEquals("first task", taskList.get(0).getDescription());
        assertEquals("third task", taskList.get(1).getDescription());
    }

    @Test
    public void execute_deleteLastTask_correctTaskRemoved() throws EdithException {
        DeleteCommand command = new DeleteCommand("delete 3");
        command.execute(taskList, ui, storage);

        assertEquals(2, taskList.size());
        assertEquals("first task", taskList.get(0).getDescription());
        assertEquals("second task", taskList.get(1).getDescription());
    }

    @Test
    public void execute_deleteSingleTask_listBecomesEmpty() throws EdithException {
        TaskList singleTaskList = new TaskList();
        singleTaskList.add(new Todo("only task"));

        DeleteCommand command = new DeleteCommand("delete 1");
        command.execute(singleTaskList, ui, storage);

        assertEquals(0, singleTaskList.size());
        assertTrue(singleTaskList.getList().isEmpty());
    }

    @Test
    public void execute_deleteMarkedTask_removesCorrectly() throws EdithException {
        taskList.markTask(1);

        DeleteCommand command = new DeleteCommand("delete 2");
        command.execute(taskList, ui, storage);

        assertEquals(2, taskList.size());
        assertEquals("first task", taskList.get(0).getDescription());
        assertEquals("third task", taskList.get(1).getDescription());
    }

    @Test
    public void execute_deleteMixedTaskTypes_removesCorrectly() throws EdithException {
        TaskList mixedList = new TaskList();
        mixedList.add(new Todo("todo task"));
        mixedList.add(new Deadline("deadline task", DateTimeParser.parseDateTime("01/01/2024 1200")));
        mixedList.add(new Todo("another todo"));

        DeleteCommand command = new DeleteCommand("delete 2");
        command.execute(mixedList, ui, storage);

        assertEquals(2, mixedList.size());
        assertEquals("todo task", mixedList.get(0).getDescription());
        assertEquals("another todo", mixedList.get(1).getDescription());
    }

    @Test
    public void execute_invalidTaskNumber_throwsException() {
        DeleteCommand command = new DeleteCommand("delete 5");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_negativeTaskNumber_throwsException() {
        DeleteCommand command = new DeleteCommand("delete -1");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_zeroTaskNumber_throwsException() {
        DeleteCommand command = new DeleteCommand("delete 0");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_emptyTaskList_throwsException() {
        TaskList emptyList = new TaskList();
        DeleteCommand command = new DeleteCommand("delete 1");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(emptyList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("out of range"));
    }

    @Test
    public void execute_nonNumericTaskNumber_throwsException() {
        DeleteCommand command = new DeleteCommand("delete abc");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }

    @Test
    public void execute_displaysConfirmationMessage() throws EdithException {
        DeleteCommand command = new DeleteCommand("delete 1");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Noted. I've removed this task:"));
        assertTrue(output.contains("first task"));
        assertTrue(output.contains("Now you have 2 tasks in the list."));
    }

    @Test
    public void execute_multipleDeletes_correctOrder() throws EdithException {

        DeleteCommand delete3 = new DeleteCommand("delete 3");
        DeleteCommand delete2 = new DeleteCommand("delete 2");
        DeleteCommand delete1 = new DeleteCommand("delete 1");

        delete3.execute(taskList, ui, storage);
        assertEquals(2, taskList.size());

        delete2.execute(taskList, ui, storage);
        assertEquals(1, taskList.size());

        delete1.execute(taskList, ui, storage);
        assertEquals(0, taskList.size());
    }

    @Test
    public void execute_preservesOtherTasksProperties() throws EdithException {
        Todo firstTask = new Todo("task with note");
        firstTask.setNote("important note");
        firstTask.markAsDone();

        TaskList customList = new TaskList();
        customList.add(firstTask);
        customList.add(new Todo("task to delete"));
        customList.add(new Todo("third task"));

        DeleteCommand command = new DeleteCommand("delete 2");
        command.execute(customList, ui, storage);

        assertEquals(2, customList.size());
        assertTrue(customList.get(0).isDone());
        assertEquals("important note", customList.get(0).getNote());
        assertEquals("task with note", customList.get(0).getDescription());
    }

    @Test
    public void isExit_deleteCommand_returnsFalse() {
        DeleteCommand command = new DeleteCommand("delete 1");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesInput() {
        String input = "delete 2";
        DeleteCommand command = new DeleteCommand(input);
        assertNotNull(command);
    }

    @Test
    public void execute_withExtraSpaces_throwsException() {
        DeleteCommand command = new DeleteCommand("delete  2");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }

    @Test
    public void execute_correctTaskCountInMessage() throws EdithException {
        int originalSize = taskList.size();

        DeleteCommand command = new DeleteCommand("delete 1");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Now you have " + (originalSize - 1) + " tasks in the list."));
    }

    @Test
    public void execute_returnsDeletedTask() throws EdithException {

        DeleteCommand command = new DeleteCommand("delete 2");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("second task"));
    }

    @Test
    public void execute_storageFailure_showsError() throws EdithException {
        FailingStorage failingStorage = new FailingStorage();
        DeleteCommand command = new DeleteCommand("delete 1");

        command.execute(taskList, ui, failingStorage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Warning: Could not save tasks to file"));
        assertEquals(2, taskList.size());
    }

    @Test
    public void execute_missingTaskNumber_throwsException() {
        DeleteCommand command = new DeleteCommand("delete");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("provide a task number"));
    }

    @Test
    public void execute_onlySpacesAfterDelete_throwsException() {
        DeleteCommand command = new DeleteCommand("delete   ");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("provide a task number"));
    }

    @Test
    public void execute_floatingPointNumber_throwsException() {
        DeleteCommand command = new DeleteCommand("delete 1.5");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("valid number"));
    }
}
