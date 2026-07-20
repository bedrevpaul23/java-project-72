package hexlet.code.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDateTime;

import org.junit.jupiter.api.Test;

class UrlTest {
    @Test
    void urlPropertiesCanBeChanged() {
        var url = new Url("https://old.example");
        var createdAt = LocalDateTime.now();

        url.setId(1L);
        url.setName("https://example.com");
        url.setCreatedAt(createdAt);

        assertEquals(1L, url.getId());
        assertEquals("https://example.com", url.getName());
        assertEquals(createdAt, url.getCreatedAt());
    }
}
