package hexlet.code.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UrlCheckTest {
    @Test
    void urlCheckPropertiesCanBeChanged() {
        var longText = "a".repeat(210);
        var createdAt = LocalDateTime.of(2026, 2, 20, 10, 15);
        var urlCheck = new UrlCheck(1L, 200, longText, "Old title", "Old description");

        urlCheck.setId(2L);
        urlCheck.setUrlId(3L);
        urlCheck.setStatusCode(201);
        urlCheck.setH1("New h1");
        urlCheck.setTitle(longText);
        urlCheck.setDescription("New description");
        urlCheck.setCreatedAt(createdAt);

        assertEquals(2L, urlCheck.getId());
        assertEquals(3L, urlCheck.getUrlId());
        assertEquals(201, urlCheck.getStatusCode());
        assertEquals("New h1", urlCheck.getH1());
        assertEquals(longText, urlCheck.getTitle());
        assertEquals("New description", urlCheck.getDescription());
        assertEquals(createdAt, urlCheck.getCreatedAt());
        assertEquals("2026-02-20", urlCheck.getCreatedAtAsDate());
        assertEquals(200, urlCheck.getTitleForDisplay().length());
        assertEquals("a".repeat(197) + "...", urlCheck.getTitleForDisplay());
    }

    @Test
    void nullValuesAreRenderedAsEmptyStrings() {
        var urlCheck = new UrlCheck(1L, 200, null, null, null);

        assertEquals("", urlCheck.getH1ForDisplay());
        assertEquals("", urlCheck.getTitleForDisplay());
        assertEquals("", urlCheck.getDescriptionForDisplay());
    }
}
