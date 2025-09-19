package edith.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.task.Deadline;
import edith.ui.Ui;
import edith.exception.EdithException;

public class DeadlineCommandTest {

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

        outputStream = new ByteArrayOutputStream();
        originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
    }

    @Test
    public void execute_validDeadline_addsTask() throws EdithException {
        DeadlineCommand command = new DeadlineCommand("deadline submit assignment /by 2024-12-25");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("submit assignment", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Deadline);
    }

    @Test
    public void execute_validDeadlineWithTime_addsTask() throws EdithException {
        DeadlineCommand command = new DeadlineCommand("deadline complete project /by 25/12/2024 1800");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("complete project", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Deadline);
    }

    @Test
    public void execute_multipleDeadlines_addsAllTasks() throws EdithException {
        DeadlineCommand command1 = new DeadlineCommand("deadline task 1 /by 2024-12-25");
        DeadlineCommand command2 = new DeadlineCommand("deadline task 2 /by 26/12/2024 1200");

        command1.execute(taskList, ui, storage);
        command2.execute(taskList, ui, storage);

        assertEquals(2, taskList.size());
        assertEquals("task 1", taskList.get(0).getDescription());
        assertEquals("task 2", taskList.get(1).getDescription());
    }

    @Test
    public void execute_invalidDateFormat_showsError() throws EdithException {
        DeadlineCommand command = new DeadlineCommand("deadline invalid task /by invalid-date");
        command.execute(taskList, ui, storage);

        assertEquals(0, taskList.size());

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("OOPS!!!"));
    }

    @Test
    public void execute_emptyDescription_throwsException() {
        DeadlineCommand command = new DeadlineCommand("deadline /by 2024-12-25");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("description cannot be empty"));
    }

    @Test
    public void execute_displaysConfirmationMessage() throws EdithException {
        DeadlineCommand command = new DeadlineCommand("deadline test task /by 2024-12-25");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Mission accepted"));
        assertTrue(output.contains("test task"));
        assertTrue(output.contains("Total active missions: 1"));
    }

    @Test
    public void isExit_deadlineCommand_returnsFalse() {
        DeadlineCommand command = new DeadlineCommand("deadline test /by 2024-12-25");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesInput() {
        String input = "deadline test /by 2024-12-25";
        DeadlineCommand command = new DeadlineCommand(input);
        assertNotNull(command);
    }

    @Test
    public void execute_longDescription_handlesCorrectly() throws EdithException {
        String longDesc = "deadline very long task description with many words that should be handled properly /by 2024-12-25";
        DeadlineCommand command = new DeadlineCommand(longDesc);
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertTrue(taskList.get(0).getDescription().contains("very long task description"));
    }

    @Test
    public void execute_specialCharacters_handlesCorrectly() throws EdithException {
        DeadlineCommand command = new DeadlineCommand("deadline buy coffee @ café (€5) /by 2024-12-25");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("buy coffee @ café (€5)", taskList.get(0).getDescription());
    }

    @Test
    public void execute_leapYear_handlesCorrectly() throws EdithException {
        DeadlineCommand command = new DeadlineCommand("deadline leap year task /by 29/02/2024 1200");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("leap year task", taskList.get(0).getDescription());
    }

    @Test
    public void execute_missingByKeyword_throwsException() {
        DeadlineCommand command = new DeadlineCommand("deadline test task 2024-12-25");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("Deadline format should be"));
    }

    @Test
    public void execute_emptyDeadlineTime_throwsException() {
        DeadlineCommand command = new DeadlineCommand("deadline test task /by ");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("Deadline format should be"));
    }

    @Test
    public void execute_onlySpacesInDescription_throwsException() {
        DeadlineCommand command = new DeadlineCommand("deadline    /by 2024-12-25");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("description cannot be empty"));
    }

    @Test
    public void execute_onlySpacesInTime_throwsException() {
        DeadlineCommand command = new DeadlineCommand("deadline test task /by   ");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("time cannot be empty"));
    }

    @Test
    public void execute_multipleByKeywords_throwsException() {
        DeadlineCommand command = new DeadlineCommand("deadline test task /by 2024-12-25 /by 2024-12-26");

        EdithException exception = assertThrows(EdithException.class, () -> {
            command.execute(taskList, ui, storage);
        });
        assertTrue(exception.getMessage().contains("Deadline format should be"));
    }
}