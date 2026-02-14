package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.AccountRole;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class AccountDao extends AbstractDao<Account> {

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
                AccountRole.valueOf(resultSet.getString("role"))
        );
    }

    @Override
    protected Optional<Account> create(Account entity) throws RuntimeException {
        return Optional.empty();
    }

    @Override
    protected boolean update(Account entity) throws RuntimeException {
        return false;
    }

}
