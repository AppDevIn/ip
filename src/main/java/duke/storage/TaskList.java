package duke.storage;
import java.util.ArrayList;
import duke.task.Task;

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
        tasks.add(task);
    }

    /**
     * Removes and returns the task at the specified position.
     * 
     * @param index the position of the task to delete (0-based)
     * @return the task that was removed
     */
    public Task delete(int index) {
        return tasks.remove(index);
    }

    /**
     * Gets the task at the specified position without removing it.
     * 
     * @param index the position of the task (0-based)
     * @return the task at that position
     */
    public Task get(int index) {
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
        tasks.get(index).markAsDone();
    }

    /**
     * Marks the task at the given position as not completed.
     * 
     * @param index the position of the task to unmark (0-based)
     */
    public void unmarkTask(int index) {
        tasks.get(index).markAsUndone();
    }
}