import java.io.IOException;

public class Deadline extends Task {

    protected String by;

    public Deadline(String description, String by) {
        super(description);
        this.by = by;
    }

    @Override
    public String toString() {
        return "[D]" + super.toString() + " (by: " + by + ")";
    }

    public String getBy() {
        return by;
    }

    @Override
    public String toJson() {
        return "{\"type\":\"D\",\"done\":" + isDone() + ",\"description\":\"" + escapeJson(getDescription()) + "\",\"by\":\"" + escapeJson(by) + "\"}";
    }

    public static Deadline fromJson(String jsonLine) throws IOException {
        try {
            String json = jsonLine.trim();
            String content = json.substring(1, json.length() - 1);
            String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
            
            boolean isDone = false;
            String description = null;
            String by = null;
            
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
                        by = unescapeJson(value.substring(1, value.length() - 1));
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