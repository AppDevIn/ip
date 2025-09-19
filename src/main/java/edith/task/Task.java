package edith.task;
import java.io.IOException;
import java.time.Duration;

/**
 * Abstract base class representing a task in the E.D.I.T.H. task management system.
 * Provides common functionality for all types of tasks including completion status,
 * description management, and JSON serialization support.
 */
public abstract class Task {
    protected String description;
    protected boolean isDone;
    protected Duration duration;
    protected String note;

    /**
     * Creates a new task with the specified description.
     * The task is initially marked as not done.
     *
     * @param description the description of the task
     */
    public Task(String description) {
        assert description != null : "Task description cannot be null";
        assert !description.trim().isEmpty() : "Task description cannot be empty or whitespace only";
        this.description = description;
        this.isDone = false;
        this.duration = null;
        this.note = "";
    }

    /**
     * Returns the status icon for this task.
     *
     * @return "X" if the task is done, " " (space) if not done
     */
    public String getStatusIcon() {
        return (isDone ? "X" : " ");
    }

    /**
     * Marks this task as completed.
     */
    public void markAsDone() {
        this.isDone = true;
    }

    /**
     * Marks this task as not completed.
     */
    public void markAsUndone() {
        this.isDone = false;
    }

    /**
     * Returns the description of this task.
     *
     * @return the task description
     */
    public String getDescription() {
        return description;
    }

    /**
     * Checks if this task is marked as done.
     *
     * @return true if the task is completed, false otherwise
     */
    public boolean isDone() {
        return isDone;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void setDuration(String durationStr) {
        this.duration = DurationParser.parseDuration(durationStr);
    }

    /**
     * Returns the note associated with this task.
     *
     * @return the task note, empty string if no note is set
     */
    public String getNote() {
        return note;
    }

    /**
     * Sets a note for this task.
     *
     * @param note the note to associate with this task
     */
    public void setNote(String note) {
        this.note = note == null ? "" : note;
    }

    /**
     * Checks if this task has a note.
     *
     * @return true if the task has a non-empty note, false otherwise
     */
    public boolean hasNote() {
        return note != null && !note.trim().isEmpty();
    }

    @Override
    public String toString() {
        String baseString = "[" + getStatusIcon() + "] " + description;
        if (hasNote()) {
            baseString += " (Note: " + note + ")";
        }
        if (duration != null) {
            baseString += " (duration: " + DurationParser.formatDuration(duration) + ")";
        }
        return baseString;
    }

    /**
     * Converts this task to its JSON string representation for storage.
     * Each subclass must implement this method to serialize its specific attributes.
     *
     * @return the JSON string representation of this task
     */
    public abstract String toJson();

    /**
     * Converts a JSON string representation to a Task object.
     * Parses the JSON to determine the task type and delegates to the appropriate subclass.
     *
     * AI-Assisted: Method refactored Sonnet to break down
     * a 40+ line method into smaller, focused helper methods for better maintainability.
     *
     * @param jsonLine the JSON string representing a task
     * @return the Task object created from the JSON string
     * @throws IOException if the JSON is malformed or contains an unkno`wn task type
     */
    public static Task convertFromJson(String jsonLine) throws IOException {
        assert jsonLine != null : "JSON line cannot be null";
        try {
            validateJsonFormat(jsonLine);
            String content = extractJsonContent(jsonLine.trim());
            String type = findTaskType(content);
            return createTaskFromType(type, jsonLine);
        } catch (Exception e) {
            throw new IOException("Failed to parse JSON: " + jsonLine + " - " + e.getMessage());
        }
    }

    /**
     * Validates that the JSON string has proper format with curly braces.
     *
     * @param jsonLine the JSON string to validate
     * @throws IOException if the JSON format is invalid
     */
    private static void validateJsonFormat(String jsonLine) throws IOException {
        String json = jsonLine.trim();
        if (!json.startsWith("{") || !json.endsWith("}")) {
            throw new IOException("Invalid JSON format: " + jsonLine);
        }
    }

    /**
     * Extracts the content between the curly braces from a JSON string.
     *
     * @param json the trimmed JSON string
     * @return the content between the braces
     */
    private static String extractJsonContent(String json) {
        return json.substring(1, json.length() - 1);
    }

    /**
     * Finds and returns the task type from the JSON content.
     *
     * @param content the JSON content without braces
     * @return the task type string
     * @throws IOException if the type field is missing
     */
    private static String findTaskType(String content) throws IOException {
        String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length != 2) {
                continue;
            }

            String key = keyValue[0].trim().replace("\"", "");
            if ("type".equals(key)) {
                return keyValue[1].trim().replace("\"", "");
            }
        }

        throw new IOException("Missing type field in JSON");
    }

    /**
     * Creates the appropriate Task subclass based on the type string.
     *
     * @param type the task type identifier
     * @param jsonLine the original JSON string for delegation
     * @return the created Task object
     * @throws IOException if the task type is unknown
     */
    private static Task createTaskFromType(String type, String jsonLine) throws IOException {
        switch (type) {
        case "T":
            return Todo.convertFromJson(jsonLine);
        case "D":
            return Deadline.convertFromJson(jsonLine);
        case "E":
            return Event.convertFromJson(jsonLine);
        default:
            throw new IOException("Unknown task type: " + type);
        }
    }

    /**
     * Escapes special characters in a string for JSON serialization.
     * Handles backslashes, quotes, and whitespace characters.
     *
     * @param str the string to escape
     * @return the escaped string safe for JSON
     */
    protected static String escapeJson(String str) {
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Unescapes JSON-escaped characters back to their original form.
     * Reverses the escaping done by escapeJson method.
     *
     * @param str the JSON-escaped string
     * @return the unescaped string
     */
    protected static String unescapeJson(String str) {
        return str.replace("\\\"", "\"")
                  .replace("\\\\", "\\")
                  .replace("\\n", "\n")
                  .replace("\\r", "\r")
                  .replace("\\t", "\t");
    }
}
