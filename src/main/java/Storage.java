import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Storage {
    private static final String DEFAULT_DATA_DIR = "data";
    private static final String DEFAULT_FILE_NAME = "duke.txt";
    
    private final String dataDir;
    private final String fullPath;

    public Storage() {
        this(DEFAULT_DATA_DIR, DEFAULT_FILE_NAME);
    }

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

    public void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        createDataDirectoryIfNotExists();
        
        try (FileWriter writer = new FileWriter(fullPath)) {
            for (Task task : tasks) {
                writer.write(task.toJson() + System.lineSeparator());
            }
        }
    }

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
