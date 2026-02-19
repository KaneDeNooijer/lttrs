package me.kanedenooijer.lttrs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.kanedenooijer.lttrs.view.MainView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * The main entry point for the LTTRS application.
 * This class initializes the database connection and launches the JavaFX application.
 */
public final class Main extends Application {

    /**
     * The database connection used throughout the application.
     */
    private static Connection connection;

    @Override
    public void start(Stage stage) throws SQLException {
        // Initialize the database connection
        Main.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lttrs", "root", "");

        // Set up the main view and show the stage
        stage.setScene(new Scene(MainView.getInstance(), 1400, 800));
        stage.setTitle("LTTRS");
        stage.setResizable(false);
        stage.show();
    }

    /**
     * Gets the database connection for use in other parts of the application.
     *
     * @return The database connection.
     */
    public static Connection getConnection() {
        return Main.connection;
    }

}
