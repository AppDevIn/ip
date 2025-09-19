package edith.command;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.task.Task;
import edith.ui.Ui;
import edith.exception.EdithException;

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
        String[] parts = input.split(" ", 2);
        this.keyword = parts.length > 1 ? parts[1] : "";
    }

    /**
     * Executes the find command by searching for tasks containing the keyword.
     * Uses regex pattern matching to find all tasks whose descriptions contain
     * the keyword (case-insensitive search).
     *
     * @param tasks the task list to search through
     * @param ui the user interface for displaying results
     * @param storage the storage system (not used in find operations)
     * @throws EdithException if an error occurs during execution
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) throws EdithException {
        Pattern pattern = Pattern.compile(".*" + Pattern.quote(keyword) + ".*", Pattern.CASE_INSENSITIVE);
        ArrayList<Task> allTasks = tasks.getList();

        List<Integer> matchingIndices = IntStream.range(0, allTasks.size())
                .filter(i -> pattern.matcher(allTasks.get(i).getDescription()).matches())
                .boxed()
                .collect(Collectors.toList());

        ArrayList<Task> matchingTasks = matchingIndices.stream()
                .map(allTasks::get)
                .collect(Collectors.toCollection(ArrayList::new));

        ArrayList<Integer> originalIndices = matchingIndices.stream()
                .map(i -> i + 1)
                .collect(Collectors.toCollection(ArrayList::new));

        ui.displayLineSeparator();
        ui.showFoundTasks(matchingTasks, originalIndices);
        ui.displayLineSeparator();
    }
}
