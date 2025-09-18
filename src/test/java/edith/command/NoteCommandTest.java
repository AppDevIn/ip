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
import edith.exception.NoteException;

public class NoteCommandTest {

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
    public void execute_validNote_addsNoteToTask() throws EdithException {
        NoteCommand command = new NoteCommand("note 1 important reminder");
        command.execute(taskList, ui, storage);

        assertEquals("important reminder", taskList.get(0).getNote());
        assertEquals("first task", taskList.get(0).getDescription());
    }

    @Test
    public void execute_noteOnSecondTask_addsToCorrectTask() throws EdithException {
        NoteCommand command = new NoteCommand("note 2 second task note");
        command.execute(taskList, ui, storage);

        assertEquals("", taskList.get(0).getNote());
        assertEquals("second task note", taskList.get(1).getNote());
        assertEquals("", taskList.get(2).getNote());
    }

    @Test
    public void execute_updateExistingNote_replacesNote() throws EdithException {
        NoteCommand firstNote = new NoteCommand("note 1 first note");
        firstNote.execute(taskList, ui, storage);
        assertEquals("first note", taskList.get(0).getNote());

        NoteCommand secondNote = new NoteCommand("note 1 updated note");
        secondNote.execute(taskList, ui, storage);
        assertEquals("updated note", taskList.get(0).getNote());
    }

    @Test
    public void execute_longNote_handlesCorrectly() throws EdithException {
        String longNote = "this is a very long note with many words that should be handled properly without any issues";
        NoteCommand command = new NoteCommand("note 1 " + longNote);
        command.execute(taskList, ui, storage);

        assertEquals(longNote, taskList.get(0).getNote());
    }

    @Test
    public void execute_noteWithSpecialCharacters_handlesCorrectly() throws EdithException {
        NoteCommand command = new NoteCommand("note 1 remember @ 5pm (€10) & bring laptop!");
        command.execute(taskList, ui, storage);

        assertEquals("remember @ 5pm (€10) & bring laptop!", taskList.get(0).getNote());
    }

    @Test
    public void execute_invalidTaskNumber_throwsException() {
        NoteCommand command = new NoteCommand("note 5 some note");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_negativeTaskNumber_throwsException() {
        NoteCommand command = new NoteCommand("note -1 some note");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_zeroTaskNumber_throwsException() {
        NoteCommand command = new NoteCommand("note 0 some note");

        assertThrows(AssertionError.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_nonNumericTaskNumber_throwsException() {
        NoteCommand command = new NoteCommand("note abc some note");

        assertThrows(NoteException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_emptyNote_throwsException() {
        NoteCommand command = new NoteCommand("note 1  ");

        assertThrows(NoteException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_missingNoteText_throwsException() {
        NoteCommand command = new NoteCommand("note 1");

        assertThrows(NoteException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_missingTaskNumber_throwsException() {
        NoteCommand command = new NoteCommand("note");

        assertThrows(NoteException.class, () -> {
            command.execute(taskList, ui, storage);
        });
    }

    @Test
    public void execute_displaysConfirmationMessage() throws EdithException {
        NoteCommand command = new NoteCommand("note 1 test note");
        command.execute(taskList, ui, storage);

        System.setOut(originalOut);
        String output = outputStream.toString();

        assertTrue(output.contains("Got it! I've added a note to this task:"));
        assertTrue(output.contains("first task"));
    }

    @Test
    public void execute_emptyTaskList_throwsException() {
        TaskList emptyList = new TaskList();
        NoteCommand command = new NoteCommand("note 1 some note");

        assertThrows(AssertionError.class, () -> {
            command.execute(emptyList, ui, storage);
        });
    }

    @Test
    public void isExit_noteCommand_returnsFalse() {
        NoteCommand command = new NoteCommand("note 1 test");
        assertFalse(command.isExit());
    }

    @Test
    public void constructor_preservesInput() {
        String input = "note 1 test note";
        NoteCommand command = new NoteCommand(input);
        assertNotNull(command);
    }

    @Test
    public void execute_taskListSize_unchangedAfterNote() throws EdithException {
        int originalSize = taskList.size();

        NoteCommand command = new NoteCommand("note 1 test note");
        command.execute(taskList, ui, storage);

        assertEquals(originalSize, taskList.size());
    }
}