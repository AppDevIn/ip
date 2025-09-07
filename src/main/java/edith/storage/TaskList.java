package edith.storage;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import edith.task.Task;

/**
 * Manages a collection of tasks with handy methods for common operations.
 * Basically a wrapper around ArrayList with task-specific functionality.
 */
public class TaskList {
    private ArrayList<Task> tasks;

    /**
     * Creates an empty task list ready for action.
     */
    public TaskList() {
        this.tasks = new ArrayList<>();
    }

    /**
     * Creates a task list from an existing ArrayList of tasks.
     * Useful when loading tasks from storage.
     * 
     * @param tasks the list of tasks to manage
     */
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }

    /**
     * Adds a new task to the list.
     * 
     * @param task the task to add
     */
    public void add(Task task) {
        assert task != null : "Cannot add null task to list";
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified position.
     * 
     * @param index the position of the task to delete (0-based)
     * @return the task that was removed
     */
    public Task delete(int index) {
        assert index >= 0 && index < tasks.size() : "Delete index out of bounds: " + index + ", size: " + tasks.size();
        return tasks.remove(index);
    }

    /**
     * Gets the task at the specified position without removing it.
     * 
     * @param index the position of the task (0-based)
     * @return the task at that position
     */
    public Task get(int index) {
        assert index >= 0 && index < tasks.size() : "Get index out of bounds: " + index + ", size: " + tasks.size();
        return tasks.get(index);
    }

    /**
     * Returns how many tasks are currently in the list.
     * 
     * @return the number of tasks
     */
    public int size() {
        return tasks.size();
    }

    /**
     * Returns the underlying ArrayList for when you need direct access.
     * 
     * @return the internal task list
     */
    public ArrayList<Task> getList() {
        return tasks;
    }

    /**
     * Marks the task at the given position as completed.
     * 
     * @param index the position of the task to mark (0-based)
     */
    public void markTask(int index) {
        assert index >= 0 && index < tasks.size() : "Mark index out of bounds: " + index + ", size: " + tasks.size();
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the given position as not completed.
     * 
     * @param index the position of the task to unmark (0-based)
     */
    public void unmarkTask(int index) {
        assert index >= 0 && index < tasks.size() : "Unmark index out of bounds: " + index + ", size: " + tasks.size();
        tasks.get(index).markAsUndone();
    }

    /**
     * Returns a stream of all tasks for functional programming operations.
     * 
     * @return a stream of tasks
     */
    public Stream<Task> stream() {
        return tasks.stream();
    }

    /**
     * Filters tasks based on a predicate using streams.
     * 
     * @param predicate the condition to filter tasks by
     * @return a list of tasks matching the predicate
     */
    public List<Task> filter(java.util.function.Predicate<Task> predicate) {
        return tasks.stream()
                .filter(predicate)
                .collect(Collectors.toList());
    }

    /**
     * Counts the number of completed tasks using streams.
     * 
     * @return the number of completed tasks
     */
    public long countCompleted() {
        return tasks.stream()
                .filter(Task::isDone)
                .count();
    }

    /**
     * Counts the number of pending tasks using streams.
     * 
     * @return the number of pending tasks
     */
    public long countPending() {
        return tasks.stream()
                .filter(task -> !task.isDone())
                .count();
    }
}