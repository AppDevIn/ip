package edith.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.time.Duration;

public class DurationParserTest {
    
    @Test
    public void parseDuration_validHoursOnly_returnsDuration() {
        Duration result = DurationParser.parseDuration("2h");
        assertEquals(120, result.toMinutes());
        
        result = DurationParser.parseDuration("3 hours");
        assertEquals(180, result.toMinutes());
        
        result = DurationParser.parseDuration("1 hour");
        assertEquals(60, result.toMinutes());
    }
    
    @Test
    public void parseDuration_validMinutesOnly_returnsDuration() {
        Duration result = DurationParser.parseDuration("30m");
        assertEquals(30, result.toMinutes());
        
        result = DurationParser.parseDuration("45 minutes");
        assertEquals(45, result.toMinutes());
        
        result = DurationParser.parseDuration("1 min");
        assertEquals(1, result.toMinutes());
    }
    
    @Test
    public void parseDuration_hoursAndMinutes_returnsDuration() {
        Duration result = DurationParser.parseDuration("2h 30m");
        assertEquals(150, result.toMinutes());
        
        result = DurationParser.parseDuration("1 hour 15 minutes");
        assertEquals(75, result.toMinutes());
    }
    
    @Test
    public void parseDuration_decimalHours_returnsDuration() {
        Duration result = DurationParser.parseDuration("2.5h");
        assertEquals(150, result.toMinutes());
        
        result = DurationParser.parseDuration("1.25 hours");
        assertEquals(75, result.toMinutes());
    }
    
    @Test
    public void parseDuration_plainNumber_returnsMinutes() {
        Duration result = DurationParser.parseDuration("90");
        assertEquals(90, result.toMinutes());
        
        result = DurationParser.parseDuration("120");
        assertEquals(120, result.toMinutes());
    }
    
    @Test
    public void parseDuration_nullOrEmpty_returnsNull() {
        assertNull(DurationParser.parseDuration(null));
        assertNull(DurationParser.parseDuration(""));
        assertNull(DurationParser.parseDuration("   "));
    }
    
    @Test
    public void parseDuration_invalidFormat_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            DurationParser.parseDuration("invalid"));
        assertThrows(IllegalArgumentException.class, () -> 
            DurationParser.parseDuration("2x"));
    }
    
    @Test
    public void formatDuration_validDuration_returnsFormattedString() {
        Duration duration = Duration.ofMinutes(150);
        assertEquals("2h 30m", DurationParser.formatDuration(duration));
        
        duration = Duration.ofMinutes(120);
        assertEquals("2h", DurationParser.formatDuration(duration));
        
        duration = Duration.ofMinutes(45);
        assertEquals("45m", DurationParser.formatDuration(duration));
    }
    
    @Test
    public void formatDuration_nullDuration_returnsNull() {
        assertNull(DurationParser.formatDuration(null));
    }
    
    @Test
    public void formatDurationForJson_validDuration_returnsMinutesString() {
        Duration duration = Duration.ofMinutes(150);
        assertEquals("150", DurationParser.formatDurationForJson(duration));
        
        duration = Duration.ofMinutes(90);
        assertEquals("90", DurationParser.formatDurationForJson(duration));
    }
    
    @Test
    public void parseDurationFromJson_validJson_returnsDuration() {
        Duration result = DurationParser.parseDurationFromJson("150");
        assertEquals(150, result.toMinutes());
        
        result = DurationParser.parseDurationFromJson("90");
        assertEquals(90, result.toMinutes());
    }
    
    @Test
    public void parseDurationFromJson_invalidJson_throwsException() {
        assertThrows(IllegalArgumentException.class, () -> 
            DurationParser.parseDurationFromJson("invalid"));
    }
}