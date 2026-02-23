package me.kanedenooijer.lttrs.view;

import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import me.kanedenooijer.lttrs.database.dao.LeaveDao;
import me.kanedenooijer.lttrs.database.entity.Leave;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.dialog.LeaveDialog;
import me.kanedenooijer.lttrs.view.generic.CrudView;

import java.time.LocalDate;
import java.util.List;

/**
 * View for managing leaves.
 */
public final class LeavesView extends CrudView<Leave> {

    private final LeaveDao leaveDao = new LeaveDao(DatabaseConnection.getConnection());

    public LeavesView() {
        super(new LeaveDao(DatabaseConnection.getConnection()));
    }

    @Override
    protected String getTitle() {
        return "Leave";
    }

    @Override
    protected int getId(Leave item) {
        return item.id();
    }

    @Override
    protected Leave applyAccountId(Leave result) {
        return new Leave(0, AccountSession.getAccount().id(), result.startDate(), result.endDate());
    }

    @Override
    protected Leave mergeForUpdate(Leave existing, Leave result) {
        return new Leave(existing.id(), existing.accountId(), result.startDate(), result.endDate());
    }

    @Override
    protected List<TableColumn<Leave, ?>> buildColumns() {
        TableColumn<Leave, LocalDate> startDateColumn = new TableColumn<>("Start date");
        startDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().startDate()));

        TableColumn<Leave, LocalDate> endDateColumn = new TableColumn<>("End date");
        endDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().endDate()));

        return List.of(startDateColumn, endDateColumn);
    }

    @Override
    protected Dialog<Leave> buildDialog(Leave existing) {
        return new LeaveDialog(existing);
    }

}
