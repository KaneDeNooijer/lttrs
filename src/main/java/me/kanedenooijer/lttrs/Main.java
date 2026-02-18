package me.kanedenooijer.lttrs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public final class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new Pane(), 1400, 800));
        stage.setTitle("LTTRS");
        stage.setResizable(false);
        stage.show();
    }

}
