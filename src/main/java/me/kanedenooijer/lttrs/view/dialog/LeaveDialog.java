package me.kanedenooijer.lttrs.view.dialog;

import javafx.scene.Node;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import me.kanedenooijer.lttrs.database.entity.Leave;

import java.time.LocalDate;
import java.util.List;

/**
 * Dialog for adding or editing a leave.
 */
public final class LeaveDialog extends CrudDialog<Leave> {

    private DatePicker startDatePicker;
    private DatePicker endDatePicker;

    public LeaveDialog(Leave existing) {
        super(existing, "Leave");
    }

    @Override
    protected List<Node> buildFields() {
        startDatePicker = new DatePicker();
        startDatePicker.setPromptText("Start date");

        endDatePicker = new DatePicker();
        endDatePicker.setPromptText("End date");

        return List.of(
                new Label("Start date:"), startDatePicker,
                new Label("End date:"), endDatePicker
        );
    }

    @Override
    protected void populateFields(Leave existing) {
        startDatePicker.setValue(existing.startDate());
        endDatePicker.setValue(existing.endDate());
    }

    @Override
    protected Leave buildResult() {
        LocalDate startDate = startDatePicker.getValue();
        LocalDate endDate = endDatePicker.getValue();

        if (startDate == null || endDate == null || endDate.isBefore(startDate)) return null;

        return new Leave(0, 0, startDate, endDate);
    }

}
