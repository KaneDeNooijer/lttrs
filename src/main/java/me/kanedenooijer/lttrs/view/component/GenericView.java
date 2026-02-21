package me.kanedenooijer.lttrs.view.component;

import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.DashboardView;
import me.kanedenooijer.lttrs.view.LoginView;
import me.kanedenooijer.lttrs.view.MainView;

import java.util.Objects;

public abstract class GenericView extends BorderPane {

    public GenericView() {
        HBox topBar = new HBox();
        topBar.setId("top-bar");

        ImageView logo = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/logo-white.png")).toExternalForm());
        logo.setFitWidth(200);
        logo.setFitHeight(200);
        logo.setPreserveRatio(true);

        Region navbarSpacer = new Region();
        HBox.setHgrow(navbarSpacer, Priority.ALWAYS);

        Label fullName = new Label(AccountSession.getInstance().getAccount().fullName());

        topBar.getChildren().addAll(logo, navbarSpacer, fullName);

        VBox leftBar = new VBox();
        leftBar.setId("left-bar");

        Button dashboardItem = createSidebarItem("/me/kanedenooijer/lttrs/image/dashboard.png", "Dashboard");
        Button registrationItem = createSidebarItem("/me/kanedenooijer/lttrs/image/calendar.png", "Registrations");
        Button leaveItem = createSidebarItem("/me/kanedenooijer/lttrs/image/baggage.png", "Leaves");
        Button adminItem = createSidebarItem("/me/kanedenooijer/lttrs/image/sliders.png", "Admin");
        Button logoutItem = createSidebarItem("/me/kanedenooijer/lttrs/image/arrow-left.png", "Log out");

        dashboardItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        registrationItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        leaveItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        adminItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        logoutItem.setOnAction(_ -> this.logout());

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        leftBar.getChildren().addAll(dashboardItem, registrationItem, leaveItem, adminItem, sidebarSpacer, logoutItem);

        this.setTop(topBar);
        this.setLeft(leftBar);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/generic.css")).toExternalForm());
    }

    private Button createSidebarItem(String iconPath, String text) {
        ImageView icon = new ImageView(Objects.requireNonNull(getClass().getResource(iconPath)).toExternalForm());
        icon.setFitWidth(32);
        icon.setFitHeight(32);
        icon.setPreserveRatio(true);

        Button button = new Button(text, icon);
        button.getStyleClass().add("sidebar-item");

        return button;
    }

    private void logout() {
        AccountSession.getInstance().logout();
        MainView.getInstance().showNotification(NotificationType.SUCCESS, "You have been successfully logged out.");
        MainView.getInstance().switchView(new LoginView());
    }
}