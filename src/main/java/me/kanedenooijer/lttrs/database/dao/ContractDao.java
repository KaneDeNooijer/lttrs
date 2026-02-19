package me.kanedenooijer.lttrs.database.dao;

import me.kanedenooijer.lttrs.database.entity.Contract;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public final class ContractDao extends GenericDao<Contract> {

    public ContractDao(Connection connection, String tableName) {
        super(connection, tableName);
    }

    @Override
    protected Contract mapResultSetToEntity(ResultSet resultSet) throws SQLException {
        return null;
    }

    @Override
    public Optional<Contract> create(Contract entity) throws RuntimeException {
        return Optional.empty();
    }

    @Override
    public boolean update(Contract entity) throws RuntimeException {
        return false;
    }

}
