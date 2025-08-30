package duke.task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Event extends Task {

    protected LocalDateTime from;
    protected LocalDateTime to;

    public Event(String description, String from, String to) throws DateTimeParseException {
        super(description);
        this.from = DateTimeParser.parseDateTime(from);
        this.to = DateTimeParser.parseDateTime(to);
    }

    public Event(String description, LocalDateTime from, LocalDateTime to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + DateTimeParser.formatForDisplay(from)
                + " to: " + DateTimeParser.formatForDisplay(to) + ")";
    }

    public LocalDateTime getFrom() {
        return from;
    }

    public LocalDateTime getTo() {
        return to;
    }

    @Override
    public String toJson() {
        return "{\"type\":\"E\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription())
                + "\",\"from\":\"" + DateTimeParser.formatForJson(from) + "\",\"to\":\""
                + DateTimeParser.formatForJson(to) + "\"}";
    }

    public static Event fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            LocalDateTime from = null;
            LocalDateTime to = null;
            
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
                }
            }
            
            if (description == null || from == null || to == null) {
                throw new IOException("Missing required fields in Event JSON: " + jsonLine);
            }
            
            Event event = new Event(description, from, to);
            if (isDone) {
                event.markAsDone();
            }
            
            return event;
        } catch (Exception e) {
            throw new IOException("Failed to parse Event JSON: " + jsonLine + " - " + e.getMessage());
        }
    }
}
