package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import hexlet.code.model.Url;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UrlRepositoryTest {
    @BeforeEach
    void setUp() throws Exception {
        var databaseUrl = "jdbc:h2:mem:repository_test_" + System.nanoTime() + ";DB_CLOSE_DELAY=-1;";
        App.initDatabase(databaseUrl);
    }

    @Test
    void repositoryReturnsEmptyList() throws Exception {
        var urls = UrlRepository.getEntities();

        assertTrue(urls.isEmpty());
    }

    @Test
    void repositoryCanBeInstantiated() throws Exception {
        var constructor = UrlRepository.class.getDeclaredConstructor();
        constructor.setAccessible(true);

        assertNotNull(constructor.newInstance());
    }

    @Test
    void urlCanBeSavedAndFound() throws Exception {
        var url = new Url("https://hexlet.io");

        UrlRepository.save(url);
        var savedUrl = UrlRepository.find(url.getId()).orElseThrow();

        assertNotNull(url.getId());
        assertNotNull(url.getCreatedAt());
        assertNotNull(savedUrl.getCreatedAt());
        assertEquals(url.getId(), savedUrl.getId());
        assertEquals(url.getName(), savedUrl.getName());
    }

    @Test
    void urlCanBeFoundByName() throws Exception {
        var url = new Url("https://example.com");

        UrlRepository.save(url);
        var savedUrl = UrlRepository.findByName(url.getName()).orElseThrow();

        assertEquals(url.getId(), savedUrl.getId());
        assertEquals(url.getName(), savedUrl.getName());
    }

    @Test
    void repositoryReturnsSavedEntitiesNewestFirst() throws Exception {
        var firstUrl = new Url("https://first.example");
        var secondUrl = new Url("https://second.example");

        UrlRepository.save(firstUrl);
        UrlRepository.save(secondUrl);
        var urls = UrlRepository.getEntities();

        assertEquals(2, urls.size());
        assertEquals(secondUrl.getName(), urls.get(0).getName());
        assertEquals(firstUrl.getName(), urls.get(1).getName());
        assertNotNull(urls.get(0).getCreatedAt());
    }

    @Test
    void repositoryReturnsEmptyOptionalForMissingEntity() throws Exception {
        assertTrue(UrlRepository.find(999L).isEmpty());
    }

    @Test
    void repositoryReturnsEmptyOptionalForMissingName() throws Exception {
        assertTrue(UrlRepository.findByName("https://missing.example").isEmpty());
    }
}
