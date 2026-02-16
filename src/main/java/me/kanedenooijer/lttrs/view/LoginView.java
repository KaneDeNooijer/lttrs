package me.kanedenooijer.lttrs.view;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;

import java.util.Objects;

public final class LoginView extends FlowPane {

    public LoginView() {
        VBox form = new VBox();
        form.setId("login-form");

        Label emailLabel = new Label("Email:");
        TextField emailField = new TextField();

        Label passwordLabel = new Label("Password:");
        PasswordField passwordField = new PasswordField();

        Button loginButton = new Button("Login");
        loginButton.setPrefWidth(200);

        form.getChildren().addAll(
                emailLabel,
                emailField,
                passwordLabel,
                passwordField,
                loginButton
        );

        this.setAlignment(Pos.CENTER);

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/login.css")).toString());
        this.setId("login-view");
        this.getChildren().add(form);
    }

}
