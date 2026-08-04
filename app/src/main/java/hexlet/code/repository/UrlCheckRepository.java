package hexlet.code.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import hexlet.code.model.UrlCheck;

public final class UrlCheckRepository extends BaseRepository {
    private UrlCheckRepository() {
    }

    public static void save(UrlCheck urlCheck) {
        var sql = """
                INSERT INTO url_checks (url_id, status_code, h1, title, description, created_at)
                VALUES (?, ?, ?, ?, ?, ?)
                """;

        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var createdAt = LocalDateTime.now();

            preparedStatement.setLong(1, urlCheck.getUrlId());
            preparedStatement.setInt(2, urlCheck.getStatusCode());
            preparedStatement.setString(3, urlCheck.getH1());
            preparedStatement.setString(4, urlCheck.getTitle());
            preparedStatement.setString(5, urlCheck.getDescription());
            preparedStatement.setTimestamp(6, Timestamp.valueOf(createdAt));
            preparedStatement.executeUpdate();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    urlCheck.setId(generatedKeys.getLong(1));
                    urlCheck.setCreatedAt(createdAt);
                } else {
                    throw new RepositoryException(
                            "Database did not return a generated id while saving URL check"
                    );
                }
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to save URL check", exception);
        }
    }

    public static List<UrlCheck> findByUrlId(Long urlId) {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC, id DESC";
        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);

            try (var resultSet = preparedStatement.executeQuery()) {
                var result = new ArrayList<UrlCheck>();

                while (resultSet.next()) {
                    result.add(buildUrlCheck(resultSet));
                }

                return result;
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to find URL checks by URL id", exception);
        }
    }

    public static Optional<UrlCheck> findLatestByUrlId(Long urlId) {
        var sql = "SELECT * FROM url_checks WHERE url_id = ? ORDER BY created_at DESC, id DESC LIMIT 1";
        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, urlId);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUrlCheck(resultSet));
                }

                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to find latest URL check by URL id", exception);
        }
    }

    public static Map<Long, UrlCheck> findLatestChecks() {
        var sql = """
                SELECT id,
                       url_id,
                       status_code,
                       h1,
                       title,
                       description,
                       created_at
                FROM (
                    SELECT url_checks.*,
                           ROW_NUMBER() OVER (
                               PARTITION BY url_id
                               ORDER BY created_at DESC, id DESC
                           ) AS row_number
                    FROM url_checks
                ) AS ranked_checks
                WHERE row_number = 1
                """;

        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                var result = new HashMap<Long, UrlCheck>();

                while (resultSet.next()) {
                    var urlCheck = buildUrlCheck(resultSet);
                    result.put(urlCheck.getUrlId(), urlCheck);
                }

                return result;
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to find latest URL checks", exception);
        }
    }

    private static UrlCheck buildUrlCheck(ResultSet resultSet) throws SQLException {
        var urlId = resultSet.getLong("url_id");
        var statusCode = resultSet.getInt("status_code");
        var h1 = resultSet.getString("h1");
        var title = resultSet.getString("title");
        var description = resultSet.getString("description");
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        var urlCheck = new UrlCheck(urlId, statusCode, h1, title, description);
        urlCheck.setId(resultSet.getLong("id"));
        urlCheck.setCreatedAt(createdAt);

        return urlCheck;
    }
}
