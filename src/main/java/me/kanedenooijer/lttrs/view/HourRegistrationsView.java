package me.kanedenooijer.lttrs.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.database.dao.RegistrationDao;
import me.kanedenooijer.lttrs.database.entity.Registration;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.component.CardComponent;
import me.kanedenooijer.lttrs.view.component.RegistrationDialog;

import java.time.LocalDate;
import java.util.Objects;

/**
 * View for managing hour registrations.
 * Contains a table card listing all existing ones with add, edit and delete actions.
 */
public final class HourRegistrationsView extends GenericView {

    private final ObservableList<Registration> registrations = FXCollections.observableArrayList();
    private final RegistrationDao registrationDao = new RegistrationDao(DatabaseConnection.getConnection());

    public HourRegistrationsView() {
        VBox content = new VBox(20);
        content.setId("hour-registrations");

        CardComponent tableCard = buildTableCard();
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        content.getChildren().add(tableCard);
        VBox.setVgrow(content, Priority.ALWAYS);

        registrations.setAll(registrationDao.findAll());

        this.centerPane.getChildren().add(content);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/hour-registrations.css")).toExternalForm());
    }

    /**
     * Builds the table card listing all existing registrations with add, edit and delete actions.
     */
    private CardComponent buildTableCard() {
        CardComponent card = new CardComponent();

        Label title = new Label("Registrations");
        title.getStyleClass().add("card-title");

        Button addButton = new Button("Add Registration");
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(_ -> openDialog(null));

        HBox header = new HBox(10, title, addButton);
        header.setAlignment(Pos.CENTER_LEFT);

        TableView<Registration> table = new TableView<>(registrations);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(table, Priority.ALWAYS);

        TableColumn<Registration, Integer> hoursColumn = new TableColumn<>("Hours");
        hoursColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().hours()).asObject());

        TableColumn<Registration, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().date()));

        TableColumn<Registration, Void> actionsColumn = new TableColumn<>("Actions");
        actionsColumn.setCellFactory(_ -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actions = new HBox(8, editButton, deleteButton);

            {
                editButton.getStyleClass().add("table-edit-button");
                deleteButton.getStyleClass().add("table-delete-button");

                editButton.setOnAction(_ -> openDialog(getTableRow().getItem()));
                deleteButton.setOnAction(_ -> deleteRegistration(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableRow().getItem() == null ? null : actions);
            }
        });

        table.getColumns().addAll(hoursColumn, dateColumn, actionsColumn);

        VBox content = new VBox(10);
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(header, table);

        card.getChildren().add(content);
        return card;
    }

    /**
     * Opens the registration dialog for adding or editing a registration.
     * Passing null opens it in add mode, passing a registration opens it in edit mode.
     *
     * @param existing the registration to edit, or null to add a new one
     */
    private void openDialog(Registration existing) {
        new RegistrationDialog(existing).showAndWait().ifPresent(result -> {
            if (existing == null) {
                registrationDao.create(new Registration(0, AccountSession.getAccount().id(), result.hours(), result.date())).ifPresentOrElse(
                        created -> {
                            registrations.add(created);
                            MainView.getInstance().showNotification(NotificationType.SUCCESS, "Registration added successfully.");
                        },
                        () -> MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to save registration. Please try again.")
                );

                return;
            }

            Registration updated = new Registration(existing.id(), existing.accountId(), result.hours(), result.date());

            if (registrationDao.update(updated)) {
                registrations.set(registrations.indexOf(existing), updated);
                MainView.getInstance().showNotification(NotificationType.SUCCESS, "Registration updated successfully.");
                return;
            }

            MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to update registration. Please try again.");
        });
    }

    /**
     * Deletes the given registration from the database and removes it from the table.
     *
     * @param registration the registration to delete
     */
    private void deleteRegistration(Registration registration) {
        if (registrationDao.delete(registration.id())) {
            registrations.remove(registration);
            MainView.getInstance().showNotification(NotificationType.SUCCESS, "Registration deleted.");
            return;
        }

        MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to delete registration. Please try again.");
    }

}
