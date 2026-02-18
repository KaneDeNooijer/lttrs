package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Registration;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class RegistrationDao extends GenericDao<Registration> {

    public RegistrationDao(Connection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    protected Registration mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    protected Optional<Registration> create(Registration entity) throws RuntimeException {
        return Optional.empty();
    }

    @Override
    protected boolean update(Registration entity) throws RuntimeException {
        return false;
    }

}
