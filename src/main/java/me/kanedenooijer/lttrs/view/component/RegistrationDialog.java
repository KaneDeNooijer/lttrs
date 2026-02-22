package me.kanedenooijer.lttrs.view.component;

import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import me.kanedenooijer.lttrs.database.entity.Registration;

import java.time.LocalDate;

/**
 * Modal dialog for adding or editing a registration.
 * Pre-populates fields when an existing registration is provided.
 */
public final class RegistrationDialog extends Dialog<Registration> {

    private final TextField hoursField = new TextField();
    private final DatePicker datePicker = new DatePicker();

    public RegistrationDialog(Registration existing) {
        boolean isEdit = existing != null;

        this.setTitle(isEdit ? "Edit Registration" : "Add Registration");

        ButtonType saveButtonType = new ButtonType(isEdit ? "Save" : "Add", ButtonBar.ButtonData.OK_DONE);
        this.getDialogPane().getButtonTypes().addAll(saveButtonType, ButtonType.CANCEL);

        if (isEdit) {
            hoursField.setText(String.valueOf(existing.hours()));
            datePicker.setValue(existing.date());
        } else {
            hoursField.setPromptText("Hours");
            datePicker.setPromptText("Select date");
        }

        VBox content = new VBox(10);
        content.getChildren().addAll(
                new Label("Hours:"), hoursField,
                new Label("Date:"), datePicker
        );

        this.getDialogPane().setContent(content);

        this.setResultConverter(button -> {
            if (button != saveButtonType) return null;

            String hoursText = hoursField.getText().trim();
            LocalDate date = datePicker.getValue();

            if (hoursText.isBlank() || date == null) return null;

            try {
                return new Registration(0, 0, Integer.parseInt(hoursText), date);
            } catch (NumberFormatException e) {
                return null;
            }
        });
    }

}
