package me.kanedenooijer.lttrs.view;

import javafx.scene.layout.StackPane;

public final class MainView extends StackPane {

    private final LoginView loginView;
    private final RegisterView registerView;

    public MainView() {
        this.loginView = new LoginView();
        this.registerView = new RegisterView();

        this.getChildren().add(this.loginView);
    }

}
