package me.kanedenooijer.lttrs.view.generic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.database.dao.GenericDao;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.component.CardComponent;

import java.util.List;

/**
 * Abstract base view for any CRUD-based view.
 * Subclasses define the title, columns, dialog, and DAO to use.
 *
 * @param <T> the entity type being managed
 */
public abstract class CrudView<T extends Record> extends GenericView {

    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final GenericDao<T> dao;

    public CrudView(GenericDao<T> dao) {
        this.dao = dao;

        VBox content = new VBox(20);

        CardComponent tableCard = buildTableCard();
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        content.getChildren().add(tableCard);
        VBox.setVgrow(content, Priority.ALWAYS);

        this.centerPane.getChildren().add(content);

        items.setAll(dao.findAll());
    }

    protected GenericDao<T> getDao() {
        return dao;
    }

    /**
     * Builds the table card with a header and action columns.
     */
    private CardComponent buildTableCard() {
        CardComponent card = new CardComponent();

        Label title = new Label(getTitle());
        title.getStyleClass().add("card-title");

        Button addButton = new Button("Add " + getTitle());
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(_ -> openDialog(null));

        HBox header = new HBox(10, title, addButton);
        header.setAlignment(Pos.CENTER_LEFT);

        TableView<T> table = new TableView<>(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getColumns().addAll(buildColumns());
        table.getColumns().add(buildActionsColumn());

        VBox content = new VBox(10);
        VBox.setVgrow(table, Priority.ALWAYS);
        content.getChildren().addAll(header, table);

        card.getChildren().add(content);
        return card;
    }

    /**
     * Builds the actions column with edit and delete buttons.
     */
    private TableColumn<T, Void> buildActionsColumn() {
        TableColumn<T, Void> actionsColumn = new TableColumn<>("Actions");

        actionsColumn.setCellFactory(_ -> new TableCell<>() {
            private final Button editButton = new Button("Edit");
            private final Button deleteButton = new Button("Delete");
            private final HBox actions = new HBox(8, editButton, deleteButton);

            {
                editButton.getStyleClass().add("table-edit-button");
                deleteButton.getStyleClass().add("table-delete-button");

                editButton.setOnAction(_ -> openDialog(getTableRow().getItem()));
                deleteButton.setOnAction(_ -> deleteItem(getTableRow().getItem()));
            }

            @Override
            protected void updateItem(Void item, boolean empty) {
                super.updateItem(item, empty);
                setGraphic(empty || getTableRow().getItem() == null ? null : actions);
            }
        });

        return actionsColumn;
    }

    /**
     * Opens the dialog for adding or editing an item.
     *
     * @param existing the item to edit, or null to add a new one
     */
    private void openDialog(T existing) {
        buildDialog(existing).showAndWait().ifPresent(result -> {
            if (existing == null) {
                getDao().create(applyAccountId(result)).ifPresentOrElse(
                        created -> {
                            items.add(created);
                            MainView.getInstance().showNotification(NotificationType.SUCCESS, getTitle() + " added successfully.");
                        },
                        () -> MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to save " + getTitle().toLowerCase() + ". Please try again.")
                );
            } else {
                T updated = mergeForUpdate(existing, result);
                if (getDao().update(updated)) {
                    items.set(items.indexOf(existing), updated);
                    MainView.getInstance().showNotification(NotificationType.SUCCESS, getTitle() + " updated successfully.");
                } else {
                    MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to update " + getTitle().toLowerCase() + ". Please try again.");
                }
            }
        });
    }

    /**
     * Deletes the given item from the database and removes it from the table.
     *
     * @param item the item to delete
     */
    private void deleteItem(T item) {
        if (getDao().delete(getId(item))) {
            items.remove(item);
            MainView.getInstance().showNotification(NotificationType.SUCCESS, getTitle() + " deleted.");
        } else {
            MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to delete " + getTitle().toLowerCase() + ". Please try again.");
        }
    }

    /**
     * The display title used for the card header and notifications.
     */
    protected abstract String getTitle();

    /**
     * The table columns to display, excluding the actions column.
     */
    protected abstract List<TableColumn<T, ?>> buildColumns();

    /**
     * Builds the dialog for adding or editing an item.
     *
     * @param existing the item to edit, or null to add a new one
     */
    protected abstract Dialog<T> buildDialog(T existing);

    /**
     * Returns the ID of the given item for deletion.
     *
     * @param item the item to get the ID of
     */
    protected abstract int getId(T item);

    /**
     * Returns the result with the correct account ID applied before creation.
     *
     * @param result the result from the dialog
     */
    protected abstract T applyAccountId(T result);

    /**
     * Merges the original item's identity fields with the edited result before updating.
     *
     * @param existing the original item
     * @param result   the result from the dialog
     */
    protected abstract T mergeForUpdate(T existing, T result);

}
