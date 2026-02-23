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

    private TextField emailField;
    private PasswordField passwordField;

    public LoginView() {
        this.setId("view");
        this.getChildren().add(this.buildForm());
    }

    /**
     * Builds the login form containing the logo, input fields and action buttons.
     */
    private VBox buildForm() {
        VBox parent = new VBox(18);
        parent.setId("form");

        ImageView logo = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/logo-black.png")).toExternalForm());
        logo.setFitWidth(400);
        logo.setFitHeight(400);
        logo.setPreserveRatio(true);

        parent.getChildren().addAll(logo, this.buildEmailField(), this.buildPasswordField(), this.buildButtonContainer());

        return parent;
    }

    /**
     * Builds the email input field with its label.
     */
    private VBox buildEmailField() {
        VBox parent = new VBox(2);

        this.emailField = new TextField();
        parent.getChildren().addAll(new Label("Email:"), this.emailField);

        return parent;
    }

    /**
     * Builds the password input field with its label.
     */
    private VBox buildPasswordField() {
        VBox parent = new VBox(2);

        this.passwordField = new PasswordField();
        parent.getChildren().addAll(new Label("Password:"), this.passwordField);

        return parent;
    }

    /**
     * Builds the container with the login and register buttons.
     */
    private VBox buildButtonContainer() {
        VBox parent = new VBox(8);

        Button loginButton = new Button("Log in");
        loginButton.getStyleClass().add("primary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(_ -> this.login());

        Button registerButton = new Button("Don't have an account? Sign up.");
        registerButton.getStyleClass().add("secondary-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(_ -> MainView.getInstance().switchView(new RegisterView()));

        parent.getChildren().addAll(loginButton, registerButton);

        return parent;
    }

    /**
     * Validates the input fields, verifies the credentials against the database,
     * and either starts an account session or shows an appropriate notification.
     */
    private void login() {
        String email = this.emailField.getText().trim();
        String password = this.passwordField.getText();

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
