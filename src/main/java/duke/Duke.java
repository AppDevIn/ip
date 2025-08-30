package duke;

import java.io.IOException;
import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;
import duke.command.Command;
import duke.parser.Parser;
import duke.exception.DukeException;

public class Duke {
    
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    public Duke(String filePath) {
        ui = new Ui();
        storage = new Storage("data", filePath);
        try {
            tasks = new TaskList(storage.loadTasksFromFile());
        } catch (IOException e) {
            ui.showError("Could not load saved tasks. " + e.getMessage());
            tasks = new TaskList();
        }
    }

    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommand();
                Command c = Parser.parse(fullCommand, tasks.size());
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (DukeException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
        ui.close();
    }

    public static void main(String[] args) {
        new Duke("duke.txt").run();
    }
}