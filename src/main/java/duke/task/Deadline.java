package duke.task;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class Deadline extends Task {

    protected LocalDateTime by;

    public Deadline(String description, String by) throws DateTimeParseException {
        super(description);
        this.by = DateTimeParser.parseDateTime(by);
    }

    public Deadline(String description, LocalDateTime by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + DateTimeParser.formatForDisplay(by) + ")";
    }

    public LocalDateTime getBy() {
        return by;
    }

    @Override
    public String toJson() {
        return "{\"type\":\"D\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription()) + "\",\"by\":\"" + DateTimeParser.formatForJson(by) + "\"}";
    }

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