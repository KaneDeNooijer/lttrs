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
import me.kanedenooijer.lttrs.type.AccountRole;
import me.kanedenooijer.lttrs.type.NotificationType;

import java.util.Objects;

public final class RegisterView extends FlowPane {

    private final TextField nameField;
    private final TextField emailField;
    private final PasswordField passwordField;

    public RegisterView() {
        VBox form = new VBox(18);
        form.setId("form");

        ImageView logo = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/logo-black.png")).toExternalForm());
        logo.setFitWidth(400);
        logo.setFitHeight(400);
        logo.setPreserveRatio(true);

        VBox nameFieldContainer = new VBox(2);
        Label nameLabel = new Label("Full name:");
        this.nameField = new TextField();
        nameFieldContainer.getChildren().addAll(nameLabel, this.nameField);

        VBox emailFieldContainer = new VBox(2);
        Label emailLabel = new Label("Email:");
        this.emailField = new TextField();
        emailFieldContainer.getChildren().addAll(emailLabel, this.emailField);

        VBox passwordFieldContainer = new VBox(2);
        Label passwordLabel = new Label("Password:");
        this.passwordField = new PasswordField();
        passwordFieldContainer.getChildren().addAll(passwordLabel, this.passwordField);

        VBox buttonContainer = new VBox(8);

        Button registerButton = new Button("Sign up");
        registerButton.setOnAction(_ -> register());
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setId("primary-button");

        Button loginButton = new Button("Already have an account? Log in.");
        loginButton.setOnAction(_ -> MainView.getInstance().switchView(new LoginView()));
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setId("secondary-button");

        buttonContainer.getChildren().addAll(registerButton, loginButton);

        form.getChildren().addAll(
                logo,
                nameFieldContainer,
                emailFieldContainer,
                passwordFieldContainer,
                buttonContainer
        );

        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/authentication.css")).toExternalForm());
        this.setId("view");
        this.getChildren().add(form);
    }

    private void register() {
        // Trim input values to prevent accidental spaces
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        // Create DAO instance for database operations
        AccountDao accountDao = new AccountDao(Main.getConnection());

        // Validate blank fields
        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "Please fill in all fields.");
            return;
        }

        // Validate if email is already in use
        if (accountDao.findByEmail(email).isPresent()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "An account with this email already exists.");
            return;
        }

        // Check if the account was created successfully
        if (accountDao.create(new Account(name, email, password, AccountRole.USER)).isPresent()) {
            MainView.getInstance().showNotification(NotificationType.SUCCESS, "Account created! You can now log in.");
            MainView.getInstance().switchView(new LoginView());
            return;
        }

        // If we reach this point, something went wrong during account creation
        MainView.getInstance().showNotification(NotificationType.ERROR, "An error occurred while creating your account. Please try again.");
    }

}
