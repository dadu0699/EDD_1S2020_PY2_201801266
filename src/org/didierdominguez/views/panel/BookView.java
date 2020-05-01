package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.beans.User;
import org.didierdominguez.controllers.CategoryController;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.util.ArrayList;

public class BookView {
    private static BookView instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ObservableList observableList;

    private BookView() {
    }

    public static BookView getInstance() {
        if (instance == null) {
            instance = new BookView();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }

    private void updateObservableList() {
        ArrayList<Book> arrayListBook = new ArrayList<>();
        arrayListBook = CategoryController.getInstance().getBooks();
        observableList = FXCollections.observableArrayList(arrayListBook);
    }

    private void updateObservableListSearch(String search) {
        observableList = FXCollections.observableArrayList(CategoryController.getInstance().searchBooks(search));
    }

    public void updateTableViewItems() {
        updateObservableList();
        tableView.setItems(observableList);
    }

    public void updateTableViewItemsSearch(String search) {
        updateObservableListSearch(search);
        tableView.setItems(observableList);
    }

    public HBox getViewBook() {
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

        Text textTitle = new Text("LIBROS");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        HBox hBoxButtons = new HBox();
        JFXTextField textFieldSearch = new JFXTextField();
        textFieldSearch.setPromptText("BUSCAR");
        textFieldSearch.setPrefSize(x, y);
        textFieldSearch.textProperty().addListener((ObservableValue<? extends String> observable,
                                                    String oldValue, String newValue) -> {
            updateTableViewItemsSearch(textFieldSearch.getText().trim());
        });

        JFXButton buttonFile = new JFXButton("ARCHIVO");
        buttonFile.getStyleClass().addAll("customButton", "primaryButton");
        buttonFile.setButtonType(JFXButton.ButtonType.FLAT);
        buttonFile.setPrefSize(x, y);
        buttonFile.setOnAction(event -> {
            if (SessionProperties.getInstance().isAuthenticated()) {
                FileControl.getInstance().uploadFile("Book File", "*.json");
                FileControl.getInstance().readBookJSON();
                updateTableViewItems();
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Book book = (Book) tableView.getSelectionModel().getSelectedItem();
            if (book != null && SessionProperties.getInstance().isAuthenticated()
                    && book.getUser().getID() == SessionProperties.getInstance().getUser().getID()) {
                CategoryController.getInstance().deleteBook(
                        (Category) ((Book) tableView.getSelectionModel().getSelectedItem()).getCategory(), book);

                updateTableViewItems();
                Alert.getInstance().showNotification("LIBROS", "LIBRO ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(textFieldSearch, buttonFile, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(textFieldSearch, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<Book, Integer> columnISBN = new TableColumn<>("ISBN");
        columnISBN.setPrefWidth((3 * x / 4) / 8);
        columnISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TableColumn<Book, String> columnTitle = new TableColumn<>("TITULO");
        columnTitle.setPrefWidth((3 * x / 4) / 4);
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> columnAuthor = new TableColumn<>("AUTOR");
        columnAuthor.setPrefWidth((3 * x / 4) / 4);
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book, String> columnEditorial = new TableColumn<>("EDITORIAL");
        columnEditorial.setPrefWidth((3 * x / 4) / 6);
        columnEditorial.setCellValueFactory(new PropertyValueFactory<>("editorial"));
        TableColumn<Book, Integer> columnYear = new TableColumn<>("AÑO");
        columnYear.setPrefWidth((3 * x / 4) / 10);
        columnYear.setCellValueFactory(new PropertyValueFactory<>("year"));
        TableColumn<Book, Integer> columnEdition = new TableColumn<>("EDICION");
        columnEdition.setPrefWidth((3 * x / 4) / 10);
        columnEdition.setCellValueFactory(new PropertyValueFactory<>("edition"));
        TableColumn<Book, Category> columnCategory = new TableColumn<>("CATEGORIA");
        columnCategory.setPrefWidth((3 * x / 4) / 6);
        columnCategory.setCellValueFactory(new PropertyValueFactory<>("Category"));
        TableColumn<Book, String> columnLanguage = new TableColumn<>("IDIOMA");
        columnLanguage.setPrefWidth((3 * x / 4) / 10);
        columnLanguage.setCellValueFactory(new PropertyValueFactory<>("language"));
        TableColumn<Book, User> columnUser = new TableColumn<>("USUARIO");
        columnUser.setPrefWidth((3 * x / 4) / 6);
        columnUser.setCellValueFactory(new PropertyValueFactory<>("user"));

        updateObservableList();
        tableView = new TableView<>(observableList);
        tableView.getColumns().addAll(columnISBN, columnTitle, columnAuthor, columnEditorial, columnYear,
                columnEdition, columnCategory, columnLanguage, columnUser);
        tableView.setPrefSize(x, 7 * y / 8);
        gridPane.add(tableView, 0, 2);
        hBox.getChildren().add(gridPane);

        return hBox;
    }
}
