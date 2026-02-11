package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.database.type.AccountRole;

import java.sql.*;

public final class AccountDao extends AbstractDao<Account> {

    public AccountDao(Connection connection) {
        super(connection, "account");
    }

    @Override
    protected Account mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return new Account(
                resultSet.getInt("id"),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getString("name"),
                AccountRole.valueOf(resultSet.getString("role").toUpperCase())
        );
    }

    @Override
    public Account save(Account entity) throws RuntimeException {
        String query = String.format("INSERT INTO %s (username, password, name, role) VALUES (?, ?, ?, ?)", tableName);

        try (PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            statement.setObject(1, entity.username());
            statement.setObject(2, entity.password());
            statement.setObject(3, entity.name());
            statement.setObject(4, entity.role().name().toLowerCase());
            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                generatedKeys.next();
                return new Account(
                        generatedKeys.getInt(1),
                        entity.username(),
                        entity.password(),
                        entity.name(),
                        entity.role()
                );
            }
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error saving entity to %s: %s", tableName, e));
        }
    }

    @Override
    public boolean update(Account entity) throws RuntimeException {
        String query = String.format("UPDATE %s SET username = ?, password = ?, name = ?, role = ? WHERE id = ?", tableName);

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, entity.username());
            statement.setObject(2, entity.password());
            statement.setObject(3, entity.name());
            statement.setObject(4, entity.role().name().toLowerCase());
            statement.setObject(5, entity.id());
            return statement.executeUpdate() > 0;
        } catch (SQLException e) {
            throw new RuntimeException(String.format("Error updating entity in %s: %s", tableName, e));
        }
    }

}
