package edith.task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.Map;

/**
 * Represents an event that happens during a specific time period.
 * Has both a start and end time, unlike deadlines which just have one target date.
 */
public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    /**
     * Creates an event by parsing the start and end time strings.
     * 
     * @param description what the event is about
     * @param from when it starts (as a string to be parsed)
     * @param to when it ends (as a string to be parsed)
     * @throws DateTimeParseException if either time string is unparseable
     */
    public Event(String description, String from, String to) throws DateTimeParseException {
        super(description);
        this.from = DateTimeParser.parseDateTime(from);
        this.to = DateTimeParser.parseDateTime(to);
    }

    /**
     * Creates an event with already-parsed start and end times.
     * 
     * @param description what the event is about
     * @param from when it starts (as LocalDateTime)
     * @param to when it ends (as LocalDateTime)
     */
    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    /**
     * Returns a formatted string showing the event and its time range.
     * 
     * @return formatted string like "[E][X] event description (from: start to: end)"
     */
    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeParser.formatForDisplay(from)
                + " to: " + DateTimeParser.formatForDisplay(to) + ")";
    }

    /**
     * Gets the start time of this event.
     * 
     * @return when the event starts
     */
    public LocalDateTime getFrom() {
        return from;
    }

    /**
     * Gets the end time of this event.
     * 
     * @return when the event ends
     */
    public LocalDateTime getTo() {
        return to;
    }

    /**
     * Converts this event to JSON format for storage.
     * 
     * @return JSON string with type, done status, description, from time, and to time
     */
    @Override
    public String toJson() {
        return "{\"type\":\"E\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription())
                + "\",\"from\":\"" + DateTimeParser.formatForJson(from) + "\",\"to\":\""
                + DateTimeParser.formatForJson(to) + "\",\"note\":\"" + escapeJson(getNote()) + "\"}";
    }

    /**
     * Creates an Event task from its JSON representation.
     * Parses all the fields including both start and end times.
     *
     * @param jsonLine the JSON string to parse
     * @return a new Event task with the parsed data
     * @throws IOException if the JSON is malformed or missing required fields
     */
    public static Event convertFromJson(String jsonLine) throws IOException {
        try {
            EventFields fields = parseEventFields(jsonLine);
            validateEventFields(fields, jsonLine);
            return createEventFromFields(fields);
        } catch (Exception e) {
            throw new IOException("Failed to parse Event JSON: " + jsonLine + " - " + e.getMessage());
        }
    }

    /**
     * Parses JSON string to extract Event-specific fields.
     *
     * @param jsonLine the JSON string to parse
     * @return EventFields object containing parsed values
     */
    private static EventFields parseEventFields(String jsonLine) {
        Map<String, String> fieldMap = JsonParser.parseJsonToMap(jsonLine);
        EventFields fields = new Event.EventFields();

        fields.isDone = JsonParser.extractBooleanValue(fieldMap.get("done"));
        fields.description = JsonParser.extractStringValue(fieldMap.get("description"));
        fields.note = JsonParser.extractStringValue(fieldMap.get("note"));

        String fromString = JsonParser.extractStringValue(fieldMap.get("from"));
        if (!fromString.isEmpty()) {
            fields.from = DateTimeParser.parseFromJson(fromString);
        }

        String toString = JsonParser.extractStringValue(fieldMap.get("to"));
        if (!toString.isEmpty()) {
            fields.to = DateTimeParser.parseFromJson(toString);
        }

        return fields;
    }

    /**
     * Validates that all required fields are present for Event creation.
     *
     * @param fields the parsed fields to validate
     * @param jsonLine the original JSON for error reporting
     * @throws IOException if required fields are missing
     */
    private static void validateEventFields(EventFields fields, String jsonLine) throws IOException {
        if (fields.description == null || fields.from == null || fields.to == null) {
            throw new IOException("Missing required fields in Event JSON: " + jsonLine);
        }
    }

    /**
     * Creates and configures an Event object from parsed fields.
     *
     * @param fields the parsed and validated fields
     * @return configured Event object
     */
    private static Event createEventFromFields(EventFields fields) {
        Event event = new Event(fields.description, fields.from, fields.to);
        if (fields.isDone) {
            event.markAsDone();
        }
        event.setNote(fields.note);
        return event;
    }

    /**
     * Helper class to hold parsed Event field values.
     */
    private static class EventFields {
        boolean isDone = false;
        String description = null;
        LocalDateTime from = null;
        LocalDateTime to = null;
        String note = "";
    }
}
