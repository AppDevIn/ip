package duke;

import java.io.IOException;
import duke.storage.Storage;
import duke.storage.TaskList;
import duke.ui.Ui;
import duke.command.Command;
import duke.parser.Parser;
import duke.exception.DukeException;

/**
 * Main class for the Duke personal assistant chatbot application.
 * Handles initialization, command processing loop, and coordinates between UI, storage, and task management.
 */
public class Duke {
    
    private Storage storage;
    private TaskList tasks;
    private Ui ui;

    /**
     * Creates a new Duke instance with the specified file path for task storage.
     * Initializes the UI, storage, and attempts to load existing tasks.
     * 
     * @param filePath the name of the file to store tasks in
     */
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

    /**
     * Starts the main command processing loop for the Duke application.
     * Displays welcome message, processes user commands until exit, then shows goodbye message.
     */
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

    /**
     * Generates a response for the user's chat message.
     *
     * @param input The user's input message
     * @return Duke's response to the user input
     */
    public String getResponse(String input) {
        return "Duke heard: " + input;
    }

    /**
     * Main entry point for the Duke application.
     * 
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        new Duke("duke.txt").run();
    }
}