package me.kanedenooijer.lttrs.view.generic;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.type.NotificationType;
import me.kanedenooijer.lttrs.view.*;

import java.util.Objects;

/**
 * Abstract base view that all application views extend.
 * Provides the shared navbar and sidebar.
 */
public abstract class BaseView extends BorderPane {

    /**
     * The center pane where specific view content is displayed.
     */
    protected StackPane center;

    public BaseView() {
        this.setTop(this.buildNavbar());
        this.setLeft(this.buildSidebar());
        this.setCenter(this.center = this.buildCenter());
    }

    /**
     * Builds the navbar containing the logo and account button.
     */
    private HBox buildNavbar() {
        HBox parent = new HBox();
        parent.setId("navbar");

        ImageView logo = buildIcon("/me/kanedenooijer/lttrs/image/logo-white.png", 200, 200);

        Label initials = new Label(AccountSession.getAccount().initials());
        initials.getStyleClass().add("initials");

        Label fullName = new Label(AccountSession.getAccount().fullName());
        fullName.setGraphic(initials);
        fullName.setContentDisplay(ContentDisplay.RIGHT);

        Button button = new Button();
        button.setGraphic(fullName);
        button.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        parent.getChildren().addAll(logo, spacer, button);

        return parent;
    }

    /**
     * Builds the sidebar containing navigation items and a logout button.
     */
    private VBox buildSidebar() {
        VBox parent = new VBox();
        parent.setId("sidebar");

        Button dashboardItem = this.createSidebarItem("/me/kanedenooijer/lttrs/image/dashboard.png", "Dashboard");
        Button contractItem = this.createSidebarItem("/me/kanedenooijer/lttrs/image/notebook.png", "Contracts");
        Button hourRegistrationItem = this.createSidebarItem("/me/kanedenooijer/lttrs/image/calendar.png", "Registrations");
        Button leaveItem = this.createSidebarItem("/me/kanedenooijer/lttrs/image/baggage.png", "Leaves");
        Button logoutItem = this.createSidebarItem("/me/kanedenooijer/lttrs/image/arrow-left.png", "Log out");

        dashboardItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        contractItem.setOnAction(_ -> MainView.getInstance().switchView(new ContractsView()));
        hourRegistrationItem.setOnAction(_ -> MainView.getInstance().switchView(new HourRegistrationsView()));
        leaveItem.setOnAction(_ -> MainView.getInstance().switchView(new LeavesView()));
        logoutItem.setOnAction(_ -> {
            AccountSession.logout();
            MainView.getInstance().switchView(new LoginView());
            MainView.getInstance().showNotification(NotificationType.SUCCESS, "You have been successfully logged out.");
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        parent.getChildren().addAll(dashboardItem, contractItem, hourRegistrationItem, leaveItem, spacer, logoutItem);

        return parent;
    }

    /**
     * Builds the center pane which will be populated by specific views.
     */
    private StackPane buildCenter() {
        StackPane parent = new StackPane();
        parent.setId("center");

        return parent;
    }

    /**
     * Creates a styled sidebar button with an icon and a text label.
     *
     * @param iconPath path to the icon resource
     * @param text     label text shown next to the icon
     */
    private Button createSidebarItem(String iconPath, String text) {
        Button parent = new Button(text, buildIcon(iconPath, 32, 32));
        parent.getStyleClass().add("sidebar-item");

        return parent;
    }

    /**
     * Loads an image resource and returns a sized ImageView.
     *
     * @param path   classpath resource path to the image
     * @param width  desired display width in pixels
     * @param height desired display height in pixels
     */
    private ImageView buildIcon(String path, double width, double height) {
        ImageView icon = new ImageView(Objects.requireNonNull(getClass().getResource(path)).toExternalForm());
        icon.setFitWidth(width);
        icon.setFitHeight(height);
        icon.setPreserveRatio(true);

        return icon;
    }

}
