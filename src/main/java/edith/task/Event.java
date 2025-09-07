package edith.task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

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
    public static Event fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            LocalDateTime from = null;
            LocalDateTime to = null;
            String note = "";
            
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
                    case "from":
                        String fromString = unescapeJson(value.substring(1, value.length() - 1));
                        from = DateTimeParser.parseFromJson(fromString);
                        break;
                    case "to":
                        String toString = unescapeJson(value.substring(1, value.length() - 1));
                        to = DateTimeParser.parseFromJson(toString);
                        break;
                    case "note":
                        note = unescapeJson(value.substring(1, value.length() - 1));
                        break;
                }
            }
            
            if (description == null || from == null || to == null) {
                throw new IOException("Missing required fields in Event JSON: " + jsonLine);
            }
            
            Event event = new Event(description, from, to);
            if (isDone) {
                event.markAsDone();
            }
            event.setNote(note);
            
            return event;
        } catch (Exception e) {
            throw new IOException("Failed to parse Event JSON: " + jsonLine + " - " + e.getMessage());
        }
    }
}
