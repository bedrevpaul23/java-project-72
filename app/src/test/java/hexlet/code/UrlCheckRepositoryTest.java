package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlCheckRepositoryTest {
    @BeforeEach
    void setUp() throws Exception {
        var databaseUrl = "jdbc:h2:mem:url_check_repository_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1;";
        App.initDatabase(databaseUrl);
    }

    @Test
    void repositoryCanBeInstantiated() throws Exception {
        var constructor = UrlCheckRepository.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertNotNull(constructor.newInstance());
    }

    @Test
    void urlCheckCanBeSavedAndFound() throws Exception {
        var url = new Url("https://example.com");
        UrlRepository.save(url);
        var urlCheck = new UrlCheck(url.getId(), 200, "Header", "Title", "Description");

        UrlCheckRepository.save(urlCheck);
        var checks = UrlCheckRepository.findByUrlId(url.getId());

        assertEquals(1, checks.size());
        assertNotNull(urlCheck.getId());
        assertNotNull(urlCheck.getCreatedAt());
        assertEquals(url.getId(), checks.get(0).getUrlId());
        assertEquals(200, checks.get(0).getStatusCode());
        assertEquals("Header", checks.get(0).getH1());
        assertEquals("Title", checks.get(0).getTitle());
        assertEquals("Description", checks.get(0).getDescription());
    }

    @Test
    void latestUrlCheckCanBeFound() throws Exception {
        var url = new Url("https://latest.example");
        UrlRepository.save(url);
        var firstCheck = new UrlCheck(url.getId(), 200, "First", "First", "First");
        var secondCheck = new UrlCheck(url.getId(), 201, "Second", "Second", "Second");

        UrlCheckRepository.save(firstCheck);
        UrlCheckRepository.save(secondCheck);
        var latestCheck = UrlCheckRepository.findLatestByUrlId(url.getId()).orElseThrow();

        assertEquals(secondCheck.getId(), latestCheck.getId());
        assertEquals(201, latestCheck.getStatusCode());
    }

    @Test
    void repositoryReturnsEmptyResultsForMissingUrl() throws Exception {
        assertTrue(UrlCheckRepository.findByUrlId(999L).isEmpty());
        assertTrue(UrlCheckRepository.findLatestByUrlId(999L).isEmpty());
    }
}
