package duke.task;
import java.io.IOException;

public abstract class Task {
    protected String description;
    protected boolean isDone;

    public Task(String description) {
        this.description = description;
        this.isDone = false;
    }

    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    public void markAsDone() {
        this.isDone = true;
    }

    public void markAsUndone() {
        this.isDone = false;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDone() {
        return isDone;
    }

    @Override
    public String toString() {
        return "[" + getStatusIcon() + "] " + description;
    }

    public abstract String toJson();

    public static Task fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            if (!json.startsWith("{") || !json.endsWith("}")) {
                throw new IOException("Invalid JSON format: " + jsonLine);
            }
            
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            String type = null;
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length != 2) continue;
                
                String key = keyValue[0].trim().replace("\"", "");
                if ("type".equals(key)) {
                    type = keyValue[1].trim().replace("\"", "");
                    break;
                }
            }
            
            if (type == null) {
                throw new IOException("Missing type field in JSON: " + jsonLine);
            }
            
            switch (type) {
                case "T":
                    return Todo.fromJson(jsonLine);
                case "D":
                    return Deadline.fromJson(jsonLine);
                case "E":
                    return Event.fromJson(jsonLine);
                default:
                    throw new IOException("Unknown task type: " + type);
            }
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON: " + jsonLine + " - " + e.getMessage());
        }
    }

    protected static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    protected static String unescapeJson(String str) {
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }
}
