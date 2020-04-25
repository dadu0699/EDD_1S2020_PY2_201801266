package org.didierdominguez.util;

import com.jfoenix.controls.JFXAlert;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDialogLayout;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.controlsfx.control.Notifications;
import org.didierdominguez.Main;

import static javafx.geometry.Pos.TOP_RIGHT;

public class Alert extends Stage {
    private static Alert instance;

    private Alert() {
    }

    public static Alert getInstance() {
        if (instance == null) {
            instance = new Alert();
        }
        return instance;
    }

    public void showAlert(GridPane gridPane, String title, String description) {
        JFXAlert<String> alert = new JFXAlert<>((Stage) gridPane.getScene().getWindow());
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setOverlayClose(false);

        JFXDialogLayout layout = new JFXDialogLayout();
        layout.setHeading(new Label(title));
        layout.setBody(new Label(description));

        JFXButton cancelButton = new JFXButton("Close");
        cancelButton.setCancelButton(true);
        cancelButton.getStyleClass().addAll("customButton", "primaryButton");
        cancelButton.setButtonType(JFXButton.ButtonType.FLAT);
        cancelButton.setOnAction(closeEvent -> alert.hideWithAnimation());

        layout.setActions(cancelButton);
        alert.setContent(layout);
        alert.show();
    }

    public void showNotification(String title, String description) {
        Notifications.create().title(title).text(description).hideAfter(Duration.millis(2500)).position(TOP_RIGHT)
                .owner(Main.getStage()).show();
    }
}
