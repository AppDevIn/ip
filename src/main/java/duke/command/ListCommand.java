package duke.command;

import java.io.IOException;
import java.time.format.DateTimeParseException;
import duke.task.Task;
import duke.task.Todo;
import duke.task.Deadline;
import duke.task.Event;
import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;
import duke.exception.DukeException;

/**
 * Command for displaying all current tasks.
 * Simple and straightforward - just shows everything you've got.
 */
public class ListCommand extends Command {
    /**
     * Shows all tasks in a nice numbered list format.
     * No fancy filtering or sorting, just the raw list.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        ui.showLine();
        ui.showTaskList(tasks.getList());
        ui.showLine();
    }
}
