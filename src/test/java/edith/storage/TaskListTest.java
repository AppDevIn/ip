package edith.storage;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import edith.task.Task;
import edith.task.Todo;
import edith.task.Deadline;
import edith.task.Event;
import edith.task.DateTimeParser;

/**
 * Comprehensive test suite for TaskList class.
 * Tests task management operations, edge cases, and boundary conditions.
 */
public class TaskListTest {

    private TaskList taskList;

    @BeforeEach
    public void setUp() {
        taskList = new TaskList();
    }

    @Test
    public void constructor_noParameters_createsEmptyList() {
        assertEquals(0, taskList.size());
        assertTrue(taskList.getList().isEmpty());
    }

    @Test
    public void constructor_withArrayList_initializesCorrectly() {
        ArrayList<Task> initialTasks = new ArrayList<>();
        initialTasks.add(new Todo("initial task"));

        TaskList taskListWithTasks = new TaskList(initialTasks);

        assertEquals(1, taskListWithTasks.size());
        assertEquals("initial task", taskListWithTasks.get(0).getDescription());
    }

    @Test
    public void add_singleTask_increasesSize() {
        Todo task = new Todo("test task");

        taskList.add(task);

        assertEquals(1, taskList.size());
        assertEquals(task, taskList.get(0));
    }

    @Test
    public void add_multipleTasks_maintainsOrder() {
        Todo task1 = new Todo("first task");
        Todo task2 = new Todo("second task");
        Todo task3 = new Todo("third task");

        taskList.add(task1);
        taskList.add(task2);
        taskList.add(task3);

        assertEquals(3, taskList.size());
        assertEquals(task1, taskList.get(0));
        assertEquals(task2, taskList.get(1));
        assertEquals(task3, taskList.get(2));
    }

    @Test
    public void delete_validIndex_removesTask() {
        taskList.add(new Todo("task to remove"));
        taskList.add(new Todo("task to keep"));

        Task removedTask = taskList.delete(0);

        assertEquals(1, taskList.size());
        assertEquals("task to remove", removedTask.getDescription());
        assertEquals("task to keep", taskList.get(0).getDescription());
    }

    @Test
    public void delete_invalidIndex_throwsException() {
        taskList.add(new Todo("only task"));

        assertThrows(AssertionError.class, () -> {
            taskList.delete(5);
        });

        assertThrows(AssertionError.class, () -> {
            taskList.delete(-1);
        });
    }

    @Test
    public void get_validIndex_returnsCorrectTask() {
        Todo expectedTask = new Todo("target task");
        taskList.add(new Todo("first task"));
        taskList.add(expectedTask);
        taskList.add(new Todo("third task"));

        Task actualTask = taskList.get(1);

        assertEquals(expectedTask, actualTask);
        assertEquals("target task", actualTask.getDescription());
    }

    @Test
    public void get_invalidIndex_throwsException() {
        taskList.add(new Todo("only task"));

        assertThrows(AssertionError.class, () -> {
            taskList.get(5);
        });

        assertThrows(AssertionError.class, () -> {
            taskList.get(-1);
        });
    }

    @Test
    public void size_emptyList_returnsZero() {
        assertEquals(0, taskList.size());
    }

    @Test
    public void size_afterAddingTasks_returnsCorrectCount() {
        assertEquals(0, taskList.size());

        taskList.add(new Todo("task 1"));
        assertEquals(1, taskList.size());

        taskList.add(new Todo("task 2"));
        assertEquals(2, taskList.size());

        taskList.add(new Todo("task 3"));
        assertEquals(3, taskList.size());
    }

    @Test
    public void size_afterRemovingTasks_returnsCorrectCount() {
        taskList.add(new Todo("task 1"));
        taskList.add(new Todo("task 2"));
        taskList.add(new Todo("task 3"));
        assertEquals(3, taskList.size());

        taskList.delete(1);
        assertEquals(2, taskList.size());

        taskList.delete(0);
        assertEquals(1, taskList.size());

        taskList.delete(0);
        assertEquals(0, taskList.size());
    }

    @Test
    public void getList_returnsInternalList() {
        Todo task1 = new Todo("task 1");
        Todo task2 = new Todo("task 2");
        taskList.add(task1);
        taskList.add(task2);

        ArrayList<Task> internalList = taskList.getList();

        assertEquals(2, internalList.size());
        assertEquals(task1, internalList.get(0));
        assertEquals(task2, internalList.get(1));
    }

    @Test
    public void mixedTaskTypes_handlesCorrectly() {
        Todo todo = new Todo("todo task");
        Deadline deadline = new Deadline("deadline task",
                DateTimeParser.parseDateTime("01/01/2024 1200"));
        Event event = new Event("event task",
                DateTimeParser.parseDateTime("01/01/2024 1400"),
                DateTimeParser.parseDateTime("01/01/2024 1600"));

        taskList.add(todo);
        taskList.add(deadline);
        taskList.add(event);

        assertEquals(3, taskList.size());
        assertTrue(taskList.get(0) instanceof Todo);
        assertTrue(taskList.get(1) instanceof Deadline);
        assertTrue(taskList.get(2) instanceof Event);
    }

    @Test
    public void taskModification_reflectedInList() {
        Todo task = new Todo("modifiable task");
        taskList.add(task);

        // Modify task after adding to list
        task.markAsDone();
        task.setNote("added note");

        // Verify modifications are reflected
        assertTrue(taskList.get(0).isDone());
        assertEquals("added note", taskList.get(0).getNote());
    }

    @Test
    public void edgeCases_emptyOperations() {
        // Test operations on empty list
        assertEquals(0, taskList.size());
        assertTrue(taskList.getList().isEmpty());

        assertThrows(AssertionError.class, () -> {
            taskList.get(0);
        });

        assertThrows(AssertionError.class, () -> {
            taskList.delete(0);
        });
    }
}
