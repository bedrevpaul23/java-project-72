package hexlet.code;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.zaxxer.hikari.HikariDataSource;
import hexlet.code.model.Url;
import hexlet.code.model.UrlCheck;
import hexlet.code.repository.BaseRepository;
import hexlet.code.repository.RepositoryException;
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GeneratedKeysFailureTest {
    private HikariDataSource dataSource;

    @BeforeEach
    void setUp() {
        dataSource = dataSourceWithoutGeneratedKeys();
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
    void urlRepositoryThrowsWhenGeneratedKeyIsMissing() {
        var exception = assertThrows(
                RepositoryException.class,
                () -> UrlRepository.save(new Url("https://missing-key.example"))
        );

        assertEquals(
                "Database did not return a generated id while saving URL",
                exception.getMessage()
        );
        assertNull(exception.getCause());
    }

    @Test
    void urlCheckRepositoryThrowsWhenGeneratedKeyIsMissing() {
        var urlCheck = new UrlCheck(1L, 200, "h1", "title", "description");

        var exception = assertThrows(
                RepositoryException.class,
                () -> UrlCheckRepository.save(urlCheck)
        );

        assertEquals(
                "Database did not return a generated id while saving URL check",
                exception.getMessage()
        );
        assertNull(exception.getCause());
    }

    @Test
    void urlRepositoryPreservesJdbcFailureAsCause() {
        var originalDataSource = BaseRepository.dataSource;
        var failingDataSource = dataSourceWithConnectionFailure();

        try {
            BaseRepository.dataSource = failingDataSource;

            var exception = assertThrows(
                    RepositoryException.class,
                    () -> UrlRepository.save(new Url("https://database-error.example"))
            );

            assertEquals("Failed to save URL", exception.getMessage());
            var cause = assertInstanceOf(SQLException.class, exception.getCause());
            assertEquals("Connection failed", cause.getMessage());
        } finally {
            BaseRepository.dataSource = originalDataSource;
            failingDataSource.close();
        }
    }

    private static HikariDataSource dataSourceWithoutGeneratedKeys() {
        return new HikariDataSource() {
            @Override
            public Connection getConnection() {
                return connectionWithoutGeneratedKeys();
            }
        };
    }

    private static HikariDataSource dataSourceWithConnectionFailure() {
        return new HikariDataSource() {
            @Override
            public Connection getConnection() throws SQLException {
                throw new SQLException("Connection failed");
            }
        };
    }

    private static Connection connectionWithoutGeneratedKeys() {
        return proxy(Connection.class, (object, method, args) -> {
            if ("prepareStatement".equals(method.getName())) {
                return preparedStatementWithoutGeneratedKeys();
            }

            return defaultValue(method.getReturnType());
        });
    }

    private static PreparedStatement preparedStatementWithoutGeneratedKeys() {
        return proxy(PreparedStatement.class, (object, method, args) -> {
            if ("executeUpdate".equals(method.getName())) {
                return 1;
            }

            if ("getGeneratedKeys".equals(method.getName())) {
                return emptyResultSet();
            }

            return defaultValue(method.getReturnType());
        });
    }

    private static ResultSet emptyResultSet() {
        return proxy(ResultSet.class, (object, method, args) -> {
            if ("next".equals(method.getName())) {
                return false;
            }

            return defaultValue(method.getReturnType());
        });
    }

    @SuppressWarnings("unchecked")
    private static <T> T proxy(Class<T> type, InvocationHandler invocationHandler) {
        return (T) Proxy.newProxyInstance(
                type.getClassLoader(),
                new Class<?>[] {type},
                invocationHandler
        );
    }

    private static Object defaultValue(Class<?> type) {
        if (!type.isPrimitive()) {
            return null;
        }

        if (type == boolean.class) {
            return false;
        }

        if (type == int.class) {
            return 0;
        }

        if (type == long.class) {
            return 0L;
        }

        if (type == double.class) {
            return 0.0;
        }

        if (type == float.class) {
            return 0.0F;
        }

        if (type == byte.class) {
            return (byte) 0;
        }

        if (type == short.class) {
            return (short) 0;
        }

        if (type == char.class) {
            return (char) 0;
        }

        return null;
    }
}
