package edith.task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Represents a task that has a specific deadline.
 * Because some things actually do need to be done by a certain time.
 */
public class Deadline extends Task {

    protected LocalDateTime by;

    /**
     * Creates a deadline task by parsing the time string.
     * 
     * @param description what needs to be done
     * @param by the deadline as a string (gets parsed into a proper DateTime)
     * @throws DateTimeParseException if the time string is gibberish
     */
    public Deadline(String description, String by) throws DateTimeParseException {
        super(description);
        this.by = DateTimeParser.parseDateTime(by);
    }

    /**
     * Creates a deadline task with an already-parsed DateTime.
     * Useful when loading from storage or when you already have a proper DateTime.
     * 
     * @param description what needs to be done
     * @param by the deadline as a LocalDateTime object
     */
    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    /**
     * Returns a nicely formatted string showing the task and its deadline.
     * 
     * @return formatted string like "[D][X] task description (by: date)"
     */
    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeParser.formatForDisplay(by) + ")";
    }

    /**
     * Gets the deadline time for this task.
     * 
     * @return the deadline as a LocalDateTime
     */
    public LocalDateTime getBy() {
        return by;
    }

    /**
     * Converts this deadline to JSON format for storage.
     * 
     * @return JSON string with type, done status, description, and deadline
     */
    @Override
    public String toJson() {
        return "{\"type\":\"D\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription())
                + "\",\"by\":\"" + DateTimeParser.formatForJson(by) + "\"}";
    }

    /**
     * Creates a Deadline task from its JSON representation.
     * Parses all the fields and reconstructs the original task.
     * 
     * @param jsonLine the JSON string to parse
     * @return a new Deadline task with the parsed data
     * @throws IOException if the JSON is malformed or missing required fields
     */
    public static Deadline fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            LocalDateTime by = null;
            
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
                    case "by":
                        String byString = unescapeJson(value.substring(1, value.length() - 1));
                        by = DateTimeParser.parseFromJson(byString);
                        break;
                }
            }
            
            if (description == null || by == null) {
                throw new IOException("Missing required fields in Deadline JSON: " + jsonLine);
            }
            
            Deadline deadline = new Deadline(description, by);
            if (isDone) {
                deadline.markAsDone();
            }
            
            return deadline;
        } catch (Exception e) {
            throw new IOException("Failed to parse Deadline JSON: " + jsonLine + " - " + e.getMessage());
        }
    }
}