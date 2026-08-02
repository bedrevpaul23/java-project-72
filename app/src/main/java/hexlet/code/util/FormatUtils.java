package hexlet.code.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public final class FormatUtils {
    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    private static final int MAX_DISPLAY_LENGTH = 200;
    private static final int TRUNCATED_TEXT_LENGTH = 197;

    private FormatUtils() {
    }

    public static String formatDate(LocalDateTime value) {
        return value == null ? "" : value.format(DATE_FORMATTER);
    }

    public static String formatDateTime(LocalDateTime value) {
        return value == null ? "" : value.format(DATE_TIME_FORMATTER);
    }

    public static String truncate(String value) {
        if (value == null) {
            return "";
        }

        return value.length() > MAX_DISPLAY_LENGTH
                ? value.substring(0, TRUNCATED_TEXT_LENGTH) + "..."
                : value;
    }
}
