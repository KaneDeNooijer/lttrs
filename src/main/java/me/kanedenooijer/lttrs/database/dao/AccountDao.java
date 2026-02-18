package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.Role;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class AccountDao extends GenericDao<Account> {

    public AccountDao(Connection connection) {
        super(connection, "accounts");
    }

    @Override
    protected Account mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                Role.valueOf(resultSet.getString("role").toUpperCase())
        );
    }

    @Override
    protected Optional<Account> create(Account entity) throws RuntimeException {
        String query = "INSERT INTO `accounts` (`username`, `password`, `name`, `role`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.username());
            statement.setString(2, entity.password());
            statement.setString(3, entity.name());
            statement.setString(4, entity.role().name().toLowerCase());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(new Account(
                            generatedKeys.getInt(1),
                            entity.username(),
                            entity.password(),
                            entity.name(),
                            entity.role()
                    ));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to create account: %s", e.getMessage()), e);
        }

        return Optional.empty();
    }

    @Override
    protected boolean update(Account entity) throws RuntimeException {
        String query = "UPDATE `accounts` SET `username` = ?, `password` = ?, `name` = ?, `role` = ? WHERE `id` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.username());
            statement.setString(2, entity.password());
            statement.setString(3, entity.name());
            statement.setString(4, entity.role().name().toLowerCase());
            statement.setInt(5, entity.id());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to update account: %s", e.getMessage()), e);
        }
    }

}
