package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.geometry.Insets;
import javafx.scene.input.Clipboard;
import javafx.scene.input.ClipboardContent;
import javafx.scene.layout.GridPane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.controllers.CategoryController;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.JSONBlock;
import org.didierdominguez.util.ScreenSize;
import org.didierdominguez.util.Verifications;

public class CRUDBook {
}

class CreateBook {
    private static CreateBook instance;

    private CreateBook() {
    }

    static CreateBook getInstance() {
        if (instance == null) {
            instance = new CreateBook();
        }
        return instance;
    }


    public GridPane getGridPane(Category category) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(0, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("AGREGAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);
        gridPane.setMargin(textTitle, new Insets(0, 0, -15, 0));

        JFXTextField fieldID = new JFXTextField();
        fieldID.setPromptText("ISBN");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        gridPane.add(fieldID, 1, 1);

        JFXTextField fieldTitle = new JFXTextField();
        fieldTitle.setPromptText("TITLE");
        fieldTitle.setLabelFloat(true);
        fieldTitle.setPrefWidth(x);
        gridPane.add(fieldTitle, 1, 2);

        JFXTextField fieldAuthor = new JFXTextField();
        fieldAuthor.setPromptText("AUTOR");
        fieldAuthor.setLabelFloat(true);
        fieldAuthor.setPrefWidth(x);
        gridPane.add(fieldAuthor, 1, 3);

        JFXTextField fieldEditorial = new JFXTextField();
        fieldEditorial.setPromptText("EDITORIAL");
        fieldEditorial.setLabelFloat(true);
        fieldEditorial.setPrefWidth(x);
        gridPane.add(fieldEditorial, 1, 4);

        JFXTextField fieldYear = new JFXTextField();
        fieldYear.setPromptText("AÑO");
        fieldYear.setLabelFloat(true);
        fieldYear.setPrefWidth(x);
        gridPane.add(fieldYear, 1, 5);

        JFXTextField fieldEdition = new JFXTextField();
        fieldEdition.setPromptText("EDICION");
        fieldEdition.setLabelFloat(true);
        fieldEdition.setPrefWidth(x);
        gridPane.add(fieldEdition, 1, 6);

        JFXTextField fieldLanguage = new JFXTextField();
        fieldLanguage.setPromptText("LENGUAJE");
        fieldLanguage.setLabelFloat(true);
        fieldLanguage.setPrefWidth(x);
        gridPane.add(fieldLanguage, 1, 7);

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y / 4);
        buttonAdd.setOnAction(event -> {
            if (fieldID.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldID.getText().trim())
                    || fieldTitle.getText().length() == 0
                    || fieldAuthor.getText().length() == 0
                    || fieldEditorial.getText().length() == 0
                    || fieldYear.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldYear.getText().trim())
                    || fieldEdition.getText().length() == 0
                    || !Verifications.getInstance().isNumericInteger(fieldEdition.getText().trim())
                    || fieldLanguage.getText().length() == 0) {
                Alert.getInstance().showAlert(gridPane, "ERROR", "UNO O MÁS DATOS SON INCORRECTOS");
            } else {
                if (SessionProperties.getInstance().isAuthenticated()) {
                    Book book = null;
                    for (Book books : CategoryController.getInstance().getBooks()) {
                        if (books.getISBN() == Integer.parseInt(fieldID.getText().trim())) {
                            book = books;
                        }
                    }

                    if (book != null) {
                        Alert.getInstance().showAlert(gridPane, "ERROR", "EL LIBRO YA ESTÁ REGISTRADO");
                    } else {
                        CategoryController.getInstance().searchCategory(category).getBooks().add(
                                Integer.parseInt(fieldID.getText().trim()), fieldTitle.getText(), fieldAuthor.getText(),
                                fieldEditorial.getText(), Integer.parseInt(fieldYear.getText().trim()),
                                Integer.parseInt(fieldEdition.getText().trim()), category, fieldLanguage.getText(),
                                SessionProperties.getInstance().getUser());
                        JSONBlock.getInstance().addBook(new Book(
                                Integer.parseInt(fieldID.getText().trim()), fieldTitle.getText(), fieldAuthor.getText(),
                                fieldEditorial.getText(), Integer.parseInt(fieldYear.getText().trim()),
                                Integer.parseInt(fieldEdition.getText().trim()), category, fieldLanguage.getText(),
                                SessionProperties.getInstance().getUser()));
                        CategoryView.getInstance().updateTableViewItemsBook(category);
                        Alert.getInstance().showNotification("CATEGORIAS", "LIBRO AGREGADO EXITOSAMENTE");
                    }
                }
            }
        });
        gridPane.add(buttonAdd, 0, 8, 2, 1);
        gridPane.setMargin(buttonAdd, new Insets(-5, 0, -10, 0));

        return gridPane;
    }
}

