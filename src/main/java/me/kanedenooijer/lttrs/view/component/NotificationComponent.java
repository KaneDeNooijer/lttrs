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
 * Notification component that displays a typed message with an icon and a dismiss button.
 * Automatically dismisses itself after 4 seconds with a fade-out animation.
 */
public final class NotificationComponent extends HBox {

    public NotificationComponent(NotificationType type, String message) {
        this.setId("notification");
        this.setMaxWidth(450);
        this.setMaxHeight(75);
        this.getStyleClass().add(type.name().toLowerCase());
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/component/notification.css")).toExternalForm());

        Label text = new Label(message);
        text.setId("notification-text");
        text.setWrapText(true);

        Button dismissButton = new Button("dismiss");
        dismissButton.setId("notification-dismiss");
        dismissButton.setOnAction(_ -> dismiss());

        this.getChildren().addAll(buildIcon(type), text, dismissButton);

        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(_ -> dismiss());
        pause.play();

        this.setOnMouseEntered(_ -> pause.pause());
        this.setOnMouseExited(_ -> pause.playFromStart());
    }

    /**
     * Builds the icon for the given notification type.
     *
     * @param type the notification type
     * @return an ImageView containing the corresponding icon
     */
    private ImageView buildIcon(NotificationType type) {
        return new ImageView(Objects.requireNonNull(
                getClass().getResource(String.format(
                        "/me/kanedenooijer/lttrs/image/%s.png",
                        type.name().toLowerCase()
                ))).toExternalForm()
        );
    }

    /**
     * Fades out the notification and removes it from its parent pane.
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
