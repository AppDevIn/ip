package duke.command;

import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;

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
