package edith.command;

import java.io.IOException;
import java.util.ArrayList;
import edith.storage.Storage;
import edith.task.Task;

public class FailingStorage extends Storage {
    public FailingStorage() {
        super("test", "test.txt");
    }

    @Override
    public void saveTasksToFile(ArrayList<Task> tasks) throws IOException {
        throw new IOException("Simulated storage failure");
    }
}