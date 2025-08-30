package duke.command;

import java.util.ArrayList;
import java.util.regex.Pattern;
import duke.storage.Storage;
import duke.storage.TaskList;
import duke.task.Task;
import duke.ui.Ui;
import duke.exception.DukeException;

/**
 * Command for finding tasks that contain a specific keyword in their description.
 * Uses regex pattern matching to search through task descriptions case-insensitively.
 */
public class FindCommand extends Command {
    private String keyword;

    /**
     * Creates a FindCommand with the user input containing the search keyword.
     *
     * @param input the full command input from the user (e.g., "find book")
     */
    public FindCommand(String input) {
        this.keyword = input.substring(4).trim();
    }

    /**
     * Executes the find command by searching for tasks containing the keyword.
     * Uses regex pattern matching to find all tasks whose descriptions contain
     * the keyword (case-insensitive search).
     *
     * @param tasks the task list to search through
     * @param ui the user interface for displaying results
     * @param storage the storage system (not used in find operations)
     * @throws DukeException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws DukeException {
        ArrayList<Task> matchingTasks = new ArrayList<>();
        ArrayList<Integer> originalIndices = new ArrayList<>();

        Pattern pattern = Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE);

        ArrayList<Task> allTasks = tasks.getList();
        for (int i = 0; i < allTasks.size(); i++) {
            Task task = allTasks.get(i);
            if (pattern.matcher(task.getDescription()).matches()) {
                matchingTasks.add(task);
                originalIndices.add(i + 1);
            }
        }

        ui.showLine();
        ui.showFoundTasks(matchingTasks, originalIndices);
        ui.showLine();
    }
}
