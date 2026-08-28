package et.parser;

import et.exception.ETException;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.time.format.ResolverStyle;
import java.util.Locale;

/**
 * Parses, formats, and serialises the date values accepted by ET commands.
 */
public final class DateTimeParser {
    /** The year-first date-only format accepted in commands. */
    private static final DateTimeFormatter INPUT_YEAR_FIRST_DATE_FORMAT = DateTimeFormatter
            .ofPattern("uuuu-M-d")
            .withResolverStyle(ResolverStyle.STRICT);

    /** The day-first date-only format accepted in commands. */
    private static final DateTimeFormatter INPUT_DAY_FIRST_DATE_FORMAT = DateTimeFormatter
            .ofPattern("d/M/uuuu")
            .withResolverStyle(ResolverStyle.STRICT);

    /** The date-and-time format accepted in commands. */
    private static final DateTimeFormatter INPUT_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("d/M/uuuu HHmm")
            .withResolverStyle(ResolverStyle.STRICT);

    /** The format used when showing a date without a time. */
    private static final DateTimeFormatter OUTPUT_DATE_FORMAT = DateTimeFormatter
            .ofPattern("MMM dd uuuu", Locale.ENGLISH);

    /** The format used when showing a date and time. */
    private static final DateTimeFormatter OUTPUT_DATE_TIME_FORMAT = DateTimeFormatter
            .ofPattern("MMM dd uuuu h:mm a", Locale.ENGLISH);

    /** Prevents instantiation of this utility class. */
    private DateTimeParser() {
    }

    /**
     * Parses a command date in one of ET's supported formats.
     *
     * @param input the date text provided after a command marker
     * @return the parsed value and whether the input included a time
     * @throws ETException if the input is not a valid supported date
     */
    public static ParsedDateTime parse(String input) throws ETException {
        try {
            LocalDate date = LocalDate.parse(input, INPUT_YEAR_FIRST_DATE_FORMAT);
            return new ParsedDateTime(date.atStartOfDay(), false);
        } catch (DateTimeParseException ignored) {
            // The input may use the day-first date format instead.
        }

        try {
            LocalDate date = LocalDate.parse(input, INPUT_DAY_FIRST_DATE_FORMAT);
            return new ParsedDateTime(date.atStartOfDay(), false);
        } catch (DateTimeParseException ignored) {
            // The input may be a date and time instead.
        }

        try {
            return new ParsedDateTime(LocalDateTime.parse(input, INPUT_DATE_TIME_FORMAT), true);
        } catch (DateTimeParseException e) {
            throw new ETException("Please use yyyy-M-d or d/M/yyyy, optionally followed by HHmm, "
                    + "for example 2019-1-5, 2/1/2019, or 2/12/2019 1800.");
        }
    }

    /**
     * Recreates a saved date value, whose presence of {@code T} identifies a time.
     *
     * @param storedValue the ISO date or ISO date-time saved by ET
     * @return the reconstructed value and whether it includes a time
     * @throws IllegalArgumentException if the saved value is malformed
     */
    public static ParsedDateTime parseStored(String storedValue) {
        try {
            if (storedValue.contains("T")) {
                return new ParsedDateTime(LocalDateTime.parse(storedValue), true);
            }
            return new ParsedDateTime(LocalDate.parse(storedValue).atStartOfDay(), false);
        } catch (DateTimeException e) {
            throw new IllegalArgumentException("Invalid saved date", e);
        }
    }

    /**
     * Formats a date value for task-list output.
     *
     * @param dateTime the date value to display
     * @param hasTime whether the user supplied a time
     * @return a human-readable date or date and time
     */
    public static String format(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.format(OUTPUT_DATE_TIME_FORMAT) : dateTime.format(OUTPUT_DATE_FORMAT);
    }

    /**
     * Converts a date value into the stable ISO text used in storage.
     *
     * @param dateTime the date value to save
     * @param hasTime whether the value includes a time
     * @return the ISO date or ISO date-time value
     */
    public static String formatForStorage(LocalDateTime dateTime, boolean hasTime) {
        return hasTime ? dateTime.toString() : dateTime.toLocalDate().toString();
    }

    /**
     * Holds a parsed date value together with whether its input included a time.
     *
     * @param value the parsed date and time; date-only values use midnight
     * @param hasTime whether the original input included a time
     */
    public record ParsedDateTime(LocalDateTime value, boolean hasTime) {
    }
}
