package me.kanedenooijer.lttrs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

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
     *
     * @throws SQLException if a database access error occurs
     */
    @BeforeEach
    void globalSetup() throws SQLException {
        // Using H2 in-memory database for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:test_db;DB_CLOSE_DELAY=-1");

        try (Statement statement = connection.createStatement()) {
            // Clear any existing objects (H2 specific command)
            statement.execute("DROP ALL OBJECTS");

            statement.execute("""
                    CREATE TABLE test_user (
                        id INT PRIMARY KEY AUTO_INCREMENT,
                        username VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
                    )
                    """);
        }
    }

    /**
     * Tears down the database connection after each test.
     *
     * @throws SQLException if a database access error occurs
     */
    @AfterEach
    void globalTeardown() throws SQLException {
        connection.close();
    }

    /**
     * Inserts a test user into the test_user table.
     *
     * @param username the username of the test user
     * @param email    the email of the test user
     * @throws SQLException if a database access error occurs
     */
    protected void insertTestUser(String username, String email) throws SQLException {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(String.format(
                    "INSERT INTO test_user (username, email) VALUES ('%s', '%s')", username, email
            ));
        }
    }

}
