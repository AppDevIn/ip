package edith.task;
import java.io.IOException;
import java.time.Duration;
import java.util.Map;

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
        json += ",\"note\":\"" + escapeJson(getNote()) + "\"}";
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
            TodoFields fields = parseTodoFields(jsonLine);
            validateTodoFields(fields, jsonLine);
            return createTodoFromFields(fields);
        } catch (Exception e) {
            throw new IOException("Failed to parse Todo JSON: " + jsonLine + " - " + e.getMessage());
        }
    }

    /**
     * Parses JSON string to extract Todo-specific fields.
     *
     * @param jsonLine the JSON string to parse
     * @return TodoFields object containing parsed values
     */
    private static TodoFields parseTodoFields(String jsonLine) {
        Map<String, String> fieldMap = JsonParser.parseJsonToMap(jsonLine);
        TodoFields fields = new Todo.TodoFields();

        fields.isDone = JsonParser.extractBooleanValue(fieldMap.get("done"));
        fields.description = JsonParser.extractStringValue(fieldMap.get("description"));
        fields.note = JsonParser.extractStringValue(fieldMap.get("note"));

        String durationString = JsonParser.extractStringValue(fieldMap.get("duration"));
        if (!durationString.isEmpty()) {
            fields.duration = DurationParser.parseDurationFromJson(durationString);
        }

        return fields;
    }

    /**
     * Validates that all required fields are present for Todo creation.
     *
     * @param fields the parsed fields to validate
     * @param jsonLine the original JSON for error reporting
     * @throws IOException if required fields are missing
     */
    private static void validateTodoFields(TodoFields fields, String jsonLine) throws IOException {
        if (fields.description == null) {
            throw new IOException("Missing description field in Todo JSON: " + jsonLine);
        }
    }

    /**
     * Creates and configures a Todo object from parsed fields.
     *
     * @param fields the parsed and validated fields
     * @return configured Todo object
     */
    private static Todo createTodoFromFields(TodoFields fields) {
        Todo todo = new Todo(fields.description);
        if (fields.isDone) {
            todo.markAsDone();
        }
        if (fields.duration != null) {
            todo.setDuration(fields.duration);
        }
        todo.setNote(fields.note);
        return todo;
    }

    /**
     * Helper class to hold parsed Todo field values.
     */
    private static class TodoFields {
        boolean isDone = false;
        String description = null;
        Duration duration = null;
        String note = "";
    }
}
