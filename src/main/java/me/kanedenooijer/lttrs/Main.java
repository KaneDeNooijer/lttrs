package me.kanedenooijer.lttrs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.kanedenooijer.lttrs.view.MainView;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public final class Main extends Application {

    private static Connection connection;

    static void main(String[] args) throws SQLException {
        // Initialize the database connection
        Main.connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/lttrs", "root", "password");

        // Launch the JavaFX application
        Application.launch(args);
    }

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(MainView.getInstance(), 1400, 800));
        stage.setTitle("LTTRS");
        stage.setResizable(false);
        stage.show();
    }

    public static Connection getConnection() {
        return Main.connection;
    }

}