class ShowBook {
    private static ShowBook instance;

    private ShowBook() {
    }

    static ShowBook getInstance() {
        if (instance == null) {
            instance = new ShowBook();
        }
        return instance;
    }

    GridPane getGridPane(Book book) {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(25);
        gridPane.setHgap(5);
        gridPane.setPadding(new Insets(0, 20, 20, 10));
        // gridPane.setGridLinesVisible(true);

        Text textTitle = new Text("MOSTRAR");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0, 2, 1);
        gridPane.setMargin(textTitle, new Insets(0, 0, -15, 0));

        JFXTextField fieldID = new JFXTextField(String.valueOf(book.getISBN()));
        fieldID.setPromptText("ISBN");
        fieldID.setLabelFloat(true);
        fieldID.setPrefWidth(x);
        fieldID.setEditable(false);
        gridPane.add(fieldID, 1, 1);

        JFXTextField fieldTitle = new JFXTextField(book.getTitle());
        fieldTitle.setPromptText("TITLE");
        fieldTitle.setLabelFloat(true);
        fieldTitle.setPrefWidth(x);
        fieldTitle.setEditable(false);
        gridPane.add(fieldTitle, 1, 2);

        JFXTextField fieldAuthor = new JFXTextField(book.getAuthor());
        fieldAuthor.setPromptText("AUTOR");
        fieldAuthor.setLabelFloat(true);
        fieldAuthor.setPrefWidth(x);
        fieldAuthor.setEditable(false);
        gridPane.add(fieldAuthor, 1, 3);

        JFXTextField fieldEditorial = new JFXTextField(book.getEditorial());
        fieldEditorial.setPromptText("EDITORIAL");
        fieldEditorial.setLabelFloat(true);
        fieldEditorial.setPrefWidth(x);
        fieldEditorial.setEditable(false);
        gridPane.add(fieldEditorial, 1, 4);

        JFXTextField fieldYear = new JFXTextField(String.valueOf(book.getYear()));
        fieldYear.setPromptText("AÑO");
        fieldYear.setLabelFloat(true);
        fieldYear.setPrefWidth(x);
        fieldYear.setEditable(false);
        gridPane.add(fieldYear, 1, 5);

        JFXTextField fieldEdition = new JFXTextField(String.valueOf(book.getEdition()));
        fieldEdition.setPromptText("EDICION");
        fieldEdition.setLabelFloat(true);
        fieldEdition.setPrefWidth(x);
        fieldEdition.setEditable(false);
        gridPane.add(fieldEdition, 1, 6);

        JFXTextField fieldLanguage = new JFXTextField(book.getLanguage());
        fieldLanguage.setPromptText("LENGUAJE");
        fieldLanguage.setLabelFloat(true);
        fieldLanguage.setPrefWidth(x);
        fieldLanguage.setEditable(false);
        gridPane.add(fieldLanguage, 1, 7);

        JFXButton buttonCopy = new JFXButton("COPIAR");
        buttonCopy.getStyleClass().addAll("customButton", "primaryButton");
        buttonCopy.setButtonType(JFXButton.ButtonType.FLAT);
        buttonCopy.setPrefSize(x, y / 4);
        buttonCopy.setOnAction(event -> {
            final Clipboard clipboard = Clipboard.getSystemClipboard();
            final ClipboardContent content = new ClipboardContent();
            content.putString("ISBN:          " + book.getISBN()
                    + "\nTITULO:        " + book.getTitle()
                    + "\nAUTOR:         " + book.getAuthor()
                    + "\nEDITORIAL:     " + book.getEditorial()
                    + "\nAÑO:           " + book.getYear()
                    + "\nEDICION:       " + book.getEdition()
                    + "\nCATEGORIA:     " + book.getCategory()
                    + "\nIDIOMA:        " + book.getLanguage());
            clipboard.setContent(content);
        });
        gridPane.add(buttonCopy, 0, 8, 2, 1);
        gridPane.setMargin(buttonCopy, new Insets(-5, 0, -10, 0));

        return gridPane;
    }
}