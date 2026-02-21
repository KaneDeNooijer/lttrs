package me.kanedenooijer.lttrs.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.component.NotificationView;

public final class MainView extends StackPane {

    private static MainView instance;

    private final StackPane notificationLayer;

    private MainView() {
        this.notificationLayer = new StackPane();
        this.notificationLayer.setPadding(new Insets(10, 0, 0, 0));
        this.notificationLayer.setAlignment(Pos.TOP_CENTER);
        this.notificationLayer.setPickOnBounds(false);

        this.getChildren().addAll(new LoginView(), this.notificationLayer);
    }

    public void switchView(Pane newView) {
        this.getChildren().clear();
        this.getChildren().addAll(newView, this.notificationLayer);
    }

    public void showNotification(NotificationType type, String message) {
        NotificationView notification = new NotificationView(type, message);
        this.notificationLayer.getChildren().add(notification);
    }

    public static MainView getInstance() {
        if (MainView.instance == null) {
            MainView.instance = new MainView();
        }

        return MainView.instance;
    }

}
