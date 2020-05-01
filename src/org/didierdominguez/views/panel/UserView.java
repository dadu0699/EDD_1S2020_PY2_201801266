package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.beans.User;
import org.didierdominguez.controllers.UserController;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;

import java.util.ArrayList;

public class UserView extends Stage {
    private static UserView instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private UserView() {
    }

    public static UserView getInstance() {
        if (instance == null) {
            instance = new UserView();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }

    private void updateObservableList() {
        ArrayList<User> arrayListUser = UserController.getInstance().getHashTable();
        if (observableList != null) {
            observableList.clear();
        }
        observableList = FXCollections.observableArrayList(arrayListUser);
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public HBox getViewUser() {
        hBox = new HBox();
        gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(20, 10, 20, 20));
        // gridPane.setGridLinesVisible(true);
        gridPane.setMinWidth(x / 2);
        gridPane.setPrefSize(x, y);
        hBox.setPrefSize(x, y);

        Text textTitle = new Text("USUARIOS");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        HBox hBoxButtons = new HBox();
        JFXButton buttonFile = new JFXButton("ARCHIVO");
        buttonFile.getStyleClass().addAll("customButton", "primaryButton");
        buttonFile.setButtonType(JFXButton.ButtonType.FLAT);
        buttonFile.setPrefSize(x, y);
        buttonFile.setOnAction(event -> {
            FileControl.getInstance().uploadFile("User File", "*.json");
            FileControl.getInstance().readUserJSON();
            updateTableViewItems();
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> {
            hBox.getChildren().clear();
            hBox.getChildren().addAll(gridPane, CreateUser.getInstance().getGridPane());
        });

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "warningButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y);
        buttonUpdate.setOnAction(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                User user = (User) tableView.getSelectionModel().getSelectedItem();
                if (SessionProperties.getInstance().isAuthenticated()
                        && SessionProperties.getInstance().getUser() == user) {
                    hBox.getChildren().addAll(gridPane, UpdateUser.getInstance().getGridPane(user));
                } else {
                    hBox.getChildren().addAll(gridPane, ShowUser.getInstance().getGridPane(user));
                }
            } else {
                hBox.getChildren().add(gridPane);
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            User user = (User) tableView.getSelectionModel().getSelectedItem();
            if (user != null && SessionProperties.getInstance().isAuthenticated()
                    && user.getID() == SessionProperties.getInstance().getUser().getID()) {
                restartHBox();
                UserController.getInstance().remove(user.getID());
                updateTableViewItems();
                Alert.getInstance().showNotification("USUARIO", "USUARIO ELIMINADO EXITOSAMENTE");
                SessionProperties.getInstance().setUser(null);
                AdministrativePanel.getInstance().showWindow();
            }
        });

        hBoxButtons.getChildren().addAll(buttonFile, buttonAdd, buttonUpdate, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonUpdate, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<User, Integer> columnID = new TableColumn<>("CARNET");
        columnID.setPrefWidth((3 * x / 4) / 4);
        columnID.setCellValueFactory(new PropertyValueFactory<>("ID"));
        TableColumn<User, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth((3 * x / 4) / 4);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
        TableColumn<User, User> columnUser = new TableColumn<>("APELLIDO");
        columnUser.setPrefWidth((3 * x / 4) / 4);
        columnUser.setCellValueFactory(new PropertyValueFactory<>("lastName"));
        TableColumn<User, String> columnCareer = new TableColumn<>("CARRERA");
        columnCareer.setPrefWidth((3 * x / 4) / 4);
        columnCareer.setCellValueFactory(new PropertyValueFactory<>("career"));
        TableColumn<User, String> columnPassword = new TableColumn<>("CONTRASEÑA");
        columnPassword.setPrefWidth((3 * x / 4) / 4);
        columnPassword.setCellValueFactory(new PropertyValueFactory<>("password"));

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnID, columnName, columnUser, columnCareer, columnPassword);
        tableView.setPrefSize(x, 7 * y / 8);
        tableView.setOnMouseClicked(event -> {
            hBox.getChildren().clear();
            if (tableView.getSelectionModel().getSelectedItem() != null) {
                hBox.getChildren().addAll(gridPane,
                        ShowUser.getInstance().getGridPane((User) tableView.getSelectionModel().getSelectedItem()));
            } else {
                hBox.getChildren().add(gridPane);
            }
        });
        gridPane.add(tableView, 0, 2);
        hBox.getChildren().add(gridPane);

        return hBox;
    }
}

class CreateUser {
    private static CreateUser instance;

    private CreateUser() {
    }

    static CreateUser getInstance() {
        if (instance == null) {
            instance = new CreateUser();
        }
        return instance;
    }

    GridPane getGridPane() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("NÚMERO DE CARNET");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 0, 6, 2, 1);

