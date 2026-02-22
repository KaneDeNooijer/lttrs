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

    public CrudDialog(T existing) {
        boolean isEdit = existing != null;

        this.setTitle(isEdit ? "Edit " + getEntityName() : "Add " + getEntityName());

        ButtonType saveButtonType = new ButtonType(isEdit ? "Save" : "Add", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        // Fields are built here so subclass field initializers have already run
        VBox content = new VBox(10);
        content.getChildren().addAll(buildFields());
        this.getDialogPane().setContent(content);

        if (isEdit) populateFields(existing);

        this.setResultConverter(button -> button == saveButtonType ? buildResult() : null);
    }

    protected abstract String getEntityName();

    protected abstract List<Node> buildFields();

    protected abstract void populateFields(T existing);

    protected abstract T buildResult();

}
