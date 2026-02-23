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
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.type.AccountRole;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.generic.MainView;

import java.util.Objects;

/**
 * Registration view allowing new users to create an account.
 */
public final class RegisterView extends FlowPane {

    private TextField nameField;
    private TextField emailField;
    private PasswordField passwordField;

    public RegisterView() {
        this.setId("view");
        this.getChildren().add(this.buildForm());
    }

    /**
     * Builds the registration form containing the logo, input fields and action buttons.
     */
    private VBox buildForm() {
        VBox parent = new VBox(18);
        parent.setId("form");

        ImageView logo = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/logo-black.png")).toExternalForm());
        logo.setFitWidth(400);
        logo.setFitHeight(400);
        logo.setPreserveRatio(true);

        parent.getChildren().addAll(logo, this.buildNameField(), this.buildEmailField(), this.buildPasswordField(), this.buildButtonContainer());

        return parent;
    }

    /**
     * Builds the name input field with its label.
     */
    private VBox buildNameField() {
        VBox parent = new VBox(2);

        this.nameField = new TextField();
        parent.getChildren().addAll(new Label("Full name:"), this.nameField);

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
     * Builds the container with the register and login buttons.
     */
    private VBox buildButtonContainer() {
        VBox parent = new VBox(8);

        Button registerButton = new Button("Sign up");
        registerButton.getStyleClass().add("primary-button");
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setOnAction(_ -> this.register());

        Button loginButton = new Button("Already have an account? Log in.");
        loginButton.getStyleClass().add("secondary-button");
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setOnAction(_ -> MainView.getInstance().switchView(new LoginView()));

        parent.getChildren().addAll(registerButton, loginButton);

        return parent;
    }

    /**
     * Validates the input fields, checks whether the email is already in use,
     * and creates a new account or shows an appropriate notification.
     */
    private void register() {
        String name = this.nameField.getText().trim();
        String email = this.emailField.getText().trim();
        String password = this.passwordField.getText();

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "Please fill in all fields.");
            return;
        }

        AccountDao accountDao = new AccountDao(DatabaseConnection.getConnection());

        if (accountDao.findByEmail(email).isPresent()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "An account with this email already exists.");
            return;
        }

        if (accountDao.create(new Account(name, email, password, AccountRole.USER)).isPresent()) {
            MainView.getInstance().showNotification(NotificationType.SUCCESS, "Account created! You can now log in.");
            MainView.getInstance().switchView(new LoginView());
            return;
        }

        MainView.getInstance().showNotification(NotificationType.ERROR, "An error occurred while creating your account. Please try again.");
    }

}
