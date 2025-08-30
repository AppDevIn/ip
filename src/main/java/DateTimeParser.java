import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public class DateTimeParser {
    private static final DateTimeFormatter INPUT_FORMAT_1 = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter INPUT_FORMAT_2 = DateTimeFormatter.ofPattern("d/M/yyyy HHmm");
    private static final DateTimeFormatter OUTPUT_FORMAT = DateTimeFormatter.ofPattern("MMM dd yyyy h:mm a");

    public static LocalDateTime parseDateTime(String dateTimeString) throws DateTimeParseException {
        String trimmed = dateTimeString.trim();
        
        try {
            if (trimmed.matches("\\d{4}-\\d{2}-\\d{2}")) {
                return LocalDateTime.parse(trimmed + " 00:00", DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
            }
            
            if (trimmed.matches("\\d{1,2}/\\d{1,2}/\\d{4} \\d{4}")) {
                return LocalDateTime.parse(trimmed, INPUT_FORMAT_2);
            }
            
            throw new DateTimeParseException("Unsupported date format: " + dateTimeString, dateTimeString, 0);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Invalid date format: " + dateTimeString + 
                ". Supported formats: yyyy-mm-dd, d/m/yyyy HHmm", dateTimeString, 0);
        }
    }

    public static String formatForDisplay(LocalDateTime dateTime) {
        if (dateTime.getHour() == 0 && dateTime.getMinute() == 0) {
            return dateTime.format(DateTimeFormatter.ofPattern("MMM dd yyyy"));
        }
        return dateTime.format(OUTPUT_FORMAT);
    }

    public static String formatForJson(LocalDateTime dateTime) {
        return dateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }

    public static LocalDateTime parseFromJson(String jsonDateTime) throws DateTimeParseException {
        return LocalDateTime.parse(jsonDateTime, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
    }
}