package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import javafx.scene.Node;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.didierdominguez.Main;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.controllers.BlockController;
import org.didierdominguez.util.JSONBlock;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.views.login.Login;

public class AdministrativePanel {
    private static AdministrativePanel instance;
    private VBox vBoxButtons;

    private AdministrativePanel() {
    }

    public static AdministrativePanel getInstance() {
        if (instance == null) {
            instance = new AdministrativePanel();
        }
        return instance;
    }

    public HBox getPane() {
        HBox hBox = new HBox();
        vBoxButtons = new VBox();
        VBox vBoxPanels = new VBox();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        hBox.setPrefSize(x, y);
        vBoxButtons.setPrefSize((x / 4), y);
        vBoxPanels.setPrefSize((3 * x / 4), y);
        vBoxPanels.getChildren().add(UserView.getInstance().getViewUser());

        JFXButton buttonHeader = new JFXButton(" USAC\nLibrary");
        buttonHeader.setDisable(true);
        buttonHeader.getStyleClass().addAll("headerButton", "panelButton");
        buttonHeader.setPrefSize(x, 2 * y / 8);
        buttonHeader.setButtonType(JFXButton.ButtonType.FLAT);

        JFXButton btnUsers = new JFXButton("USUARIOS");
        btnUsers.setId("btnUsers");
        btnUsers.getStyleClass().addAll("panelButton", "primaryButton");
        btnUsers.setPrefSize(x, y / 8);
        btnUsers.setButtonType(JFXButton.ButtonType.FLAT);
        selectButton(btnUsers);
        btnUsers.setOnAction(event -> {
            selectButton(btnUsers);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(UserView.getInstance().getViewUser());
        });

        JFXButton btnBooks = new JFXButton("LIBROS");
        btnBooks.setId("btnBooks");
        btnBooks.getStyleClass().addAll("panelButton", "primaryButton");
        btnBooks.setPrefSize(x, y / 8);
        btnBooks.setButtonType(JFXButton.ButtonType.FLAT);
        btnBooks.setOnAction(event -> {
            selectButton(btnBooks);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(BookView.getInstance().getViewBook());
        });

        JFXButton btnCategories = new JFXButton("CATEGORIAS");
        btnCategories.setId("btnCategories");
        btnCategories.getStyleClass().addAll("panelButton", "primaryButton");
        btnCategories.setPrefSize(x, y / 8);
        btnCategories.setButtonType(JFXButton.ButtonType.FLAT);
        btnCategories.setOnAction(event -> {
            selectButton(btnCategories);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(CategoryView.getInstance().getViewCategoryDetail());
        });

        JFXButton buttonReport = new JFXButton("REPORTES");
        buttonReport.setId("buttonReport");
        buttonReport.getStyleClass().addAll("panelButton", "primaryButton");
        buttonReport.setPrefSize(x, y / 8);
        buttonReport.setButtonType(JFXButton.ButtonType.FLAT);
        buttonReport.setOnAction(event -> {
            selectButton(buttonReport);
            vBoxPanels.getChildren().clear();
            vBoxPanels.getChildren().add(ReportView.getInstance().getReportView());
        });

        JFXButton btnSyncUp = new JFXButton("SINCRONIZAR");
        btnSyncUp.setId("btnSyncUp");
        btnSyncUp.getStyleClass().addAll("panelButton", "primaryButton");
        btnSyncUp.setPrefSize(x, y / 8);
        btnSyncUp.setButtonType(JFXButton.ButtonType.FLAT);
        btnSyncUp.setOnAction(event -> {
            if (JSONBlock.getInstance().isPossible()) {
                BlockController.getInstance().addLastNode();
            }
            // vBoxPanels.getChildren().add(ViewCustomer.getInstance().getViewCustomerDetail());
        });

        JFXButton btnSettings = new JFXButton("CONFIGURACIONES");
        btnSettings.setId("btnSettings");
        btnSettings.getStyleClass().addAll("panelButton", "primaryButton");
        btnSettings.setPrefSize(x, y / 8);
        btnSettings.setButtonType(JFXButton.ButtonType.FLAT);
        btnSettings.setOnAction(event -> {
            // selectButton(btnSettings);
            SettingsView.getInstance().show(hBox, "CONFIGURACION IP");
        });

        String message = !SessionProperties.getInstance().isAuthenticated() ? "INICIAR SESIÓN" : "CERRAR SESIÓN";
        JFXButton btnLogOut = new JFXButton(message);
        btnLogOut.setId("btnLogOut");
        btnLogOut.getStyleClass().addAll("panelButton", "primaryButton");
        btnLogOut.setPrefSize(x, y / 8);
        btnLogOut.setButtonType(JFXButton.ButtonType.FLAT);
        btnLogOut.setOnAction(event -> Login.getInstance().showWindow());

        vBoxButtons.getChildren().addAll(buttonHeader, btnUsers, btnBooks, btnCategories, buttonReport, btnSyncUp,
                btnSettings, btnLogOut);
        hBox.getChildren().addAll(vBoxButtons, vBoxPanels);

        return hBox;
    }

    private void selectButton(JFXButton jfxButton) {
        for (Node node : vBoxButtons.getChildren()) {
            if (node instanceof JFXButton) {
                if (node.getId() != null) {
                    node.getStyleClass().remove("selectedPanelButton");
                    node.getStyleClass().add("primaryButton");
                }
            }
        }
        jfxButton.getStyleClass().remove("primaryButton");
        jfxButton.getStyleClass().add("selectedPanelButton");
    }

    public void showWindow() {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(ScreenSize.getInstance().getX());
        stage.setHeight(720);
        stage.centerOnScreen();
        stage.setMaximized(true);

        root.getChildren().addAll(getPane());
        stage.show();
    }
}
