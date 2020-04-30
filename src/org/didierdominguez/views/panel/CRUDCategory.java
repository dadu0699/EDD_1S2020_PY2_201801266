package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.controllers.CategoryController;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.ScreenSize;

public class CRUDCategory {
}

class CreateCategory {
    private static CreateCategory instance;

    private CreateCategory() {
    }

    static CreateCategory getInstance() {
        if (instance == null) {
            instance = new CreateCategory();
        }
        return instance;
    }

    public GridPane getGridPane() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setPadding(new Insets(0, 20, 10, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldName = new JFXTextField();
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        gridPane.add(fieldName, 0, 1);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 20);
        buttonAdd.setOnAction(event -> {
            if (SessionProperties.getInstance().isAuthenticated()) {
                if (fieldName.getText().length() == 0) {
                    Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
                } else {
                    Category category = CategoryController.getInstance().searchCategoryByName(fieldName.getText().trim());
                    if (category != null) {
                        Alert.getInstance().showAlert(gridPane, "ERROR", "LA CATEGORIA YA ESTÁ REGISTRADA");
                    } else {
                        CategoryController.getInstance().insert(fieldName.getText().trim(),
                                SessionProperties.getInstance().getUser());
                        CategoryView.getInstance().restartHBox();
                        Alert.getInstance().showNotification("CATEGORIAS", "CATEGORIA AGREGADA EXITOSAMENTE");
                    }
                }
            }
        });
        gridPane.add(buttonAdd, 0, 2);
        return gridPane;
    }
}

class ShowCategory {
    private static ShowCategory instance;

    private ShowCategory() {
    }

    static ShowCategory getInstance() {
        if (instance == null) {
            instance = new ShowCategory();
        }
        return instance;
    }

    GridPane getGridPane(Category category) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setPadding(new Insets(0, 20, 10, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        JFXTextField fieldName = new JFXTextField(category.getName());
        fieldName.setPromptText("NOMBRE");
        fieldName.setLabelFloat(true);
        fieldName.setPrefWidth(x);
        fieldName.setEditable(false);
        gridPane.add(fieldName, 0, 1);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 20);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("NOMBRE:        " + category.getName());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 2);

        return gridPane;
    }
}
