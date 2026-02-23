package me.kanedenooijer.lttrs.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.database.dao.ContractDao;
import me.kanedenooijer.lttrs.database.dao.RegistrationDao;
import me.kanedenooijer.lttrs.database.entity.Contract;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.generic.BaseView;

import java.util.Optional;

/**
 * Dashboard view shown after a successful login.
 * Displays a welcome message and a workload meter based on the active contract.
 */
public final class DashboardView extends BaseView {

    private final ContractDao contractDao = new ContractDao(DatabaseConnection.getConnection());
    private final RegistrationDao registrationDao = new RegistrationDao(DatabaseConnection.getConnection());

    public DashboardView() {
        VBox content = new VBox(20);

        content.getChildren().addAll(this.buildWelcomeCard(), this.buildWorkloadCard());

        this.center.getChildren().add(content);
    }

    /**
     * Builds the welcome card displaying the account's first name.
     */
    private StackPane buildWelcomeCard() {
        StackPane parent = new StackPane();

        Label welcomeLabel = new Label("Welcome back ");
        welcomeLabel.getStyleClass().add("title");
        Label nameLabel = new Label(AccountSession.getAccount().firstName());
        nameLabel.getStyleClass().add("title");
        Label exclamationLabel = new Label("!");
        exclamationLabel.getStyleClass().add("title");
        nameLabel.setId("dashboard-name");

        HBox welcomeBox = new HBox();
        welcomeBox.getChildren().addAll(welcomeLabel, nameLabel, exclamationLabel);

        parent.getChildren().add(welcomeBox);
        return parent;
    }

    /**
     * Builds the workload meter card based on the active contract and hours worked this week.
     * If no active contract is found, a message is shown instead.
     */
    private StackPane buildWorkloadCard() {
        StackPane parent = new StackPane();
        parent.getStyleClass().add("card");

        Label title = new Label("Workload this week");

        int accountId = AccountSession.getAccount().id();
        Optional<Contract> activeContract = DashboardView.this.contractDao.findActive(accountId);

        if (activeContract.isEmpty()) {
            Label noContract = new Label("No active contract found.");
            noContract.setId("dashboard-no-contract");

            VBox content = new VBox(8, title, noContract);
            parent.getChildren().add(content);
            return parent;
        }

        int contractedHours = activeContract.get().hours();
        int workedHours = DashboardView.this.registrationDao.sumHoursThisWeek(accountId);

        double percentage = Math.min((workedHours / (contractedHours * 2.0)) * 100, 100);
        double progress = percentage / 100;

        ProgressBar meter = new ProgressBar(progress);
        meter.setMaxWidth(Double.MAX_VALUE);
        meter.setId("workload-meter");

        if (percentage < 40) meter.getStyleClass().add("workload-low");
        else if (percentage < 70) meter.getStyleClass().add("workload-medium");
        else meter.getStyleClass().add("workload-high");

        Label hoursLabel = new Label(String.format("%d / %d hours (%.0f%%)", workedHours, contractedHours, percentage));

        VBox meterBox = new VBox(8, meter, hoursLabel);
        meterBox.setAlignment(Pos.CENTER_LEFT);

        VBox content = new VBox(8, title, meterBox);
        parent.getChildren().add(content);

        return parent;
    }

}
