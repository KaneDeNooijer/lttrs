package me.kanedenooijer.lttrs.view.component;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.util.Duration;
import me.kanedenooijer.lttrs.type.NotificationType;

public final class Notification extends HBox {

    public Notification(NotificationType type, String message) {
        this.setId("notification");
        this.getStyleClass().add(type.name().toLowerCase());

        Label icon = new Label(getIcon(type));
        icon.setId("notification-icon");

        Label text = new Label(message);
        text.setId("notification-text");
        text.setWrapText(true);

        Button close = new Button("✕");
        close.setId("notification-close");
        close.setOnAction(_ -> dismiss());

        HBox.setHgrow(text, Priority.ALWAYS);
        getChildren().addAll(icon, text, close);

        // Auto dismiss after 4 seconds
        PauseTransition pause = new PauseTransition(Duration.seconds(4));
        pause.setOnFinished(_ -> dismiss());
        pause.play();
    }

    private String getIcon(NotificationType type) {
        return switch (type) {
            case SUCCESS -> "✓";
            case ERROR -> "✕";
            case WARNING -> "⚠";
        };
    }

    private void dismiss() {
        FadeTransition fade = new FadeTransition(Duration.millis(200), this);
        fade.setToValue(0);
        fade.setOnFinished(_ -> {
            if (getParent() instanceof Pane parent) {
                parent.getChildren().remove(this);
            }
        });
        fade.play();
    }
}
