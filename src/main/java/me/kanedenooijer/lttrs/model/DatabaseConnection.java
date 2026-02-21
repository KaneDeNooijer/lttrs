package me.kanedenooijer.lttrs.model;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Utility class for managing the database connection.
 */
public final class DatabaseConnection {

    private static final Connection connection;

    static {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lttrs", "root", "");
        } catch (SQLException e) {
            throw new RuntimeException("Failed to establish database connection.", e);
        }
    }

    private DatabaseConnection() {
    }

    /**
     * Closes the database connection.
     */
    public static void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            throw new RuntimeException("Failed to close database connection.", e);
        }
    }

    /**
     * Returns the active database connection.
     *
     * @return the active database connection
     */
    public static Connection getConnection() {
        return connection;
    }

}
