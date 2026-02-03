package me.kanedenooijer.lttrs.database.dao;

import java.sql.Connection;

public final class TestUserDao extends AbstractDao<TestUser> {

    public TestUserDao(Connection connection, Class<TestUser> recordClass) {
        super(connection, recordClass);
    }

    @Override
    public TestUser save(TestUser entity) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(TestUser entity) {
        throw new UnsupportedOperationException();
    }

}
