import java.io.IOException;

public class Event extends Task {

    protected String from;
    protected String to;

    public Event(String description, String from, String to) {
        super(description);
        this.from = from;
        this.to = to;
    }

    @Override
    public String toString() {
        return "[E]" + super.toString() + " (from: " + from + " to: " + to + ")";
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    @Override
    public String toJson() {
        return "{\"type\":\"E\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription()) + "\",\"from\":\"" + escapeJson(from) + "\",\"to\":\"" + escapeJson(to) + "\"}";
    }

    public static Event fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            String from = null;
            String to = null;
            
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
                    case "from":
                        from = unescapeJson(value.substring(1, value.length() - 1));
                        break;
                    case "to":
                        to = unescapeJson(value.substring(1, value.length() - 1));
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
