package me.kanedenooijer.lttrs.view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.component.NotificationComponent;

/**
 * Root view of the application, acts as a singleton StackPane that
 * holds the current active view and a notification layer on top.
 */
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

    /**
     * Replaces the current active view with the given view,
     * keeping the notification layer on top.
     *
     * @param view the new view to display
     */
    public void switchView(Pane view) {
        getChildren().clear();
        getChildren().addAll(view, this.notificationLayer);
    }

    /**
     * Displays a notification at the top of the screen.
     *
     * @param type    the severity type of the notification
     * @param message the message to display
     */
    public void showNotification(NotificationType type, String message) {
        notificationLayer.getChildren().add(new NotificationComponent(type, message));
    }

    /**
     * Returns the singleton instance of MainView, creating it if it does not yet exist.
     */
    public static MainView getInstance() {
        if (instance == null) {
            instance = new MainView();
        }

        return instance;
    }

}
