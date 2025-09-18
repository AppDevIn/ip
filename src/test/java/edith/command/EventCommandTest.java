package edith.command;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.task.Event;
import edith.ui.Ui;
import edith.exception.EdithException;

public class EventCommandTest {

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
    public void execute_validEvent_addsTask() throws EdithException {
        EventCommand command = new EventCommand("event project meeting /from 25/12/2024 1400 /to 25/12/2024 1600");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("project meeting", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Event);
    }

    @Test
    public void execute_validEventDifferentDates_addsTask() throws EdithException {
        EventCommand command = new EventCommand("event conference /from 2024-12-25 /to 2024-12-26");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("conference", taskList.get(0).getDescription());
        assertTrue(taskList.get(0) instanceof Event);
    }

    @Test
    public void execute_multipleEvents_addsAllTasks() throws EdithException {
        EventCommand command1 = new EventCommand("event meeting 1 /from 25/12/2024 1400 /to 25/12/2024 1500");
        EventCommand command2 = new EventCommand("event meeting 2 /from 26/12/2024 1000 /to 26/12/2024 1100");

        command1.execute(taskList, ui, storage);
        command2.execute(taskList, ui, storage);

        assertEquals(2, taskList.size());
        assertEquals("meeting 1", taskList.get(0).getDescription());
        assertEquals("meeting 2", taskList.get(1).getDescription());
    }

    @Test
    public void execute_invalidDateFormat_showsError() throws EdithException {
        EventCommand command = new EventCommand("event invalid event /from invalid-date /to another-invalid");
        command.execute(taskList, ui, storage);

        assertEquals(0, taskList.size());

        System.setOut(originalOut);
        String output = outputStream.toString();
        assertTrue(output.contains("OOPS!!!"));
    }

    @Test
    public void execute_emptyDescription_throwsException() {
        EventCommand command = new EventCommand("event /from 25/12/2024 1400 /to 25/12/2024 1600");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_displaysConfirmationMessage() throws EdithException {
        EventCommand command = new EventCommand("event test event /from 25/12/2024 1400 /to 25/12/2024 1600");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Mission accepted"));
        assertTrue(output.contains("test event"));
        assertTrue(output.contains("Total active missions: 1"));
    }

    @Test
    public void isExit_eventCommand_returnsFalse() {
        EventCommand command = new EventCommand("event test /from 2024-12-25 /to 2024-12-26");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesInput() {
        String input = "event test /from 2024-12-25 /to 2024-12-26";
        EventCommand command = new EventCommand(input);
        assertNotNull(command);
    }

    @Test
    public void execute_longDescription_handlesCorrectly() throws EdithException {
        String longDesc = "event very long event description with many words that should be handled properly /from 25/12/2024 1400 /to 25/12/2024 1600";
        EventCommand command = new EventCommand(longDesc);
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertTrue(taskList.get(0).getDescription().contains("very long event description"));
    }

    @Test
    public void execute_specialCharacters_handlesCorrectly() throws EdithException {
        EventCommand command = new EventCommand("event café meeting @ shop (€10) /from 25/12/2024 1400 /to 25/12/2024 1600");
        command.execute(taskList, ui, storage);

        assertEquals(1, taskList.size());
        assertEquals("café meeting @ shop (€10)", taskList.get(0).getDescription());
    }
}