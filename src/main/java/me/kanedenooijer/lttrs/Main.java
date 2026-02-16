package me.kanedenooijer.lttrs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;
import me.kanedenooijer.lttrs.view.MainView;

public class Main extends Application {

    @Override
    public void start(Stage stage) {
        stage.setScene(new Scene(new MainView(), 1400, 800));
        stage.setTitle("LTTRS");
        stage.setResizable(false);
        stage.show();
    }

}
