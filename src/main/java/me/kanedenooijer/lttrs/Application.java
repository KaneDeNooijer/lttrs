package me.kanedenooijer.lttrs;

import javafx.scene.Scene;
import javafx.stage.Stage;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.generic.MainView;

import java.util.Objects;

/**
 * Starts the JavaFX application and initializes the main view.
 */
public final class Application extends javafx.application.Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(MainView.getInstance(), 1400, 800);
        scene.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/main.css")).toExternalForm());

        stage.setScene(scene);
        stage.setTitle("LTTRS.");
        stage.setResizable(false);
        stage.setOnCloseRequest(_ -> DatabaseConnection.close());

        stage.show();
    }

}
