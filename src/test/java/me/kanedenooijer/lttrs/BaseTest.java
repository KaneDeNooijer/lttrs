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
        this.connection = DriverManager.getConnection("jdbc:h2:mem:lttrs;DB_CLOSE_DELAY=-1");

        try (Statement statement = this.connection.createStatement()) {
            // Clear any existing objects (H2 specific command)
            statement.execute("DROP ALL OBJECTS");

            statement.execute("""
                    CREATE TABLE IF NOT EXISTS `accounts`
                     (
                         `id`        INT                    NOT NULL AUTO_INCREMENT,
                         `full_name` VARCHAR(255)           NOT NULL,
                         `email`     VARCHAR(255)           NOT NULL UNIQUE,
                         `password`  VARCHAR(255)           NOT NULL,
                         `role`      ENUM ('user', 'admin') NOT NULL DEFAULT 'user',
                         PRIMARY KEY (`id`)
                     );
                    """);
        }
    }

    /**
     * Tears down the database connection after each test.
     */
    @AfterEach
    void globalTeardown() throws SQLException {
        this.connection.close();
    }

    /**
     * Inserts a test user into the account table.
     *
     * @param fullName The full name of the user.
     * @param email    The email of the user.
     * @param password The password of the user.
     */
    protected void insertTestUser(String fullName, String email, String password) throws SQLException {
        String query = "INSERT INTO `accounts` (`full_name`, `email`, `password`, `role`) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, fullName);
            statement.setString(2, email);
            statement.setString(3, password);
            statement.setObject(4, AccountRole.USER.name().toLowerCase());
            statement.executeUpdate();
        }
    }

}