        JFXTextField fieldName = new JFXTextField();
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 7, 2, 1);

        JFXTextField fieldLastName = new JFXTextField();
        fieldLastName.setPromptText("APELLIDO");
        fieldLastName.setLabelFloat(true);
        fieldLastName.setPrefWidth(x);
        gridPane.add(fieldLastName, 0, 8, 2, 1);

        JFXTextField fieldCareer = new JFXTextField();
        fieldCareer.setPromptText("CARRERA");
        fieldCareer.setLabelFloat(true);
        fieldCareer.setPrefWidth(x);
        gridPane.add(fieldCareer, 0, 9, 2, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setLabelFloat(true);
        fieldPassword.setPrefWidth(x);
        gridPane.add(fieldPassword, 0, 10, 2, 1);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 20);
        buttonAdd.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldID.getText().trim())
                    || fieldName.getText().length() == 0 || fieldLastName.getText().length() == 0
                    || fieldCareer.getText().length() == 0 || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                User user = UserController.getInstance().search(Integer.parseInt(fieldID.getText().trim()));
                if (user != null) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "EL USUARIO YA ESTÁ REGISTRADO");
                } else {
                    UserController.getInstance().insert(Integer.parseInt(fieldID.getText().trim()),
                            fieldName.getText().trim(), fieldLastName.getText().trim(), fieldCareer.getText().trim(),
                            fieldPassword.getText().trim());
                    UserView.getInstance().updateTableViewItems();
                    Alert.getInstance().showNotification("USUARIO", "REGISTRO REALIZADO EXITOSAMENTE");
                }
            }
        });
        gridPane.add(buttonAdd, 0, 11);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> UserView.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 11);

        return gridPane;
    }
}

class UpdateUser {
    private static UpdateUser instance;

    private UpdateUser() {
    }

    static UpdateUser getInstance() {
        if (instance == null) {
            instance = new UpdateUser();
        }
        return instance;
    }

    GridPane getGridPane(User user) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MODIFICAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldID = new JFXTextField(String.valueOf(user.getID()));
        fieldID.setPromptText("NÚMERO DE CARNET");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        fieldID.setEditable(false);
        gridPane.add(fieldID, 0, 6, 2, 1);

        JFXTextField fieldName = new JFXTextField(user.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 7, 2, 1);

        JFXTextField fieldLastName = new JFXTextField(user.getLastName());
        fieldLastName.setPromptText("APELLIDO");
        fieldLastName.setLabelFloat(true);
        fieldLastName.setPrefWidth(x);
        gridPane.add(fieldLastName, 0, 8, 2, 1);

        JFXTextField fieldCareer = new JFXTextField(user.getCareer());
        fieldCareer.setPromptText("CARRERA");
        fieldCareer.setLabelFloat(true);
        fieldCareer.setPrefWidth(x);
        gridPane.add(fieldCareer, 0, 9, 2, 1);

        JFXPasswordField fieldPassword = new JFXPasswordField();
        fieldPassword.setPromptText("CONTRASEÑA");
        fieldPassword.setAccessibleText(user.getPassword());
        fieldPassword.setLabelFloat(true);
        fieldPassword.setPrefWidth(x);
        gridPane.add(fieldPassword, 0, 10, 2, 1);

        JFXButton buttonUpdate = new JFXButton("MODIFICAR");
        buttonUpdate.getStyleClass().addAll("customButton", "primaryButton");
        buttonUpdate.setButtonType(JFXButton.ButtonType.FLAT);
        buttonUpdate.setPrefSize(x, y / 20);
        buttonUpdate.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldID.getText().trim())
                    || fieldName.getText().length() == 0 || fieldLastName.getText().length() == 0
                    || fieldCareer.getText().length() == 0 || fieldPassword.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR",
                        "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                UserController.getInstance().update(user.getID(), fieldName.getText().trim(),
                        fieldLastName.getText().trim(), fieldPassword.getText().trim(),
                        fieldPassword.getText().trim().toUpperCase());
                if (UserController.getInstance().update()) {
                    Alert.getInstance().showNotification("USUARIOS",
                            "USUARIO ACTUALIZADO EXITOSAMENTE");
                } else {
                    Alert.getInstance().showAlert(gridPane, "ERROR",
                            "ERROR AL MODIFICAR USUARIO");
                }
                UserView.getInstance().updateTableViewItems();
            }
        });
        gridPane.add(buttonUpdate, 0, 11);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> UserView.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 11);

        return gridPane;
    }
}

class ShowUser {
    private static ShowUser instance;

    private ShowUser() {
    }

    static ShowUser getInstance() {
        if (instance == null) {
            instance = new ShowUser();
        }
        return instance;
    }

    GridPane getGridPane(User user) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(20, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 5, 2, 1);

        JFXTextField fieldID = new JFXTextField(String.valueOf(user.getID()));
        fieldID.setPromptText("NÚMERO DE CARNET");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        fieldID.setEditable(false);
        gridPane.add(fieldID, 0, 6, 2, 1);

        JFXTextField fieldName = new JFXTextField(user.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        fieldName.setEditable(false);
        gridPane.add(fieldName, 0, 7, 2, 1);

        JFXTextField fieldLastName = new JFXTextField(user.getLastName());
        fieldLastName.setPromptText("APELLIDO");
        fieldLastName.setLabelFloat(true);
        fieldLastName.setPrefWidth(x);
        fieldLastName.setEditable(false);
        gridPane.add(fieldLastName, 0, 8, 2, 1);

        JFXTextField fieldCareer = new JFXTextField(user.getCareer());
        fieldCareer.setPromptText("CARRERA");
        fieldCareer.setLabelFloat(true);
        fieldCareer.setPrefWidth(x);
        fieldCareer.setEditable(false);
        gridPane.add(fieldCareer, 0, 9, 2, 1);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 20);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("CARNET:        " + user.getID() + "\nNOMBRE:        " + user.getName()
                    + "\nAPELLIDO:      " + user.getLastName() + "\nCARRERA:       " + user.getCareer());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 10);

        JFXButton buttonCancel = new JFXButton("CANCELAR");
        buttonCancel.getStyleClass().addAll("customButton", "dangerButton");
        buttonCancel.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCancel.setPrefSize(x, y / 20);
        buttonCancel.setOnAction(event -> UserView.getInstance().restartHBox());
        gridPane.add(buttonCancel, 1, 10);

        return gridPane;
    }
}
