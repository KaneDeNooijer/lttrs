package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Registration;

import java.sql.*;
import java.util.Optional;

/**
 * DAO for managing hour registrations in the database.
 */
public final class RegistrationDao extends GenericDao<Registration> {

    public RegistrationDao(Connection connection) {
        super(connection, "registrations");
    }

    /**
     * Maps a result set row to a Registration entity.
     *
     * @param resultSet the result set to map
     */
    @Override
    protected Registration mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Registration(
                resultSet.getInt("id"),
                resultSet.getInt("account_id"),
                resultSet.getInt("hours"),
                resultSet.getDate("date").toLocalDate()
        );
    }

    /**
     * Inserts a new registration into the database.
     *
     * @param entity the registration to create
     */
    @Override
    public Optional<Registration> create(Registration entity) throws RuntimeException {
        String sql = "INSERT INTO `registrations` (`account_id`, `hours`, `date`) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.accountId());
            statement.setInt(2, entity.hours());
            statement.setDate(3, Date.valueOf(entity.date()));
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(new Registration(
                            generatedKeys.getInt(1),
                            entity.accountId(),
                            entity.hours(),
                            entity.date()
                    ));
                }
            }

            return Optional.empty();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to create registration.", e);
        }
    }

    /**
     * Updates an existing registration in the database.
     *
     * @param entity the registration to update
     */
    @Override
    public boolean update(Registration entity) throws RuntimeException {
        String sql = "UPDATE `registrations` SET `hours` = ?, `date` = ? WHERE `id` = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, entity.hours());
            statement.setDate(2, Date.valueOf(entity.date()));
            statement.setInt(3, entity.id());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to update registration.", e);
        }
    }

}
