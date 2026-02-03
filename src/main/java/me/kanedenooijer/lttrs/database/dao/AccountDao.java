package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entities.Account;

import java.sql.Connection;

public final class AccountDao extends AbstractDao<Account> {

    public AccountDao(Connection connection) {
        super(connection, Account.class);
    }

    @Override
    public Account save(Account entity) {
        return null;
    }

    @Override
    public void update(Account entity) throws RuntimeException {
    }

}
