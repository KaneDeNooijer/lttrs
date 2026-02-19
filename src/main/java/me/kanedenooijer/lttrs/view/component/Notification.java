package me.kanedenooijer.lttrs.view.component;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import me.kanedenooijer.lttrs.type.NotificationType;

import java.util.Objects;

/**
 * A simple notification component that displays a message with an icon and a close button.
 */
public final class Notification extends HBox {

    public Notification(NotificationType type, String message) {
        this.setId("notification");
        this.setMaxWidth(450);
        this.setMaxHeight(75);
        this.getStyleClass().add(type.name().toLowerCase());

        ImageView icon = getIcon(type);

        Label text = new Label(message);
        text.setId("notification-text");
        text.setWrapText(true);

        Button close = new Button("X");
        close.setId("notification-close");
        close.setOnAction(_ -> dismiss());

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/notification.css")).toExternalForm());
        this.getChildren().addAll(icon, text, close);

        // Auto dismiss after 4 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(_ -> dismiss());
        pause.play();
    }

    /**
     * Get the icon for the given notification type.
     *
     * @param type The type of the notification.
     * @return An ImageView containing the icon for the notification type.
     */
    private ImageView getIcon(NotificationType type) {
        return new ImageView(Objects.requireNonNull(
                this.getClass().getResource(String.format(
                        "/me/kanedenooijer/lttrs/image/%s.png",
                        type.name().toLowerCase()
                ))).toExternalForm()
        );
    }

    /**
     * Dismiss the notification with a fade-out animation and remove it from the parent pane.
     */
    private void dismiss() {
        FadeTransition fade = new FadeTransition(Duration.millis(200), this);
        fade.setToValue(0);
        fade.setOnFinished(_ -> {
            if (this.getParent() instanceof Pane parent) {
                parent.getChildren().remove(this);
            }
        });
        fade.play();
    }
}
