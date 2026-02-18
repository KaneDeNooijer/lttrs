package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Leave;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class LeaveDao extends GenericDao<Leave> {

    public LeaveDao(Connection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    protected Leave mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    protected Optional<Leave> create(Leave entity) throws RuntimeException {
        return Optional.empty();
    }

    @Override
    protected boolean update(Leave entity) throws RuntimeException {
        return false;
    }

}
