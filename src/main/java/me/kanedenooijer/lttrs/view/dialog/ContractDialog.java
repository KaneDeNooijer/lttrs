package me.kanedenooijer.lttrs.view.dialog;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import me.kanedenooijer.lttrs.database.entity.Contract;

import java.time.LocalDate;
import java.util.List;

/**
 * Dialog for adding or editing a contract.
 */
public final class ContractDialog extends CrudDialog<Contract> {

    private TextField hoursField;
    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public ContractDialog(Contract existing) {
        super(existing, "Contract");
    }

    @Override
    protected List<Node> buildFields() {
        hoursField = new TextField();
        hoursField.setPromptText("Hours per week");

        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start date");

        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End date");

        return List.of(
                new Label("Hours per week:"), hoursField,
                new Label("Start date:"), startDatePicker,
                new Label("End date:"), endDatePicker
        );
    }

    @Override
    protected void populateFields(Contract existing) {
        hoursField.setText(String.valueOf(existing.hours()));
        startDatePicker.setValue(existing.startDate());
        endDatePicker.setValue(existing.endDate());
    }

    @Override
    protected Contract buildResult() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null || endDate.isBefore(startDate)) return null;

        try {
            return new Contract(0, 0, Integer.parseInt(hoursField.getText().trim()), startDate, endDate);
        } catch (NumberFormatException e) {
            return null;
        }
    }

}
