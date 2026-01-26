package me.kanedenooijer.lttrs.database.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractJdbcDao<T, ID> implements GenericDaoInterface<T, ID> {

    protected final Connection connection;
    protected final String tableName;

    protected AbstractJdbcDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    protected abstract T mapResultSetToEntity(ResultSet rs) throws SQLException;

    @Override
    public Optional<T> findById(ID id) {
        String query = String.format("SELECT * FROM %s WHERE id = ?", tableName);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapResultSetToEntity(result));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error finding entity in %s: %s", tableName, e));
        }

        return Optional.empty();
    }

    @Override
    public List<T> findAll() {
        List<T> results = new ArrayList<>();

        try (Statement stmt = connection.createStatement(); ResultSet rs = stmt.executeQuery("SELECT * FROM " + tableName)) {
            while (rs.next()) {
                results.add(mapResultSetToEntity(rs));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error executing findAll on %s: %s", tableName, e));
        }

        return results;
    }

    @Override
    public boolean deleteById(ID id) {
        String query = String.format("DELETE FROM %s WHERE id = ?", tableName);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error deleting from %s: %s", tableName, e));
        }
    }
}
