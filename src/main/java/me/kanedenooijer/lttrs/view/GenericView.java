package me.kanedenooijer.lttrs.view;

import javafx.scene.control.Button;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import me.kanedenooijer.lttrs.model.AccountSession;
import me.kanedenooijer.lttrs.type.NotificationType;

import java.util.Objects;

/**
 * Abstract base view that all application views extend.
 * Provides the shared navbar and sidebar.
 */
public abstract class GenericView extends BorderPane {

    protected StackPane centerPane;

    public GenericView() {
        this.setTop(buildNavbar());
        this.setLeft(buildSidebar());
        this.centerPane = buildCenter();
        this.setCenter(this.centerPane);
        this.getStylesheets().add(Objects.requireNonNull(getClass().getResource("/me/kanedenooijer/lttrs/style/generic.css")).toExternalForm());
    }

    /**
     * Builds the navbar containing the logo and account button.
     */
    private HBox buildNavbar() {
        HBox navbar = new HBox();
        navbar.setId("navbar");

        ImageView logo = buildIcon("/me/kanedenooijer/lttrs/image/logo-white.png", 200, 200);

        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        navbar.getChildren().addAll(logo, spacer, buildAccountButton());

        return navbar;
    }

    /**
     * Builds the account button showing the user's full name and initials.
     * Clicking it navigates to the dashboard.
     */
    private Button buildAccountButton() {
        Label initials = new Label(AccountSession.getAccount().initials());
        initials.setId("account-initials");

        Label fullName = new Label(AccountSession.getAccount().fullName());
        fullName.setGraphic(initials);
        fullName.setContentDisplay(ContentDisplay.RIGHT);

        Button accountButton = new Button();
        accountButton.setGraphic(fullName);
        accountButton.setId("account-button");
        accountButton.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));

        return accountButton;
    }

    /**
     * Builds the sidebar containing navigation items and a logout button.
     */
    private VBox buildSidebar() {
        VBox sidebar = new VBox();
        sidebar.setId("sidebar");

        Button dashboardItem = createSidebarItem("/me/kanedenooijer/lttrs/image/dashboard.png", "Dashboard");
        Button hourRegistrationItem = createSidebarItem("/me/kanedenooijer/lttrs/image/calendar.png", "Registrations");
        Button leaveItem = createSidebarItem("/me/kanedenooijer/lttrs/image/baggage.png", "Leaves");
        Button adminItem = createSidebarItem("/me/kanedenooijer/lttrs/image/sliders.png", "Admin");
        Button logoutItem = createSidebarItem("/me/kanedenooijer/lttrs/image/arrow-left.png", "Log out");

        dashboardItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        hourRegistrationItem.setOnAction(_ -> MainView.getInstance().switchView(new HourRegistrationsView()));
        leaveItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        adminItem.setOnAction(_ -> MainView.getInstance().switchView(new DashboardView()));
        logoutItem.setOnAction(_ -> {
            AccountSession.logout();
            MainView.getInstance().showNotification(NotificationType.SUCCESS, "You have been successfully logged out.");
            MainView.getInstance().switchView(new LoginView());
        });

        Region spacer = new Region();
        VBox.setVgrow(spacer, Priority.ALWAYS);

        sidebar.getChildren().addAll(dashboardItem, hourRegistrationItem, leaveItem, adminItem, spacer, logoutItem);

        return sidebar;
    }

    /**
     * Creates a styled sidebar button with an icon and a text label.
     *
     * @param iconPath path to the icon resource
     * @param text     label text shown next to the icon
     */
    private Button createSidebarItem(String iconPath, String text) {
        Button button = new Button(text, buildIcon(iconPath, 32, 32));
        button.getStyleClass().add("sidebar-item");

        return button;
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

    private StackPane buildCenter() {
        StackPane center = new StackPane();
        center.setId("center");

        return center;
    }
}
