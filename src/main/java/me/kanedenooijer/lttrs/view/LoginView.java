package me.kanedenooijer.lttrs.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.database.dao.AccountDao;
import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.generic.MainView;

import java.util.Objects;
import java.util.Optional;

/**
 * Login view shown when no account session is active.
 * Allows the user to authenticate with their email and password.
 */
public final class LoginView extends FlowPane {

    private final TextField emailField;
    private final PasswordField passwordField;

    public LoginView() {
        VBox form = new VBox(18);
        form.setId("form");

        ImageView logo = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/logo-black.png")).toExternalForm());
        logo.setFitWidth(400);
        logo.setFitHeight(400);
        logo.setPreserveRatio(true);

        VBox emailFieldContainer = new VBox(2);
        Label emailLabel = new Label("Email:");
        this.emailField = new TextField();
        emailFieldContainer.getChildren().addAll(emailLabel, this.emailField);

        VBox passwordFieldContainer = new VBox(2);
        Label passwordLabel = new Label("Password:");
        this.passwordField = new PasswordField();
        passwordFieldContainer.getChildren().addAll(passwordLabel, this.passwordField);

        Button loginButton = new Button("Log in");
        loginButton.setOnAction(_ -> login());
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setId("primary-button");

        Button registerButton = new Button("Don't have an account? Sign up.");
        registerButton.setOnAction(_ -> MainView.getInstance().switchView(new RegisterView()));
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setId("secondary-button");

        VBox buttonContainer = new VBox(8);
        buttonContainer.getChildren().addAll(loginButton, registerButton);

        form.getChildren().addAll(
                logo,
                emailFieldContainer,
                passwordFieldContainer,
                buttonContainer
        );

        this.setId("view");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/authentication.css")).toExternalForm());
        this.getChildren().add(form);
    }

    /**
     * Validates the input fields, verifies the credentials against the database,
     * and either starts an account session or shows an appropriate notification.
     */
    private void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (email.isBlank() || password.isEmpty()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "Please fill in all fields.");
            return;
        }

        AccountDao accountDao = new AccountDao(DatabaseConnection.getConnection());
        Optional<Account> account = accountDao.findByEmail(email);

        if (account.isEmpty() || !account.get().password().equals(password)) {
            MainView.getInstance().showNotification(NotificationType.ERROR, "Invalid email or password. Please try again.");
            return;
        }

        AccountSession.login(account.get());
        MainView.getInstance().showNotification(NotificationType.SUCCESS, "You have been successfully logged in.");
        MainView.getInstance().switchView(new DashboardView());
    }

}
