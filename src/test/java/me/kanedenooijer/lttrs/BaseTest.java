package me.kanedenooijer.lttrs;

import me.kanedenooijer.lttrs.type.AccountRole;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.*;

/**
 * Base test class providing common setup and teardown for database tests.
 */
public abstract class BaseTest {

    /**
     * The database connection used for testing.
     */
    protected Connection connection;

    /**
     * Sets up the in-memory database and creates the database tables before each test.
     */
    @BeforeEach
    void globalSetup() throws SQLException {
        // Create an in-memory H2 database connection with
        // DB_CLOSE_DELAY=-1 to keep it alive until the JVM shuts down
        connection = DriverManager.getConnection("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1");

        try (Statement statement = connection.createStatement()) {
            // Clear any existing objects (H2 specific command)
            statement.execute("DROP ALL OBJECTS");

            statement.execute("""
                    CREATE TABLE `accounts` (
                        `id` INT AUTO_INCREMENT,
                        `username` VARCHAR(255) NOT NULL,
                        `password` VARCHAR(255) NOT NULL,
                        `name` VARCHAR(255) NOT NULL,
                        `role` ENUM('USER', 'ADMIN') NOT NULL,
                        PRIMARY KEY (`id`)
                    )
                    """);
        }
    }

    /**
     * Tears down the database connection after each test.
     */
    @AfterEach
    void globalTeardown() throws SQLException {
        connection.close();
    }

    /**
     * Inserts a test user into the account table.
     *
     * @param username the username of the test user
     * @param password the password of the test user
     * @param name     the name of the test user
     */
    protected void insertTestUser(String username, String password, String name) throws SQLException {
        String query = "INSERT INTO `accounts` (`username`, `password`, `name`, `role`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setObject(1, username);
            statement.setObject(2, password);
            statement.setObject(3, name);
            statement.setObject(4, AccountRole.USER.name());
            statement.executeUpdate();
        }
    }

}
