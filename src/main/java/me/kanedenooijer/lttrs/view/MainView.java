package me.kanedenooijer.lttrs.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;

public final class MainView extends StackPane {

    private static MainView instance;

    private MainView() {
        getChildren().add(new LoginView());
    }

    public void switchView(Pane newView) {
        getChildren().clear();
        getChildren().add(newView);
    }

    public void showNotification(Notification.Type type, String message) {
        Notification notification = new Notification(type, message);
        notification.setMaxWidth(400);
        notification.setMaxHeight(200);
        StackPane.setAlignment(notification, Pos.TOP_CENTER);
        StackPane.setMargin(notification, new Insets(0, 0, 24, 0));
        getChildren().add(notification);
    }

    public static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }

        return instance;
    }

}
