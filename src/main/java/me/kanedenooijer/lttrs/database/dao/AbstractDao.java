package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.annotation.Column;
import me.kanedenooijer.lttrs.annotation.Id;
import me.kanedenooijer.lttrs.annotation.Table;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.RecordComponent;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Abstract data access object providing basic CRUD operations for entities of type T.
 *
 * @param <T> the type of the entity, which must be a record
 */
public abstract class AbstractDao<T extends Record> {

    /**
     * The database connection.
     */
    protected final Connection connection;

    /**
     * The name of the database table associated with this DAO.
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
        String query = String.format("SELECT * FROM %s WHERE id = ?", getTableName());

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return Optional.of(mapResultSetToEntity(result));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error finding entity in %s: %s", getTableName(), e));
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
        String query = String.format("SELECT * FROM %s", getTableName());

        try (Statement statement = connection.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            while (resultSet.next()) {
                results.add(mapResultSetToEntity(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error finding entities in %s: %s", getTableName(), e));
        }

        return results;
    }

    /**
     * Creates a new entity in the database.
     *
     * @param entity the entity to create
     * @return an Optional containing the created entity with its generated ID, or empty if creation failed
     * @throws RuntimeException if a database access error occurs
     */
    public Optional<T> create(T entity) throws RuntimeException {
        List<String> columnNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();

        try {
            // Extract column names and values from the record components
            for (RecordComponent component : recordClass.getRecordComponents()) {
                // Skip the ID column as we assume the database auto-increments it
                if (component.isAnnotationPresent(Id.class)) {
                    continue;
                }

                // Extract the column name from the @Column annotation
                columnNames.add(getColumnName(component));

                // Extract the value from the record instance using the accessor method
                values.add(component.getAccessor().invoke(entity));
            }

            // Construct the SQL INSERT statement
            String columns = String.join(", ", columnNames);
            String placeholders = String.join(", ", columnNames.stream().map(_ -> "?").toList());
            String query = String.format("INSERT INTO %s (%s) VALUES (%s)", getTableName(), columns, placeholders);

            // Execute the INSERT statement and retrieve the generated ID
            try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
                // Set the values for the prepared statement
                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i));
                }

                // Execute the statement
                statement.executeUpdate();

                // Retrieve the generated ID and return the created entity
                try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        return find(generatedKeys.getInt(1));
                    }
                }
            }
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating entity in %s: %s", getTableName(), e));
        }

        return Optional.empty();
    }

    /**
     * Updates an existing entity in the database.
     *
     * @param entity the entity to update
     * @return true if the entity was successfully updated, false otherwise
     * @throws RuntimeException if a database access error occurs
     */
    public boolean update(T entity) throws RuntimeException {
        List<String> columnNames = new ArrayList<>();
        List<Object> values = new ArrayList<>();
        Integer idValue = null;

        try {
            // Extract column names and values from the record components
            for (RecordComponent component : recordClass.getRecordComponents()) {
                // Skip the ID column as we assume the database auto-increments it
                if (component.isAnnotationPresent(Id.class)) {
                    idValue = (Integer) component.getAccessor().invoke(entity);
                    continue;
                }

                // Extract the column name from the @Column annotation
                columnNames.add(getColumnName(component));

                // Extract the value from the record instance using the accessor method
                values.add(component.getAccessor().invoke(entity));
            }

            // Construct the SQL UPDATE statement
            String setClause = String.join(", ", columnNames.stream().map(name -> name + " = ?").toList());
            String query = String.format("UPDATE %s SET %s WHERE id = ?", getTableName(), setClause);

            // Execute the UPDATE statement
            try (PreparedStatement statement = connection.prepareStatement(query)) {
                // Set the values for the prepared statement
                for (int i = 0; i < values.size(); i++) {
                    statement.setObject(i + 1, values.get(i));
                }

                // Set the ID value for the WHERE clause
                statement.setObject(values.size() + 1, idValue);

                // Execute the statement and return whether it was successful
                return statement.executeUpdate() > 0;
            }
        } catch (SQLException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(String.format("Error creating entity in %s: %s", getTableName(), e));
        }
    }

    /**
     * Deletes an entity by its ID.
     *
     * @param id the ID of the entity to delete
     * @return true if the entity was successfully deleted, false otherwise
     * @throws RuntimeException if a database access error occurs
     */
    public boolean delete(int id) throws RuntimeException {
        String query = String.format("DELETE FROM %s WHERE id = ?", getTableName());

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, id);
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error deleting from %s: %s", getTableName(), e));
        }
    }

    /**
     * Maps a ResultSet row to an entity of type T.
     *
     * @return the mapped entity
     * @throws RuntimeException if a mapping error occurs
     */
    private String getTableName() throws RuntimeException {
        if (recordClass.isAnnotationPresent(Table.class)) {
            return recordClass.getAnnotation(Table.class).value();
        }

        throw new RuntimeException(String.format("Class %s is not annotated with @Table", recordClass.getName()));
    }

    /**
     * Gets the column name for a given RecordComponent, checking for the @Column annotation.
     *
     * @param component the RecordComponent to get the column name for
     * @return the column name specified in the @Column annotation
     * @throws RuntimeException if the RecordComponent is not annotated with @Column
     */
    private String getColumnName(RecordComponent component) throws RuntimeException {
        if (component.isAnnotationPresent(Column.class)) {
            return component.getAnnotation(Column.class).value();
        }

        throw new RuntimeException(String.format(
                "RecordComponent %s in class %s is not annotated with @Column",
                component.getName(),
                recordClass.getName()
        ));
    }

    /**
     * Maps a ResultSet row to an instance of the record class T using reflection.
     *
     * @param resultSet the ResultSet to map
     * @return an instance of the record class T populated with data from the ResultSet
     * @throws RuntimeException if a database access error occurs or if the mapping fails
     */
    private T mapResultSetToEntity(ResultSet resultSet) throws RuntimeException {
        try {
            RecordComponent[] components = recordClass.getRecordComponents();
            Object[] values = new Object[components.length];
            Class<?>[] types = new Class[components.length];

            for (int i = 0; i < components.length; i++) {
                RecordComponent recordComponent = components[i];
                String columnName = getColumnName(recordComponent);

                values[i] = resultSet.getObject(columnName);
                types[i] = recordComponent.getType();
            }

            return recordClass.getDeclaredConstructor(types).newInstance(values);
        } catch (InvocationTargetException |
                 NoSuchMethodException |
                 InstantiationException |
                 IllegalAccessException |
                 SQLException e) {
            throw new RuntimeException(String.format("Error mapping ResultSet to %s: %s", recordClass.getName(), e));
        }
    }
}
