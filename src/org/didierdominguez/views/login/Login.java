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

public class Login {
    private static Login instance;

    private Login() {
    }

    public static Login getInstance() {
        if (instance == null) {
            instance = new Login();
        }
        return instance;
    }

    public GridPane getLogin() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setId("gridPaneAccessWindow");
        gridPane.setVgap(15);
        gridPane.setHgap(10);
        gridPane.setPadding(new Insets(20));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("INICIAR SESIÓN");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldUser = new JFXTextField();
        fieldUser.setPromptText("CARNET");
        fieldUser.setId("fieldUser");
        fieldUser.setPrefSize(x, y);
        gridPane.add(fieldUser, 0, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setId("fieldPassword");
        fieldPassword.setPrefSize(x, y);
        gridPane.add(fieldPassword, 0, 2);

        JFXButton buttonSignIn = new JFXButton("INICIAR SESIÓN");
        buttonSignIn.getStyleClass().addAll("customButton", "primaryButton");
        buttonSignIn.setButtonType(JFXButton.ButtonType.FLAT);
        buttonSignIn.setPrefSize(x, y);
        buttonSignIn.setOnAction(event -> {
            if (fieldUser.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldUser.getText().trim())
                    || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                User user = UserController.getInstance().search(Integer.parseInt(fieldUser.getText().trim()),
                        fieldPassword.getText());
                if (user != null) {
                    SessionProperties.getInstance().setUser(user);
                    AdministrativePanel.getInstance().showWindow();
                } else {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL CARNET Y LA CONTRASEÑA QUE "
                            + "INGRESASTE NO COINCIDEN CON NUESTROS REGISTROS. POR FAVOR, REVISA E INTÉNTALO DE NUEVO.");
                }
            }
        });
        GridPane.setMargin(buttonSignIn, new Insets(5, 0, 0, 0));
        gridPane.add(buttonSignIn, 0, 3);

        Text label = new Text("¿NUEVO EN EL SITIO?");
        label.getStyleClass().add("textTitlehref");
        GridPane.setHalignment(label, HPos.RIGHT);
        GridPane.setMargin(label, new Insets(-10, 130, 0, 0));
        gridPane.add(label, 0, 4);

        Text text = new Text("CREAR UNA CUENTA");
        text.getStyleClass().add("texthref");
        GridPane.setHalignment(text, HPos.RIGHT);
        GridPane.setMargin(text, new Insets(-10, 0, 0, 0));
        gridPane.add(text, 0, 4);
        text.setOnMouseClicked(event -> Signup.getInstance().showWindow());

        return gridPane;
    }

    public void showWindow() {
        Stage stage = Main.getStage();
        VBox root = Main.getRoot();

        root.getChildren().clear();

        stage.hide();
        stage.setWidth(480);
        stage.setHeight(270);
        stage.setMaximized(false);
        stage.centerOnScreen();

        root.getChildren().addAll(getLogin());
        stage.show();
    }
}
