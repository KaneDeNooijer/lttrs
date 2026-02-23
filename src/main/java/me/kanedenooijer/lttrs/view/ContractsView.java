package me.kanedenooijer.lttrs.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import me.kanedenooijer.lttrs.database.dao.ContractDao;
import me.kanedenooijer.lttrs.database.entity.Contract;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.dialog.ContractDialog;
import me.kanedenooijer.lttrs.view.generic.CrudView;

import java.time.LocalDate;
import java.util.List;

/**
 * View for managing contracts.
 */
public final class ContractsView extends CrudView<Contract> {

    public ContractsView() {
        super(new ContractDao(DatabaseConnection.getConnection()));
    }

    @Override
    protected String getTitle() {
        return "Contract";
    }

    @Override
    protected int getId(Contract item) {
        return item.id();
    }

    @Override
    protected Contract applyAccountId(Contract result) {
        return new Contract(0, AccountSession.getAccount().id(), result.hours(), result.startDate(), result.endDate());
    }

    @Override
    protected Contract mergeForUpdate(Contract existing, Contract result) {
        return new Contract(existing.id(), existing.accountId(), result.hours(), result.startDate(), result.endDate());
    }

    @Override
    protected List<TableColumn<Contract, ?>> buildColumns() {
        TableColumn<Contract, Integer> hoursColumn = new TableColumn<>("Hours per week");
        hoursColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().hours()).asObject());

        TableColumn<Contract, LocalDate> startDateColumn = new TableColumn<>("Start date");
        startDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().startDate()));

        TableColumn<Contract, LocalDate> endDateColumn = new TableColumn<>("End date");
        endDateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().endDate()));

        return List.of(hoursColumn, startDateColumn, endDateColumn);
    }

    @Override
    protected Dialog<Contract> buildDialog(Contract existing) {
        return new ContractDialog(existing);
    }

}
