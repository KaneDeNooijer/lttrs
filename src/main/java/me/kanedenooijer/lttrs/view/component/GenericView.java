package me.kanedenooijer.lttrs.view.component;

import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;

import java.util.Objects;

/**
 * A generic view that can be extended by other views in the application,
 * it provides the top and left bar for the application.
 */
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

        Label fullName = new Label("Full Name");

        topBar.getChildren().addAll(logo, navbarSpacer, fullName);

        VBox leftBar = new VBox();
        leftBar.setId("left-bar");

        HBox dashboardItem = new HBox();
        ImageView dashboardIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/dashboard.png")).toExternalForm());
        dashboardIcon.setFitWidth(32);
        dashboardIcon.setFitHeight(32);
        dashboardIcon.setPreserveRatio(true);
        Label dashboardLabel = new Label("Dashboard");
        dashboardItem.getStyleClass().add("sidebar-item");
        dashboardItem.getChildren().addAll(dashboardIcon, dashboardLabel);

        HBox registrationItem = new HBox();
        ImageView registrationIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/calendar.png")).toExternalForm());
        registrationIcon.setFitWidth(32);
        registrationIcon.setFitHeight(32);
        registrationIcon.setPreserveRatio(true);
        Label registrationLabel = new Label("Registrations");
        registrationItem.getStyleClass().add("sidebar-item");
        registrationItem.getChildren().addAll(registrationIcon, registrationLabel);

        HBox leaveItem = new HBox();
        ImageView leaveIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/baggage.png")).toExternalForm());
        leaveIcon.setFitWidth(32);
        leaveIcon.setFitHeight(32);
        leaveIcon.setPreserveRatio(true);
        Label leaveLabel = new Label("Leaves");
        leaveItem.getStyleClass().add("sidebar-item");
        leaveItem.getChildren().addAll(leaveIcon, leaveLabel);

        HBox adminItem = new HBox();
        ImageView adminIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/sliders.png")).toExternalForm());
        adminIcon.setFitWidth(32);
        adminIcon.setFitHeight(32);
        adminIcon.setPreserveRatio(true);
        Label adminLabel = new Label("Admin");
        adminItem.getStyleClass().add("sidebar-item");
        adminItem.getChildren().addAll(adminIcon, adminLabel);

        Region sidebarSpacer = new Region();
        VBox.setVgrow(sidebarSpacer, Priority.ALWAYS);

        HBox logoutItem = new HBox();
        ImageView logoutIcon = new ImageView(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/image/arrow-left.png")).toExternalForm());
        logoutIcon.setFitWidth(32);
        logoutIcon.setFitHeight(32);
        logoutIcon.setPreserveRatio(true);
        Label logoutLabel = new Label("Log out");
        logoutItem.getStyleClass().add("sidebar-item");
        logoutItem.getChildren().addAll(logoutIcon, logoutLabel);

        leftBar.getChildren().addAll(dashboardItem, registrationItem, leaveItem, adminItem, sidebarSpacer, logoutItem);

        this.setTop(topBar);
        this.setLeft(leftBar);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/generic.css")).toExternalForm());
    }

}
