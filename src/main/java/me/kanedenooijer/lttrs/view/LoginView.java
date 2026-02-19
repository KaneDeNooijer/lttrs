package me.kanedenooijer.lttrs.view;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.Main;
import me.kanedenooijer.lttrs.database.dao.AccountDao;
import me.kanedenooijer.lttrs.database.entity.Account;
import me.kanedenooijer.lttrs.type.NotificationType;

import java.util.Objects;
import java.util.Optional;

/**
 * The LoginView class represents the login screen of the LTTRS application.
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

        VBox buttonContainer = new VBox(8);

        Button loginButton = new Button("Log in");
        loginButton.setOnAction(_ -> this.login());
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setId("primary-button");

        Button registerButton = new Button("Don't have an account? Sign up.");
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

    /**
     * Handles the login process when the user clicks the "Log in" button.
     * It validates the input fields, checks the credentials against the database,
     * and provides feedback to the user through notifications.
     */
    private void login() {
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        AccountDao accountDao = new AccountDao(Main.getConnection());

        // Validate blank fields
        if (email.isBlank() || password.isEmpty()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "Please fill in all fields.");
            return;
        }

        // Find account by email
        Optional<Account> account = accountDao.findByEmail(email);

        if (account.isEmpty()) {
            MainView.getInstance().showNotification(NotificationType.ERROR, "No account found with this email.");
            return;
        }

        // Check password
        if (!account.get().password().equals(password)) {
            MainView.getInstance().showNotification(NotificationType.ERROR, "Incorrect password.");
            return;
        }

        // Login successful
        MainView.getInstance().showNotification(NotificationType.SUCCESS, "Logged in successfully.");
        MainView.getInstance().switchView(new DashboardView());
    }

}