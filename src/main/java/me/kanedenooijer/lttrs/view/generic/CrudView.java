package me.kanedenooijer.lttrs.view.generic;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import me.kanedenooijer.lttrs.database.dao.GenericDao;
import me.kanedenooijer.lttrs.type.NotificationType;

import java.util.List;

/**
 * Abstract base view for any CRUD-based view.
 * Subclasses define the title, columns, dialog, and how to handle entities.
 *
 * @param <T> the entity type being managed
 */
public abstract class CrudView<T extends Record> extends BaseView {

    private final ObservableList<T> items = FXCollections.observableArrayList();
    private final GenericDao<T> dao;

    public CrudView(GenericDao<T> dao) {
        this.dao = dao;

        VBox content = new VBox(20);

        VBox tableCard = buildTableCard();
        VBox.setVgrow(tableCard, Priority.ALWAYS);

        content.getChildren().add(tableCard);
        VBox.setVgrow(content, Priority.ALWAYS);

        this.center.getChildren().add(content);

        items.setAll(dao.findAll());
    }

    /**
     * Builds the table card with a header and action columns.
     */
    private VBox buildTableCard() {
        Label title = new Label(getTitle());
        title.getStyleClass().add("title");

        Button addButton = new Button("Add " + getTitle());
        addButton.getStyleClass().add("primary-button");
        addButton.setOnAction(_ -> openDialog(null));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        HBox header = new HBox(10, title, spacer, addButton);
        header.setAlignment(Pos.CENTER_LEFT);

        StackPane card = new StackPane();
        card.getStyleClass().add("card");

        TableView<T> table = new TableView<>(items);
        table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_FLEX_LAST_COLUMN);
        VBox.setVgrow(table, Priority.ALWAYS);

        table.getColumns().addAll(buildColumns());
        table.getColumns().add(buildActionsColumn());

        card.getChildren().add(table);

        VBox parent = new VBox(10, header, card);
        VBox.setVgrow(card, Priority.ALWAYS);

        return parent;
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
                actions.setAlignment(Pos.CENTER);

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
                dao.create(applyAccountId(result)).ifPresentOrElse(
                        created -> {
                            items.add(created);
                            MainView.getInstance().showNotification(NotificationType.SUCCESS, getTitle() + " added successfully.");
                        },
                        () -> MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to save " + getTitle().toLowerCase() + ". Please try again.")
                );
            } else {
                T updated = mergeForUpdate(existing, result);
                if (dao.update(updated)) {
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
        if (dao.delete(getId(item))) {
            items.remove(item);
            MainView.getInstance().showNotification(NotificationType.SUCCESS, getTitle() + " deleted.");
        } else {
            MainView.getInstance().showNotification(NotificationType.ERROR, "Failed to delete " + getTitle().toLowerCase() + ". Please try again.");
        }
    }

    protected abstract String getTitle();

    protected abstract List<TableColumn<T, ?>> buildColumns();

    protected abstract Dialog<T> buildDialog(T existing);

    protected abstract int getId(T item);

    protected abstract T applyAccountId(T result);

    protected abstract T mergeForUpdate(T existing, T result);

}
