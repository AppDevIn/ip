package edith.task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * Comprehensive test suite for DateTimeParser class.
 * Tests various date formats, edge cases, and error conditions.
 */
public class DateTimeParserTest {

    @Test
    public void parseDateTime_yyyyMMddFormat_parsesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("2024-12-25");

        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_dMyyyyHHmmFormat_parsesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("25/12/2024 1430");

        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_singleDigitDay_parsesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("5/12/2024 0900");

        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(5, result.getDayOfMonth());
        assertEquals(9, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_singleDigitMonth_parsesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("25/1/2024 1200");

        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(12, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_midnightTime_parsesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("01/01/2024 0000");

        assertEquals(2024, result.getYear());
        assertEquals(1, result.getMonthValue());
        assertEquals(1, result.getDayOfMonth());
        assertEquals(0, result.getHour());
        assertEquals(0, result.getMinute());
    }

    @Test
    public void parseDateTime_lateEveningTime_parsesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("31/12/2024 2359");

        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(31, result.getDayOfMonth());
        assertEquals(23, result.getHour());
        assertEquals(59, result.getMinute());
    }

    @Test
    public void parseDateTime_invalidDateFormat_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("invalid-date");
        });

        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("2024/12/25");  // Wrong separator for yyyy format
        });

        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("25-12-2024");  // Wrong separator for d/m format
        });
    }

    @Test
    public void parseDateTime_invalidDayMonth_throwsException() {
        // Test truly invalid dates that should fail parsing
        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("99/99/2024 1200");  // Completely invalid
        });

        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("invalid/date/2024 1200");  // Non-numeric
        });
    }

    @Test
    public void parseDateTime_invalidTime_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("25/12/2024 invalid");  // Non-numeric time
        });

        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("25/12/2024");  // Missing time when expected
        });
    }

    @Test
    public void parseDateTime_nullInput_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime(null);
        });
    }

    @Test
    public void parseDateTime_emptyInput_throwsException() {
        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("");
        });

        assertThrows(DateTimeParseException.class, () -> {
            DateTimeParser.parseDateTime("   ");
        });
    }

    @Test
    public void parseDateTime_leadingTrailingSpaces_handlesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("  25/12/2024 1430  ");

        assertEquals(2024, result.getYear());
        assertEquals(12, result.getMonthValue());
        assertEquals(25, result.getDayOfMonth());
        assertEquals(14, result.getHour());
        assertEquals(30, result.getMinute());
    }

    @Test
    public void parseDateTime_leapYear_handlesCorrectly() {
        LocalDateTime result = DateTimeParser.parseDateTime("29/02/2024 1200");

        assertEquals(2024, result.getYear());
        assertEquals(2, result.getMonthValue());
        assertEquals(29, result.getDayOfMonth());
    }

    @Test
    public void parseDateTime_extremeDates_handlesCorrectly() {
        // Test minimum supported date
        LocalDateTime minResult = DateTimeParser.parseDateTime("01/01/1900 0000");
        assertEquals(1900, minResult.getYear());

        // Test far future date
        LocalDateTime maxResult = DateTimeParser.parseDateTime("31/12/2099 2359");
        assertEquals(2099, maxResult.getYear());
    }

    @Test
    public void parseDateTime_consistentParsing_sameInputSameOutput() {
        String dateString = "15/06/2024 1530";

        LocalDateTime result1 = DateTimeParser.parseDateTime(dateString);
        LocalDateTime result2 = DateTimeParser.parseDateTime(dateString);

        assertEquals(result1, result2);
    }

    @Test
    public void formatForDisplay_parsedDateTime_formatsCorrectly() {
        String original = "25/12/2024 1430";
        LocalDateTime parsed = DateTimeParser.parseDateTime(original);
        String formatted = DateTimeParser.formatForDisplay(parsed);

        // Should format consistently
        assertNotNull(formatted);
        assertTrue(formatted.contains("Dec"));
        assertTrue(formatted.contains("25"));
        assertTrue(formatted.contains("2024"));
    }
}
