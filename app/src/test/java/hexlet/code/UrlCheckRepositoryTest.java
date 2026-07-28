package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlCheckRepositoryTest {
    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() throws Exception {
        var databaseUrl =
                "jdbc:h2:mem:url_check_repository_test_"
                        + System.nanoTime()
                        + ";DB_CLOSE_DELAY=-1;";

        dataSource = App.initDatabase(databaseUrl);
        BaseRepository.dataSource = dataSource;
    }

    @AfterEach
    void tearDown() {
        dataSource.close();

        if (BaseRepository.dataSource == dataSource) {
            BaseRepository.dataSource = null;
        }
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
    void latestChecksCanBeFoundWithSingleQuery() throws Exception {
        var firstUrl = new Url("https://first-latest.example");
        var secondUrl = new Url("https://second-latest.example");
        UrlRepository.save(firstUrl);
        UrlRepository.save(secondUrl);

        UrlCheckRepository.save(new UrlCheck(firstUrl.getId(), 200, "First", "First", "First"));
        UrlCheckRepository.save(new UrlCheck(firstUrl.getId(), 201, "Latest", "Latest", "Latest"));
        UrlCheckRepository.save(new UrlCheck(secondUrl.getId(), 204, "", "", ""));

        var latestChecks = UrlCheckRepository.findLatestChecks();

        assertEquals(2, latestChecks.size());
        assertEquals(201, latestChecks.get(firstUrl.getId()).getStatusCode());
        assertEquals(204, latestChecks.get(secondUrl.getId()).getStatusCode());
    }

    @Test
    void repositoryReturnsEmptyResultsForMissingUrl() throws Exception {
        assertTrue(UrlCheckRepository.findByUrlId(999L).isEmpty());
        assertTrue(UrlCheckRepository.findLatestByUrlId(999L).isEmpty());
    }
}
