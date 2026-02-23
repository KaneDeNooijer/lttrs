package me.kanedenooijer.lttrs.view;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.database.dao.ContractDao;
import me.kanedenooijer.lttrs.database.dao.RegistrationDao;
import me.kanedenooijer.lttrs.database.entity.Contract;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.component.CardComponent;
import me.kanedenooijer.lttrs.view.generic.GenericView;

import java.util.Objects;
import java.util.Optional;

/**
 * Dashboard view shown after a successful login.
 * Displays a welcome message and a workload meter based on the active contract.
 */
public final class DashboardView extends GenericView {

    private final ContractDao contractDao = new ContractDao(DatabaseConnection.getConnection());
    private final RegistrationDao registrationDao = new RegistrationDao(DatabaseConnection.getConnection());

    public DashboardView() {
        VBox content = new VBox(20);
        content.setId("dashboard");

        content.getChildren().addAll(buildWelcomeCard(), buildWorkloadCard());

        this.centerPane.getChildren().add(content);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/dashboard.css")).toExternalForm());
    }

    /**
     * Builds the welcome card displaying the account's first name.
     */
    private CardComponent buildWelcomeCard() {
        CardComponent card = new CardComponent();

        Label welcomeLabel = new Label("Welcome back ");
        Label nameLabel = new Label(AccountSession.getAccount().firstName());
        Label exclamationLabel = new Label("!");
        nameLabel.setId("dashboard-name");

        HBox welcomeBox = new HBox();
        welcomeBox.getChildren().addAll(welcomeLabel, nameLabel, exclamationLabel);

        card.getChildren().add(welcomeBox);
        return card;
    }

    /**
     * Builds the workload meter card based on the active contract and hours worked this week.
     * If no active contract is found, a message is shown instead.
     */
    private CardComponent buildWorkloadCard() {
        CardComponent card = new CardComponent();

        Label title = new Label("Workload this week");
        title.getStyleClass().add("card-title");

        int accountId = AccountSession.getAccount().id();
        Optional<Contract> activeContract = contractDao.findActive(accountId);

        if (activeContract.isEmpty()) {
            Label noContract = new Label("No active contract found.");
            noContract.setId("dashboard-no-contract");
            card.getChildren().addAll(title, noContract);
            return card;
        }

        int contractedHours = activeContract.get().hours();
        int workedHours = registrationDao.sumHoursThisWeek(accountId);

        double percentage = Math.min((workedHours / (contractedHours * 2.0)) * 100, 100);
        double progress = percentage / 100;

        ProgressBar meter = new ProgressBar(progress);
        meter.setMaxWidth(Double.MAX_VALUE);
        meter.setId("workload-meter");

        if (percentage < 40) meter.getStyleClass().add("workload-low");
        else if (percentage < 70) meter.getStyleClass().add("workload-medium");
        else meter.getStyleClass().add("workload-high");

        Label hoursLabel = new Label(String.format("%d / %d hours (%.0f%%)", workedHours, contractedHours, percentage));
        hoursLabel.setId("workload-hours");

        VBox meterBox = new VBox(8, meter, hoursLabel);
        meterBox.setAlignment(Pos.CENTER_LEFT);

        card.getChildren().addAll(title, meterBox);
        return card;
    }

}
