package me.kanedenooijer.lttrs.view.component;

import javafx.scene.layout.StackPane;

import java.util.Objects;

/**
 * Custom component representing a card with a drop shadow and rounded corners.
 */
public final class CardComponent extends StackPane {

    public CardComponent() {
        this.getStyleClass().add("card");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/components/card.css")).toExternalForm());
    }

}
