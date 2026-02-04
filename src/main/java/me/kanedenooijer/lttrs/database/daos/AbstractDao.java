package me.kanedenooijer.lttrs.database.daos;

import me.kanedenooijer.lttrs.utils.Util;

import java.lang.reflect.RecordComponent;
import java.sql.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Abstract Data Access Object providing basic CRUD operations.
 *
 * @param <T> the type of the entity
 */
public abstract class AbstractDao<T extends Record> {

    /**
     * The database connection.
     */
    protected final Connection connection;

    /**
     * The record this DAO manages.
     */
    protected final Class<T> recordClass;

    protected AbstractDao(Connection connection, Class<T> recordClass) {
        this.connection = connection;
        this.recordClass = recordClass;
    }

    /**
     * Finds an entity by its ID.
     *
     * @param id the ID of the entity
     * @return an Optional containing the entity if found, or empty if not found
     * @throws RuntimeException if a database access error occurs
     */
    public Optional<T> find(int id) throws RuntimeException {
        String query = String.format("SELECT * FROM %s WHERE id = ?", Util.toSnakeCase(recordClass.getSimpleName()));

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapResultSetToEntity(result));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error finding entity in %s: %s", Util.toSnakeCase(recordClass.getSimpleName()), e)
            );
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
        String query = String.format("SELECT * FROM %s", Util.toSnakeCase(recordClass.getSimpleName()));

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                results.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error executing findAll on %s: %s", Util.toSnakeCase(recordClass.getSimpleName()), e)
            );
        }

        return results;
    }

    /**
     * Saves a new entity to the database.
     *
     * @param entity the entity to save
     * @return an Optional containing the saved entity, or empty if the save failed
     * @throws RuntimeException if a database access error occurs
     */
    public T save(T entity) throws RuntimeException {
        // Filter components to exclude ID
        List<RecordComponent> components = Arrays.stream(recordClass.getRecordComponents())
                .filter(recordComponent -> !recordComponent.getName().equals("id"))
                .toList();

        // Convert the table name to snake_case
        String table = Util.toSnakeCase(recordClass.getSimpleName());

        // Fetch all names from the record and convert them to snake_case
        String columns = components.stream()
                .map(RecordComponent::getName)
                .map(Util::toSnakeCase)
                .collect(Collectors.joining(", "));

        // Create a row of question marks with commas
        String placeholders = components.stream()
                .map(_ -> "?")
                .collect(Collectors.joining(", "));

        // Construct the query
        String query = String.format("INSERT INTO %s (%s) VALUES (%s)", table, columns, placeholders);

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            // TODO
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error saving entity to %s: %s", table, e));
        }

        return null;
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return true if the entity was successfully updated, false otherwise
     * @throws RuntimeException if a database access error occurs
     */
    public boolean update(T entity) throws RuntimeException {
        return false;
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @return true if the entity was successfully deleted, false otherwise
     * @throws RuntimeException if a database access error occurs
     */
    public boolean delete(int id) throws RuntimeException {
        String query = String.format("DELETE FROM %s WHERE id = ?", Util.toSnakeCase(recordClass.getSimpleName()));

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(
                    String.format("Error deleting from %s: %s", Util.toSnakeCase(recordClass.getSimpleName()), e)
            );
        }
    }

    /**
     * Maps a ResultSet row to an entity of type T using reflection.
     *
     * @param resultSet the ResultSet to map
     * @return the mapped entity
     * @throws RuntimeException if a database access error occurs
     */
    private T mapResultSetToEntity(ResultSet resultSet) throws RuntimeException {
        try {
            var components = recordClass.getRecordComponents();
            Object[] values = new Object[components.length];

            for (int i = 0; i < components.length; i++) {
                // Get the name of the record field (e.g., "username")
                String name = components[i].getName();

                // OPTIONAL: Convert camelCase to snake_case if your DB uses underscores
                // String dbColumn = toSnakeCase(name);
                values[i] = resultSet.getObject(name);
            }

            // Find the constructor and create the record
            Class<?>[] types = Arrays.stream(components)
                    .map(RecordComponent::getType)
                    .toArray(Class<?>[]::new);

            return recordClass.getDeclaredConstructor(types).newInstance(values);
        } catch (Exception e) {
            throw new RuntimeException(
                    String.format("Error mapping ResultSet to entity in %s: %s", Util.toSnakeCase(recordClass.getSimpleName()), e)
            );
        }
    }

}
