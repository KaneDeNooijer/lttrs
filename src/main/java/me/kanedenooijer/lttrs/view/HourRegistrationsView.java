package me.kanedenooijer.lttrs.view;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.scene.control.Dialog;
import javafx.scene.control.TableColumn;
import me.kanedenooijer.lttrs.database.dao.RegistrationDao;
import me.kanedenooijer.lttrs.database.entity.Registration;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.model.DatabaseConnection;
import me.kanedenooijer.lttrs.view.dialog.RegistrationDialog;
import me.kanedenooijer.lttrs.view.generic.CrudView;

import java.time.LocalDate;
import java.util.List;

/**
 * View for managing hour registrations.
 */
public final class HourRegistrationsView extends CrudView<Registration> {

    private final RegistrationDao registrationDao = new RegistrationDao(DatabaseConnection.getConnection());

    public HourRegistrationsView() {
        super(new RegistrationDao(DatabaseConnection.getConnection()));
    }

    @Override
    protected String getTitle() {
        return "Registration";
    }

    @Override
    protected int getId(Registration item) {
        return item.id();
    }

    @Override
    protected Registration applyAccountId(Registration result) {
        return new Registration(0, AccountSession.getAccount().id(), result.hours(), result.date());
    }

    @Override
    protected Registration mergeForUpdate(Registration existing, Registration result) {
        return new Registration(existing.id(), existing.accountId(), result.hours(), result.date());
    }

    @Override
    protected List<TableColumn<Registration, ?>> buildColumns() {
        TableColumn<Registration, Integer> hoursColumn = new TableColumn<>("Hours");
        hoursColumn.setCellValueFactory(cell -> new SimpleIntegerProperty(cell.getValue().hours()).asObject());

        TableColumn<Registration, LocalDate> dateColumn = new TableColumn<>("Date");
        dateColumn.setCellValueFactory(cell -> new SimpleObjectProperty<>(cell.getValue().date()));

        return List.of(hoursColumn, dateColumn);
    }

    @Override
    protected Dialog<Registration> buildDialog(Registration existing) {
        return new RegistrationDialog(existing);
    }

}
