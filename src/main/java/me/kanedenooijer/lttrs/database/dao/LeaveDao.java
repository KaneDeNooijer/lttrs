package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Leave;

import java.sql.*;
import java.util.Optional;

/**
 * DAO for managing leaves in the database.
 */
public final class LeaveDao extends GenericDao<Leave> {

    public LeaveDao(Connection connection) {
        super(connection, "leaves");
    }

    @Override
    protected Leave mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Leave(
                resultSet.getInt("id"),
                resultSet.getInt("account_id"),
                resultSet.getDate("start_date").toLocalDate(),
                resultSet.getDate("end_date").toLocalDate()
        );
    }

    @Override
    public Optional<Leave> create(Leave entity) throws RuntimeException {
        String query = "INSERT INTO `leaves` (`account_id`, `start_date`, `end_date`) VALUES (?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.accountId());
            statement.setDate(2, Date.valueOf(entity.startDate()));
            statement.setDate(3, Date.valueOf(entity.endDate()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(new Leave(
                            generatedKeys.getInt(1),
                            entity.accountId(),
                            entity.startDate(),
                            entity.endDate()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to create leave: %s", e.getMessage()), e);
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Leave entity) throws RuntimeException {
        String query = "UPDATE `leaves` SET `start_date` = ?, `end_date` = ? WHERE `id` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setDate(1, Date.valueOf(entity.startDate()));
            statement.setDate(2, Date.valueOf(entity.endDate()));
            statement.setInt(3, entity.id());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to update leave: %s", e.getMessage()), e);
        }
    }

}
