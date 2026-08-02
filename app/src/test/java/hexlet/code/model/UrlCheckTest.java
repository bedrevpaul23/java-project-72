package hexlet.code.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UrlCheckTest {
    @Test
    void urlCheckPropertiesCanBeChanged() {
        var createdAt = LocalDateTime.of(2026, 2, 20, 10, 15);
        var urlCheck = new UrlCheck(1L, 200, "Old h1", "Old title", "Old description");

        urlCheck.setId(2L);
        urlCheck.setUrlId(3L);
        urlCheck.setStatusCode(201);
        urlCheck.setH1("New h1");
        urlCheck.setTitle("New title");
        urlCheck.setDescription("New description");
        urlCheck.setCreatedAt(createdAt);

        assertEquals(2L, urlCheck.getId());
        assertEquals(3L, urlCheck.getUrlId());
        assertEquals(201, urlCheck.getStatusCode());
        assertEquals("New h1", urlCheck.getH1());
        assertEquals("New title", urlCheck.getTitle());
        assertEquals("New description", urlCheck.getDescription());
        assertEquals(createdAt, urlCheck.getCreatedAt());
    }
}
