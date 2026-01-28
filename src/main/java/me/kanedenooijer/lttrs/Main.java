package me.kanedenooijer.lttrs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        Scene scene = new Scene(new Pane(), 1400, 800);
        stage.setTitle("LTTRS");
        stage.setScene(scene);
        stage.setResizable(false);
        stage.show();
    }

}
