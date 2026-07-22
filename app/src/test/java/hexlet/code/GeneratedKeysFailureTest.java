package hexlet.code;

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
import hexlet.code.repository.UrlCheckRepository;
import hexlet.code.repository.UrlRepository;
import org.junit.jupiter.api.Test;

class GeneratedKeysFailureTest {
    @Test
    void urlRepositoryThrowsWhenGeneratedKeyIsMissing() {
        BaseRepository.dataSource = dataSourceWithoutGeneratedKeys();

        assertThrows(
                SQLException.class,
                () -> UrlRepository.save(new Url("https://missing-key.example"))
        );
    }

    @Test
    void urlCheckRepositoryThrowsWhenGeneratedKeyIsMissing() {
        BaseRepository.dataSource = dataSourceWithoutGeneratedKeys();
        var urlCheck = new UrlCheck(1L, 200, "h1", "title", "description");

        assertThrows(SQLException.class, () -> UrlCheckRepository.save(urlCheck));
    }

    private static HikariDataSource dataSourceWithoutGeneratedKeys() {
        return new HikariDataSource() {
            @Override
            public Connection getConnection() {
                return connectionWithoutGeneratedKeys();
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
