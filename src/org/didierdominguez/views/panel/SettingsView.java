package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.validation.RequiredFieldValidator;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.didierdominguez.controllers.NodeController;
import org.didierdominguez.util.ScreenSize;

public class SettingsView {
    private static SettingsView instance;

    private SettingsView() {}

    public static SettingsView getInstance() {
        if (instance == null) {
            instance = new SettingsView();
        }
        return instance;
    }

    public void show(HBox hBox, String title){
        JFXAlert<String> alert = new JFXAlert<>((Stage) hBox.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        RequiredFieldValidator validator = new RequiredFieldValidator();
        double x = ScreenSize.getInstance().getX();
        GridPane gridfields = new GridPane();
        gridfields.setVgap(25);
        gridfields.setPadding(new Insets(20));

        JFXTextField fieldIP = new JFXTextField();
        fieldIP.setPromptText("IP");
        fieldIP.setLabelFloat(true);
        fieldIP.setPrefWidth(x);
        gridfields.add(fieldIP, 0, 0, 2, 1);

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new VBox(gridfields));

        JFXButton cancelButton = new JFXButton("Cerrar");
        cancelButton.setCancelButton(true);
        cancelButton.getStyleClass().addAll("customButton", "primaryButton");
        cancelButton.setButtonType(JFXButton.ButtonType.FLAT);
        cancelButton.setOnAction(closeEvent -> alert.hideWithAnimation());

        JFXButton buttonAdd = new JFXButton("Aceptar");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setOnAction(event -> {
            NodeController.getInstance().addLastNode(fieldIP.getText());
            // NodeController.getInstance().readNodes();
        });

        layout.setActions(buttonAdd, cancelButton);
        alert.setContent(layout);
        alert.show();
    }
}
