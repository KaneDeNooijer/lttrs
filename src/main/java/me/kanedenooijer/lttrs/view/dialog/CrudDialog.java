package me.kanedenooijer.lttrs.view.dialog;

import javafx.scene.Node;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.layout.VBox;

import java.util.List;

/**
 * Abstract base dialog for adding or editing an entity.
 * Subclasses define the fields and how to build the result.
 *
 * @param <T> the entity type being managed
 */
public abstract class CrudDialog<T> extends Dialog<T> {

    public CrudDialog(T existing, String entityName) {
        boolean isEdit = existing != null;

        this.setTitle(isEdit ? "Edit " + entityName : "Add " + entityName);

        ButtonType saveButtonType = new ButtonType(isEdit ? "Save" : "Add", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        VBox content = new VBox(10);
        content.getChildren().addAll(buildFields());
        this.getDialogPane().setContent(content);

        if (isEdit) populateFields(existing);

        this.setResultConverter(button -> button == saveButtonType ? buildResult() : null);
    }

    protected abstract List<Node> buildFields();

    protected abstract void populateFields(T existing);

    protected abstract T buildResult();

}
