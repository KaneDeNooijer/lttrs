package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.AccountRole;

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
                resultSet.getString("full_name"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                AccountRole.valueOf(resultSet.getString("role").toUpperCase())
        );
    }

    @Override
    public Optional<Account> create(Account entity) throws RuntimeException {
        String query = "INSERT INTO `accounts` (`full_name`, `email`, `password`, `role`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query, PreparedStatement.RETURN_GENERATED_KEYS)) {
            statement.setString(1, entity.fullName());
            statement.setString(2, entity.email());
            statement.setString(3, entity.password());
            statement.setString(4, entity.role().name().toLowerCase());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return Optional.of(new Account(
                            generatedKeys.getInt(1),
                            entity.fullName(),
                            entity.email(),
                            entity.password(),
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
    public boolean update(Account entity) throws RuntimeException {
        String query = "UPDATE `accounts` SET `full_name` = ?, `email` = ?, `password` = ?, `role` = ? WHERE `id` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, entity.fullName());
            statement.setString(2, entity.email());
            statement.setString(3, entity.password());
            statement.setString(4, entity.role().name().toLowerCase());
            statement.setInt(5, entity.id());

            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to update account: %s", e.getMessage()), e);
        }
    }

    public Optional<Account> findByEmail(String email) throws RuntimeException {
        String query = "SELECT * FROM `accounts` WHERE `email` = ?";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapResultSetToEntity(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Failed to find account by email: %s", e.getMessage()), e);
        }

        return Optional.empty();
    }

}
