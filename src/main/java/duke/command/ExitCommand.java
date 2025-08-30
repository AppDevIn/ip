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

public class ExitCommand extends Command {
    @Override
    public void execute(TaskList tasks, Ui ui, Storage storage) {
        // No action needed for exit
    }
    
    @Override
    public boolean isExit() {
        return true;
    }
}
