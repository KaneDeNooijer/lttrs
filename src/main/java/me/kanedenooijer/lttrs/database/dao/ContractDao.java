package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Contract;

import java.sql.*;
import java.util.Optional;

/**
 * DAO for managing contracts in the database.
 */
public final class ContractDao extends GenericDao<Contract> {

    public ContractDao(Connection connection) {
        super(connection, "contracts");
    }

    @Override
    protected Contract mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Contract(
                resultSet.getInt("id"),
                resultSet.getInt("account_id"),
                resultSet.getInt("hours"),
                resultSet.getDate("start_date").toLocalDate(),
                resultSet.getDate("end_date").toLocalDate()
        );
    }

    @Override
    public Optional<Contract> create(Contract entity) throws RuntimeException {
        String query = "INSERT INTO `contracts` (`account_id`, `hours`, `start_date`, `end_date`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setInt(1, entity.accountId());
            statement.setInt(2, entity.hours());
            statement.setDate(3, Date.valueOf(entity.startDate()));
            statement.setDate(4, Date.valueOf(entity.endDate()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(new Contract(
                            generatedKeys.getInt(1),
                            entity.accountId(),
                            entity.hours(),
                            entity.startDate(),
                            entity.endDate()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to create contract: %s", e.getMessage()), e);
        }

        return Optional.empty();
    }

    @Override
    public boolean update(Contract entity) throws RuntimeException {
        String query = "UPDATE `contracts` SET `hours` = ?, `start_date` = ?, `end_date` = ? WHERE `id` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setInt(1, entity.hours());
            statement.setDate(2, Date.valueOf(entity.startDate()));
            statement.setDate(3, Date.valueOf(entity.endDate()));
            statement.setInt(4, entity.id());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to update contract: %s", e.getMessage()), e);
        }
    }

}
