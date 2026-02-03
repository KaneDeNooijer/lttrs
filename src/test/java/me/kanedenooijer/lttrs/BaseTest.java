package me.kanedenooijer.lttrs;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public abstract class BaseTest {

    protected Connection connection;

    @BeforeEach
    void globalSetup() throws SQLException {
        // Using H2 in-memory database for testing
        connection = DriverManager.getConnection("jdbc:h2:mem:test;DB_CLOSE_DELAY=-1");

        try (Statement statement = connection.createStatement()) {
            // Clear any existing objects (H2 specific command)
            statement.execute("DROP ALL OBJECTS");

            statement.execute("""
                    CREATE TABLE test_user (
                        id INT PRIMARY KEY,
                        username VARCHAR(255) NOT NULL,
                        email VARCHAR(255) NOT NULL
                    )
                    """);
        }
    }

    @AfterEach
    void globalTeardown() throws SQLException {
        connection.close();
    }

}
