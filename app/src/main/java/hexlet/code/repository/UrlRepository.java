package hexlet.code.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import hexlet.code.model.Url;

public final class UrlRepository extends BaseRepository {
    private UrlRepository() {
    }

    public static void save(Url url) {
        var sql = "INSERT INTO urls (name, created_at) VALUES (?, ?)";
        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            var createdAt = LocalDateTime.now();

            preparedStatement.setString(1, url.getName());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(createdAt));
            preparedStatement.executeUpdate();

            try (var generatedKeys = preparedStatement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    url.setId(generatedKeys.getLong(1));
                    url.setCreatedAt(createdAt);
                } else {
                    throw new RepositoryException("Database did not return a generated id while saving URL");
                }
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to save URL", exception);
        }
    }

    public static Optional<Url> find(Long id) {
        var sql = "SELECT * FROM urls WHERE id = ?";
        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setLong(1, id);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUrl(resultSet));
                }

                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to find URL by id", exception);
        }
    }

    public static Optional<Url> findByName(String name) {
        var sql = "SELECT * FROM urls WHERE name = ?";
        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, name);

            try (var resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(buildUrl(resultSet));
                }

                return Optional.empty();
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to find URL by name", exception);
        }
    }

    public static List<Url> getEntities() {
        var sql = "SELECT * FROM urls ORDER BY created_at DESC, id DESC";
        try (var connection = dataSource.getConnection();
                var preparedStatement = connection.prepareStatement(sql)) {
            try (var resultSet = preparedStatement.executeQuery()) {
                var result = new ArrayList<Url>();

                while (resultSet.next()) {
                    result.add(buildUrl(resultSet));
                }

                return result;
            }
        } catch (SQLException exception) {
            throw new RepositoryException("Failed to get URLs", exception);
        }
    }

    private static Url buildUrl(ResultSet resultSet) throws SQLException {
        var id = resultSet.getLong("id");
        var name = resultSet.getString("name");
        var createdAt = resultSet.getTimestamp("created_at").toLocalDateTime();

        var url = new Url(name);
        url.setId(id);
        url.setCreatedAt(createdAt);

        return url;
    }
}
