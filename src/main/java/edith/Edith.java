package edith;

import java.io.IOException;
import edith.storage.Storage;
import edith.storage.TaskList;
import edith.ui.GuiUi;
import edith.ui.Ui;
import edith.command.Command;
import edith.parser.Parser;
import edith.exception.EdithException;

/**
 * Main class for the E.D.I.T.H. personal assistant chatbot application.
 * Handles initialization, command processing loop, and coordinates between UI, storage, and task management.
 */
public class Edith {

    private Storage storage;
    private TaskList tasks;
    private Ui ui;
    private boolean shouldExit = false;

    /**
     * Creates a new E.D.I.T.H. instance with the specified file path for task storage.
     * Initializes the UI, storage, and attempts to load existing tasks.
     *
     * @param filePath the name of the file to store tasks in
     */
    public Edith(String filePath) {
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
     * Starts the main command processing loop for the E.D.I.T.H. application.
     * Displays welcome message, processes user commands until exit, then shows goodbye message.
     */
    public void run() {
        ui.showWelcome();
        boolean isExit = false;
        while (!isExit) {
            try {
                String fullCommand = ui.readCommandFromTerminal();
                Command c = Parser.parse(fullCommand, tasks.size());
                c.execute(tasks, ui, storage);
                isExit = c.isExit();
            } catch (EdithException e) {
                ui.showError(e.getMessage());
            }
        }
        ui.showGoodbye();
        ui.close();
    }

    /**
     * Generates a response for the user's chat message using the command processing system.
     * This method processes the input through the parser and executes the appropriate command,
     * returning the response that would be displayed to the user.
     *
     * @param input The user's input message
     * @return Edith's response to the user input
     */
    public String getResponse(String input) {
        try {
            Command c = Parser.parse(input, tasks.size());

            GuiUi guiUi = new GuiUi();
            c.execute(tasks, guiUi, storage);

            shouldExit = c.isExit();

            return guiUi.getResponse();

        } catch (EdithException e) {
            return "Error: " + e.getMessage();
        } catch (Exception e) {
            return "An unexpected error occurred: " + e.getMessage();
        }
    }

    /**
     * Checks if the last processed command was an exit command.
     *
     * @return true if the application should exit, false otherwise
     */
    public boolean shouldExit() {
        return shouldExit;
    }

    /**
     * Main entry point for the E.D.I.T.H. application.
     *
     * @param args command line arguments - optional file path for data storage
     */
    public static void main(String[] args) {
        String filePath = "edith.txt";

        if (args.length > 0) {
            filePath = args[0];
            if (filePath.trim().isEmpty()) {
                System.err.println("Error: File path cannot be empty");
                System.exit(1);
            }
        }

        try {
            new Edith(filePath).run();
        } catch (Exception e) {
            System.err.println("Fatal error: " + e.getMessage());
            System.exit(1);
        }
    }
}
