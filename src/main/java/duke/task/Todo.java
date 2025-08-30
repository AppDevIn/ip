package duke.task;
import java.io.IOException;

public class Todo extends Task {

    public Todo(String description) {
        super(description);
    }

    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    @Override
    public String toJson() {
        return "{\"type\":\"T\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription()) + "\"}";
    }

    public static Todo fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length != 2) continue;
                
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();
                
                switch (key) {
                    case "done":
                        isDone = Boolean.parseBoolean(value);
                        break;
                    case "description":
                        description = unescapeJson(value.substring(1, value.length() - 1));
                        break;
                }
            }
            
            if (description == null) {
                throw new IOException("Missing description field in Todo JSON: " + jsonLine);
            }
            
            Todo todo = new Todo(description);
            if (isDone) {
                todo.markAsDone();
            }
            
            return todo;
        } catch (Exception e) {
            throw new IOException("Failed to parse Todo JSON: " + jsonLine + " - " + e.getMessage());
        }
    }
}