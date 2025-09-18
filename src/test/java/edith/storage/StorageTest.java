package edith.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.io.TempDir;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.ArrayList;

import edith.task.Task;
import edith.task.Todo;
import edith.task.Deadline;
import edith.task.Event;
import edith.task.DateTimeParser;

/**
 * Comprehensive test suite for Storage class.
 * Tests file I/O operations, error handling, and edge cases.
 */
public class StorageTest {

    @TempDir
    Path tempDir;

    private Storage storage;
    private String testDataDir;
    private String testFileName;

    @BeforeEach
    public void setUp() {
        testDataDir = tempDir.toString();
        testFileName = "test_tasks.txt";
        storage = new Storage(testDataDir, testFileName);
    }

    @Test
    public void constructor_defaultParameters_createsValidStorage() {
        Storage defaultStorage = new Storage();
        assertNotNull(defaultStorage);
    }

    @Test
    public void constructor_customParameters_createsValidStorage() {
        Storage customStorage = new Storage("custom_data", "custom_file.txt");
        assertNotNull(customStorage);
    }

    @Test
    public void saveTasksToFile_emptyList_createsEmptyFile() throws IOException {
        ArrayList<Task> emptyTasks = new ArrayList<>();

        storage.saveTasksToFile(emptyTasks);

        File savedFile = new File(testDataDir + File.separator + testFileName);
        assertTrue(savedFile.exists());
        assertEquals(0, savedFile.length());
    }

    @Test
    public void saveTasksToFile_singleTodo_savesCorrectly() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("test task"));

        storage.saveTasksToFile(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasksFromFile();

        assertEquals(1, loadedTasks.size());
        assertEquals("test task", loadedTasks.get(0).getDescription());
        assertFalse(loadedTasks.get(0).isDone());
    }

    @Test
    public void saveTasksToFile_multipleTasks_savesAllCorrectly() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("todo task"));
        tasks.add(new Deadline("deadline task", DateTimeParser.parseDateTime("01/01/2024 1200")));
        tasks.add(new Event("event task",
                DateTimeParser.parseDateTime("01/01/2024 1400"),
                DateTimeParser.parseDateTime("01/01/2024 1600")));

        storage.saveTasksToFile(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasksFromFile();

        assertEquals(3, loadedTasks.size());
        assertEquals("todo task", loadedTasks.get(0).getDescription());
        assertEquals("deadline task", loadedTasks.get(1).getDescription());
        assertEquals("event task", loadedTasks.get(2).getDescription());
    }

    @Test
    public void saveTasksToFile_markedTasks_preservesStatus() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        Todo completedTask = new Todo("completed task");
        completedTask.markAsDone();
        tasks.add(completedTask);
        tasks.add(new Todo("incomplete task"));

        storage.saveTasksToFile(tasks);
        ArrayList<Task> loadedTasks = storage.loadTasksFromFile();

        assertEquals(2, loadedTasks.size());
        assertTrue(loadedTasks.get(0).isDone());
        assertFalse(loadedTasks.get(1).isDone());
    }

    @Test
    public void loadTasksFromFile_nonexistentFile_returnsEmptyList() throws IOException {
        Storage newStorage = new Storage(testDataDir, "nonexistent.txt");
        ArrayList<Task> tasks = newStorage.loadTasksFromFile();

        assertTrue(tasks.isEmpty());
    }

    @Test
    public void loadTasksFromFile_corruptedData_filtersOutInvalidTasks() throws IOException {
        // Create file with mixed valid and invalid JSON
        ArrayList<Task> validTasks = new ArrayList<>();
        validTasks.add(new Todo("valid task"));
        storage.saveTasksToFile(validTasks);

        // Manually append corrupted data
        try (java.io.FileWriter writer = new java.io.FileWriter(
                testDataDir + File.separator + testFileName, true)) {
            writer.write("invalid json line" + System.lineSeparator());
            writer.write("{\"invalid\": \"json\"}" + System.lineSeparator());
        }

        ArrayList<Task> loadedTasks = storage.loadTasksFromFile();

        // Should only load the valid task, filter out corrupted ones
        assertEquals(1, loadedTasks.size());
        assertEquals("valid task", loadedTasks.get(0).getDescription());
    }

    @Test
    public void saveAndLoad_roundTrip_preservesAllTaskData() throws IOException {
        ArrayList<Task> originalTasks = new ArrayList<>();

        // Create tasks with various features
        Todo todoWithDuration = new Todo("todo with duration");
        todoWithDuration.setDuration("2h");

        Todo todoWithNote = new Todo("todo with note");
        todoWithNote.setNote("important note");

        Deadline deadline = new Deadline("deadline task",
                DateTimeParser.parseDateTime("25/12/2024 1800"));
        deadline.markAsDone();

        originalTasks.add(todoWithDuration);
        originalTasks.add(todoWithNote);
        originalTasks.add(deadline);

        storage.saveTasksToFile(originalTasks);
        ArrayList<Task> loadedTasks = storage.loadTasksFromFile();

        assertEquals(3, loadedTasks.size());

        // Verify todo with duration
        assertEquals("todo with duration", loadedTasks.get(0).getDescription());
        assertNotNull(loadedTasks.get(0).getDuration());
        assertEquals(120, loadedTasks.get(0).getDuration().toMinutes());

        // Verify todo with note
        assertEquals("todo with note", loadedTasks.get(1).getDescription());
        assertEquals("important note", loadedTasks.get(1).getNote());

        // Verify deadline status
        assertEquals("deadline task", loadedTasks.get(2).getDescription());
        assertTrue(loadedTasks.get(2).isDone());
    }

    @Test
    public void saveTasksToFile_createsDataDirectory() throws IOException {
        String newDirPath = tempDir.resolve("new_directory").toString();
        Storage newStorage = new Storage(newDirPath, "test.txt");

        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(new Todo("test"));

        newStorage.saveTasksToFile(tasks);

        File dataDir = new File(newDirPath);
        assertTrue(dataDir.exists());
        assertTrue(dataDir.isDirectory());
    }
}
