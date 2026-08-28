package et.parser;

import et.exception.ETException;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests parsing, displaying, and storing of ET date values.
 */
class DateTimeParserTest {
    @Test
    void parse_yearFirstDate_dateAtStartOfDayAndHasTimeFalse() throws ETException {
        DateTimeParser.ParsedDateTime parsedDateTime = DateTimeParser.parse("2019-1-5");

        assertEquals(LocalDateTime.of(2019, 1, 5, 0, 0), parsedDateTime.value());
        assertFalse(parsedDateTime.hasTime());
    }

    @Test
    void parse_dayFirstDate_dateAtStartOfDayAndHasTimeFalse() throws ETException {
        DateTimeParser.ParsedDateTime parsedDateTime = DateTimeParser.parse("5/1/2019");

        assertEquals(LocalDateTime.of(2019, 1, 5, 0, 0), parsedDateTime.value());
        assertFalse(parsedDateTime.hasTime());
    }

    @Test
    void parse_dateAndTime_dateTimeAndHasTimeTrue() throws ETException {
        DateTimeParser.ParsedDateTime parsedDateTime = DateTimeParser.parse("5/1/2019 0830");

        assertEquals(LocalDateTime.of(2019, 1, 5, 8, 30), parsedDateTime.value());
        assertTrue(parsedDateTime.hasTime());
    }

    @Test
    void parse_malformedOrImpossibleDate_etExceptionThrown() {
        assertThrows(ETException.class, () -> DateTimeParser.parse("not a date"));
        assertThrows(ETException.class, () -> DateTimeParser.parse("2019-2-29"));
        assertThrows(ETException.class, () -> DateTimeParser.parse("5/1/2019 2460"));
    }

    @Test
    void parseStored_dateOnly_dateAtStartOfDayAndHasTimeFalse() {
        DateTimeParser.ParsedDateTime parsedDateTime = DateTimeParser.parseStored("2019-01-05");

        assertEquals(LocalDateTime.of(2019, 1, 5, 0, 0), parsedDateTime.value());
        assertFalse(parsedDateTime.hasTime());
    }

    @Test
    void parseStored_dateAndTime_dateTimeAndHasTimeTrue() {
        DateTimeParser.ParsedDateTime parsedDateTime = DateTimeParser.parseStored("2019-01-05T08:30");

        assertEquals(LocalDateTime.of(2019, 1, 5, 8, 30), parsedDateTime.value());
        assertTrue(parsedDateTime.hasTime());
    }

    @Test
    void parseStored_malformedValue_illegalArgumentExceptionThrown() {
        assertThrows(IllegalArgumentException.class, () -> DateTimeParser.parseStored("not a date"));
        assertThrows(IllegalArgumentException.class, () -> DateTimeParser.parseStored("2019-02-29"));
        assertThrows(IllegalArgumentException.class, () -> DateTimeParser.parseStored("2019-01-05T24:00"));
    }

    @Test
    void format_dateOnly_displaysDateWithoutTime() {
        String displayedDate = DateTimeParser.format(LocalDateTime.of(2019, 1, 5, 8, 30), false);

        assertEquals("Jan 05 2019", displayedDate);
    }

    @Test
    void format_dateAndTime_displaysDateAndTime() {
        String displayedDate = DateTimeParser.format(LocalDateTime.of(2019, 12, 2, 18, 0), true);

        assertEquals("Dec 02 2019 6:00 PM", displayedDate);
    }

    @Test
    void formatForStorage_dateOnly_returnsIsoDate() {
        String storedDate = DateTimeParser.formatForStorage(LocalDateTime.of(2019, 1, 5, 8, 30), false);

        assertEquals("2019-01-05", storedDate);
    }

    @Test
    void formatForStorage_dateAndTime_returnsIsoDateAndTime() {
        String storedDate = DateTimeParser.formatForStorage(LocalDateTime.of(2019, 1, 5, 8, 30), true);

        assertEquals("2019-01-05T08:30", storedDate);
    }
}
