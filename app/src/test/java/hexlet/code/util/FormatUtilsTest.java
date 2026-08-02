package hexlet.code.util;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class FormatUtilsTest {
    @Test
    void datesAreFormattedAndNullSafe() {
        var value = LocalDateTime.of(2026, 2, 20, 10, 15);

        assertEquals("2026-02-20", FormatUtils.formatDate(value));
        assertEquals("2026-02-20 10:15", FormatUtils.formatDateTime(value));
        assertEquals("", FormatUtils.formatDate(null));
        assertEquals("", FormatUtils.formatDateTime(null));
    }

    @Test
    void textIsTruncatedAndNullSafe() {
        assertEquals("", FormatUtils.truncate(null));
        assertEquals("a".repeat(200), FormatUtils.truncate("a".repeat(200)));
        assertEquals("a".repeat(197) + "...", FormatUtils.truncate("a".repeat(201)));
    }
}
