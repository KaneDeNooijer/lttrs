package me.kanedenooijer.lttrs.database.dao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractDao<T extends Record> {

    /**
     * The database connection.
     */
    protected final Connection connection;

    /**
     * The record this DAO manages.
     */
    protected final String tableName;

    protected AbstractDao(Connection connection, String tableName) {
        this.connection = connection;
        this.tableName = tableName;
    }

    /**
     * Maps a ResultSet row to an entity of type T.
     *
     * @param resultSet the ResultSet to map
     * @return an entity of type T mapped from the ResultSet
     * @throws SQLException if a database access error occurs while mapping the ResultSet
     */
    protected abstract T mapResultSetToEntity(ResultSet resultSet) throws SQLException;

    /**
     * Saves a new entity to the database.
     *
     * @param entity the entity to save
     * @return the saved entity with the primary key ID populated
     * @throws RuntimeException if a database access error occurs while saving the entity
     */
    public abstract T save(T entity) throws RuntimeException;

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return true if the entity was successfully updated, false otherwise
     * @throws RuntimeException if a database access error occurs while updating the entity
     */
    public abstract boolean update(T entity) throws RuntimeException;

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity
     * @return an Optional containing the entity if found, or empty if not found
     * @throws RuntimeException if a database access error occurs
     */
    public Optional<T> find(int id) throws RuntimeException {
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

    /**
     * Finds all entities in the table.
     *
     * @return a list of all entities in the table
     * @throws RuntimeException if a database access error occurs
     */
    public List<T> findAll() throws RuntimeException {
        List<T> results = new ArrayList<>();
        String query = String.format("SELECT * FROM %s", tableName);

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                results.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error finding entities in %s: %s", tableName, e));
        }

        return results;
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @return true if the entity was successfully deleted, false otherwise
     * @throws RuntimeException if a database access error occurs
     */
    public boolean delete(int id) throws RuntimeException {
        String query = String.format("DELETE FROM %s WHERE id = ?", tableName);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error deleting from %s: %s", tableName, e));
        }
    }
}
