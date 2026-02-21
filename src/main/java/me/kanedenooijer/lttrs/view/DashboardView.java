package me.kanedenooijer.lttrs.view;

import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.view.component.GenericView;

import java.util.Objects;

/**
 * Dashboard view shown after a successful login.
 * Displays a personalized welcome message for the logged in account.
 */
public final class DashboardView extends GenericView {

    public DashboardView() {
        super();

        FlowPane dashboard = new FlowPane();
        dashboard.setId("dashboard");

        dashboard.getChildren().add(buildWelcomeBox());

        this.setCenter(dashboard);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/dashboard.css")).toExternalForm());
    }

    /**
     * Builds the welcome message displaying the account's first name.
     */
    private HBox buildWelcomeBox() {
        Label welcomeLabel = new Label("Welcome back ");
        Label nameLabel = new Label(AccountSession.getInstance().getAccount().firstName());
        Label exclamationLabel = new Label("!");

        nameLabel.setId("dashboard-name");

        HBox welcomeBox = new HBox();
        welcomeBox.getChildren().addAll(welcomeLabel, nameLabel, exclamationLabel);

        return welcomeBox;
    }

}
