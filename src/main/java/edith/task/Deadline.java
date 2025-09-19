package edith.task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

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
                + "\",\"by\":\"" + DateTimeParser.formatForJson(by) + "\",\"note\":\"" + escapeJson(getNote()) + "\"}";
    }

    /**
     * Creates a Deadline task from its JSON representation.
     * Parses all the fields and reconstructs the original task.
     *
     * @param jsonLine the JSON string to parse
     * @return a new Deadline task with the parsed data
     * @throws IOException if the JSON is malformed or missing required fields
     */
    public static Deadline convertFromJson(String jsonLine) throws IOException {
        try {
            DeadlineFields fields = parseDeadlineFields(jsonLine);
            validateDeadlineFields(fields, jsonLine);
            return createDeadlineFromFields(fields);
        } catch (Exception e) {
            throw new IOException("Failed to parse Deadline JSON: " + jsonLine + " - " + e.getMessage());
        }
    }

    /**
     * Parses JSON string to extract Deadline-specific fields.
     *
     * @param jsonLine the JSON string to parse
     * @return DeadlineFields object containing parsed values
     */
    private static DeadlineFields parseDeadlineFields(String jsonLine) {
        Map<String, String> fieldMap = JsonParser.parseJsonToMap(jsonLine);
        DeadlineFields fields = new Deadline.DeadlineFields();

        fields.isDone = JsonParser.extractBooleanValue(fieldMap.get("done"));
        fields.description = JsonParser.extractStringValue(fieldMap.get("description"));
        fields.note = JsonParser.extractStringValue(fieldMap.get("note"));

        String byString = JsonParser.extractStringValue(fieldMap.get("by"));
        if (!byString.isEmpty()) {
            fields.by = DateTimeParser.parseFromJson(byString);
        }

        return fields;
    }

    /**
     * Validates that all required fields are present for Deadline creation.
     *
     * @param fields the parsed fields to validate
     * @param jsonLine the original JSON for error reporting
     * @throws IOException if required fields are missing
     */
    private static void validateDeadlineFields(DeadlineFields fields, String jsonLine) throws IOException {
        if (fields.description == null || fields.by == null) {
            throw new IOException("Missing required fields in Deadline JSON: " + jsonLine);
        }
    }

    /**
     * Creates and configures a Deadline object from parsed fields.
     *
     * @param fields the parsed and validated fields
     * @return configured Deadline object
     */
    private static Deadline createDeadlineFromFields(DeadlineFields fields) {
        Deadline deadline = new Deadline(fields.description, fields.by);
        if (fields.isDone) {
            deadline.markAsDone();
        }
        deadline.setNote(fields.note);
        return deadline;
    }

    /**
     * Helper class to hold parsed Deadline field values.
     */
    private static class DeadlineFields {
        boolean isDone = false;
        String description = null;
        LocalDateTime by = null;
        String note = "";
    }
}