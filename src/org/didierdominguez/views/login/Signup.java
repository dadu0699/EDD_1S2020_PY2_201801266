package org.didierdominguez.views.login;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.Main;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.beans.User;
import org.didierdominguez.controllers.UserController;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;
import org.didierdominguez.views.panel.AdministrativePanel;

public class Signup {
    private static Signup instance;

    private Signup() {
    }

    public static Signup getInstance() {
        if (instance == null) {
            instance = new Signup();
        }
        return instance;
    }

    public GridPane getSignup() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setId("gridPaneAccessWindow");
        gridPane.setVgap(15);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("CREAR UNA CUENTA");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("NÚMERO DE CARNET");
        fieldID.setPrefSize(x, y);
        gridPane.add(fieldID, 0, 1);

        JFXTextField fieldName = new JFXTextField();
        fieldName.setPromptText("NOMBRE");
        fieldName.setPrefSize(x, y);
        gridPane.add(fieldName, 0, 2);

        JFXTextField fieldLastName = new JFXTextField();
        fieldLastName.setPromptText("APELLIDO");
        fieldLastName.setId("fieldLastName");
        fieldLastName.setPrefSize(x, y);
        gridPane.add(fieldLastName, 0, 3);

        JFXTextField fieldCareer = new JFXTextField();
        fieldCareer.setPromptText("CARRERA");
        fieldCareer.setId("fieldCareer");
        fieldCareer.setPrefSize(x, y);
        gridPane.add(fieldCareer, 0, 4);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setId("fieldPassword");
        fieldPassword.setPrefSize(x, y);
        gridPane.add(fieldPassword, 0, 5);

        JFXButton buttonSignUp = new JFXButton("REGISTRARSE");
        buttonSignUp.getStyleClass().addAll("customButton", "primaryButton");
        buttonSignUp.setButtonType(JFXButton.ButtonType.FLAT);
        buttonSignUp.setPrefSize(x, y);
        buttonSignUp.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldID.getText().trim())
                    || fieldName.getText().length() == 0 || fieldLastName.getText().length() == 0
                    || fieldCareer.getText().length() == 0 || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                User user = UserController.getInstance().search(Integer.parseInt(fieldID.getText().trim()));
                if (user != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL USUARIO YA ESTÁ EN USO");
                } else {
                    UserController.getInstance().insert(Integer.parseInt(fieldID.getText().trim()),
                            fieldName.getText().trim(), fieldLastName.getText().trim(), fieldCareer.getText().trim(),
                            fieldPassword.getText().trim());
                    SessionProperties.getInstance()
                            .setUser(UserController.getInstance().search(Integer.parseInt(fieldID.getText().trim())));
                    AdministrativePanel.getInstance().showWindow();
                    Alert.getInstance().showNotification("REGISTRO", "REGISTRO REALIZADO EXITOSAMENTE");
                }
            }
        });
        GridPane.setMargin(buttonSignUp, new Insets(5, 0, 0, 0));
        gridPane.add(buttonSignUp, 0, 6);

        Text label = new Text("¿YA ERES USUARIO?");
        label.getStyleClass().add("textTitlehref");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setMargin(label, new Insets(-10, 100, 0, 0));
        gridPane.add(label, 0, 7);

        Text text = new Text("INICIAR SESIÓN");
        text.getStyleClass().add("texthref");
        GridPane.setHalignment(text, HPos.RIGHT);
        GridPane.setMargin(text, new Insets(-10, 0, 0, 0));
        gridPane.add(text, 0, 7);
        text.setOnMouseClicked(event -> Login.getInstance().showWindow());

        return gridPane;
    }

    public void showWindow() {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(480);
        stage.setHeight(375);
        stage.setMaximized(false);

        root.getChildren().addAll(getSignup());
        stage.show();
    }
}
