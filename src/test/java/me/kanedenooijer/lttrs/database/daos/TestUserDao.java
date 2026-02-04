package me.kanedenooijer.lttrs.database.daos;

import java.sql.Connection;

public final class TestUserDao extends AbstractDao<TestUser> {

    public TestUserDao(Connection connection, Class<TestUser> recordClass) {
        super(connection, recordClass);
    }

}
