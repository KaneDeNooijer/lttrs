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

/**
 * Registration view allowing new users to create an account.
 */
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

        Button registerButton = new Button("Sign up");
        registerButton.setOnAction(_ -> register());
        registerButton.setMaxWidth(Double.MAX_VALUE);
        registerButton.setId("primary-button");

        Button loginButton = new Button("Already have an account? Log in.");
        loginButton.setOnAction(_ -> MainView.getInstance().switchView(new LoginView()));
        loginButton.setMaxWidth(Double.MAX_VALUE);
        loginButton.setId("secondary-button");

        VBox buttonContainer = new VBox(8);
        buttonContainer.getChildren().addAll(registerButton, loginButton);

        form.getChildren().addAll(
                logo,
                nameFieldContainer,
                emailFieldContainer,
                passwordFieldContainer,
                buttonContainer
        );

        this.setId("view");
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/authentication.css")).toExternalForm());
        this.getChildren().add(form);
    }

    /**
     * Validates the input fields, checks whether the email is already in use,
     * and creates a new account or shows an appropriate notification.
     */
    private void register() {
        String name = nameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();

        if (name.isBlank() || email.isBlank() || password.isBlank()) {
            MainView.getInstance().showNotification(NotificationType.WARNING, "Please fill in all fields.");
            return;
        }

        AccountDao accountDao = new AccountDao(Main.getConnection());

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
