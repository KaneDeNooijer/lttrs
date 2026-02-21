package me.kanedenooijer.lttrs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.MainView;

/**
 * The main entry point for the LTTRS application.
 * This class initializes the database connection and launches the JavaFX application.
 */
public final class Main extends Application {

    @Override
    public void start(Stage stage) throws RuntimeException {
        stage.setScene(new Scene(MainView.getInstance(), 1400, 800));
        stage.setTitle("LTTRS.");
        stage.setResizable(false);
        stage.setOnCloseRequest(_ -> DatabaseConnection.close());
        stage.show();
    }

}
