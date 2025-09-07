package edith.task;
import java.io.IOException;
import java.time.Duration;

/**
 * Represents a todo task with a description that can be marked as done or undone.
 * This is the simplest type of task with no additional time constraints.
 */
public class Todo extends Task {

    /**
     * Creates a new Todo task with the specified description.
     * The task is initially marked as not done.
     * 
     * @param description the description of the todo task
     */
    public Todo(String description) {
        super(description);
    }

    /**
     * Returns a string representation of the todo task in the format "[T][status] description".
     * 
     * @return formatted string representation of the todo task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Converts the todo task to its JSON string representation for storage.
     * 
     * @return JSON string containing the task type, completion status, and description
     */
    @Override
    public String toJson() {
        String json = "{\"type\":\"T\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription()) + "\"";
        if (getDuration() != null) {
            json += ",\"duration\":\"" + DurationParser.formatDurationForJson(getDuration()) + "\"";
        }
        json += "}";
        return json;
    }

    /**
     * Creates a Todo task from its JSON string representation.
     * Parses the JSON to extract task completion status and description.
     * 
     * @param jsonLine the JSON string representation of the todo task
     * @return a Todo object created from the JSON data
     * @throws IOException if the JSON format is invalid or parsing fails
     */
    public static Todo fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            Duration duration = null;
            
            for (String pair : pairs) {
                String[] keyValue = pair.split(":", 2);
                if (keyValue.length != 2) {
                    continue;
                }
                
                String key = keyValue[0].trim().replace("\"", "");
                String value = keyValue[1].trim();
                
                switch (key) {
                    case "done":
                        isDone = Boolean.parseBoolean(value);
                        break;
                    case "description":
                        description = unescapeJson(value.substring(1, value.length() - 1));
                        break;
                    case "duration":
                        String durationString = unescapeJson(value.substring(1, value.length() - 1));
                        duration = DurationParser.parseDurationFromJson(durationString);
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
            if (duration != null) {
                todo.setDuration(duration);
            }
            
            return todo;
        } catch (Exception e) {
            throw new IOException("Failed to parse Todo JSON: " + jsonLine + " - " + e.getMessage());
        }
    }
}