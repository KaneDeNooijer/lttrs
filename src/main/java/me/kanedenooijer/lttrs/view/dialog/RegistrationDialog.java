package me.kanedenooijer.lttrs.view.dialog;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.kanedenooijer.lttrs.database.entity.Registration;

import java.time.LocalDate;
import java.util.List;

/**
 * Dialog for adding or editing a registration.
 */
public final class RegistrationDialog extends CrudDialog<Registration> {

    private TextField hoursField;
    private DatePicker datePicker;

    public RegistrationDialog(Registration existing) {
        super(existing);
    }

    @Override
    protected String getEntityName() {
        return "Registration";
    }

    @Override
    protected List<Node> buildFields() {
        hoursField = new TextField();
        hoursField.setPromptText("Hours");

        datePicker = new DatePicker();
        datePicker.setPromptText("Select date");

        return List.of(
                new Label("Hours:"), hoursField,
                new Label("Date:"), datePicker
        );
    }

    @Override
    protected void populateFields(Registration existing) {
        hoursField.setText(String.valueOf(existing.hours()));
        datePicker.setValue(existing.date());
    }

    @Override
    protected Registration buildResult() {
        LocalDate date = datePicker.getValue();
        if (date == null) return null;

        try {
            return new Registration(0, 0, Integer.parseInt(hoursField.getText().trim()), date);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
