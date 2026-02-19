package me.kanedenooijer.lttrs.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public final class LoginView extends FlowPane {

    public LoginView() {
        VBox form = new VBox(18);
        form.setId("form");

        ImageView logo = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/logo-black.png")).toExternalForm());
        logo.setFitWidth(400);
        logo.setFitHeight(400);
        logo.setPreserveRatio(true);

        VBox emailFieldContainer = new VBox(2);
        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();
        emailFieldContainer.getChildren().addAll(emailLabel, emailField);

        VBox passwordFieldContainer = new VBox(2);
        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();
        passwordFieldContainer.getChildren().addAll(passwordLabel, passwordField);

        VBox buttonContainer = new VBox(8);

        Button loginButton = new Button("Log in");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setId("primary-button");

        Button registerButton = new Button("Don’t have an account? Sign up.");
        registerButton.setOnAction(_ -> MainView.getInstance().switchView(new RegisterView()));
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setId("secondary-button");

        buttonContainer.getChildren().addAll(loginButton, registerButton);

        form.getChildren().addAll(
                logo,
                emailFieldContainer,
                passwordFieldContainer,
                buttonContainer
        );

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/authentication.css")).toExternalForm());
        this.setId("view");
        this.getChildren().add(form);
    }

}