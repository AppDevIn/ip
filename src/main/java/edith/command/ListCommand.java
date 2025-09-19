package edith.command;

import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.Ui;

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
        ui.displayLineSeparator();
        ui.showTaskList(tasks.getList());
        ui.displayLineSeparator();
    }
}
