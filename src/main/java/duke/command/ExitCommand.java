package duke.command;

import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;

/**
 * Command for exiting the application.
 * Doesn't do much except signal that it's time to shut down.
 */
public class ExitCommand extends Command {
    /**
     * Does nothing - exit commands don't need to perform any actions.
     * The main loop checks isExit() instead.
     */
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // No action needed for exit
    }
    
    /**
     * Returns true to signal that the application should terminate.
     * This is what actually makes the main loop stop.
     */
    @Override
    public boolean isExit() {
        return true;
    }
}
