package edith.task;

import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DurationParser {

    private static final int MINUTES_PER_HOUR = 60;

    private static final Pattern DURATION_PATTERN = Pattern.compile(
        "(?:(\\d+)\\s*h(?:ours?)?)?\\s*(?:(\\d+)\\s*m(?:inutes?)?)?",
        Pattern.CASE_INSENSITIVE
    );

    private static final Pattern SIMPLE_DURATION_PATTERN = Pattern.compile(
        "(\\d+(?:\\.\\d+)?)\\s*(h|hour|hours|m|min|mins|minute|minutes)",
        Pattern.CASE_INSENSITIVE
    );
    
    public static Duration parseDuration(String durationStr) {
        if (durationStr == null || durationStr.trim().isEmpty()) {
            return null;
        }
        
        String trimmed = durationStr.trim().toLowerCase();
        
        Matcher simpleMatcher = SIMPLE_DURATION_PATTERN.matcher(trimmed);
        if (simpleMatcher.matches()) {
            double value = Double.parseDouble(simpleMatcher.group(1));
            String unit = simpleMatcher.group(2);
            
            if (unit.startsWith("h")) {
                return Duration.ofMinutes((long) (value * MINUTES_PER_HOUR));
            } else {
                return Duration.ofMinutes((long) value);
            }
        }
        
        Matcher matcher = DURATION_PATTERN.matcher(trimmed);
        if (matcher.matches()) {
            long hours = 0;
            long minutes = 0;
            
            if (matcher.group(1) != null) {
                hours = Long.parseLong(matcher.group(1));
            }
            if (matcher.group(2) != null) {
                minutes = Long.parseLong(matcher.group(2));
            }
            
            if (hours == 0 && minutes == 0) {
                return null;
            }
            
            return Duration.ofHours(hours).plusMinutes(minutes);
        }
        
        try {
            long minutes = Long.parseLong(trimmed);
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid duration format: " + durationStr);
        }
    }
    
    public static String formatDuration(Duration duration) {
        if (duration == null) {
            return null;
        }
        
        long totalMinutes = duration.toMinutes();
        long hours = totalMinutes / 60;
        long minutes = totalMinutes % 60;
        
        if (hours > 0 && minutes > 0) {
            return hours + "h " + minutes + "m";
        } else if (hours > 0) {
            return hours + "h";
        } else {
            return minutes + "m";
        }
    }
    
    public static String formatDurationForJson(Duration duration) {
        if (duration == null) {
            return null;
        }
        return String.valueOf(duration.toMinutes());
    }
    
    public static Duration parseDurationFromJson(String jsonDuration) {
        if (jsonDuration == null || jsonDuration.trim().isEmpty()) {
            return null;
        }
        try {
            long minutes = Long.parseLong(jsonDuration.trim());
            return Duration.ofMinutes(minutes);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid JSON duration format: " + jsonDuration);
        }
    }
}