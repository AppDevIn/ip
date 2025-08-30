package duke.storage;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import duke.task.Task;

/**
 * Handles saving and loading tasks from the file system.
 * Creates the data directory automatically and deals with all the file I/O messiness.
 */
public class Storage {
    private static final String DEFAULT_DATA_DIR = "data";
    private static final String DEFAULT_FILE_NAME = "duke.txt";
    
    private final String dataDir;
    private final String fullPath;

    /**
     * Creates storage with default directory and filename.
     * Uses 'data/duke.txt' as the default location.
     */
    public Storage() {
        this(DEFAULT_DATA_DIR, DEFAULT_FILE_NAME);
    }

    /**
     * Creates storage with custom directory and filename.
     * Sets up the data directory if it doesn't exist yet.
     * 
     * @param dataDir the directory to store files in
     * @param fileName the name of the file to use
     */
    public Storage(String dataDir, String fileName) {
        this.dataDir = dataDir;
        this.fullPath = dataDir + File.separator + fileName;
        createDataDirectoryIfNotExists();
    }

    private void createDataDirectoryIfNotExists() {
        File dataDirFile = new File(dataDir);
        if (!dataDirFile.exists()) {
            dataDirFile.mkdirs();
        }
    }

    /**
     * Saves all tasks to the file in JSON format.
     * Overwrites whatever was there before, so make sure you really want to do this.
     * 
     * @param tasks the list of tasks to save
     * @throws IOException if something goes wrong with file writing
     */
    public void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        createDataDirectoryIfNotExists();
        
        try (FileWriter writer = new FileWriter(fullPath)) {
            for (Task task : tasks) {
                writer.write(task.toJson() + System.lineSeparator());
            }
        }
    }

    /**
     * Loads all tasks from the file, if it exists.
     * Returns an empty list if the file doesn't exist yet (first run).
     * 
     * @return list of tasks loaded from storage
     * @throws IOException if the file exists but can't be read properly
     */
    public ArrayList<Task> loadTasksFromFile() throws IOException {
        ArrayList<Task> tasks = new ArrayList<>();
        File file = new File(fullPath);
        
        if (!file.exists()) {
            return tasks;
        }

        try {
            List<String> lines = Files.readAllLines(Paths.get(fullPath));
            for (String line : lines) {
                if (!line.trim().isEmpty()) {
                    Task task = Task.fromJson(line);
                    if (task != null) {
                        tasks.add(task);
                    }
                }
            }
        } catch (Exception e) {
            throw new IOException("Error reading tasks from file: " + e.getMessage());
        }
        
        return tasks;
    }
}
