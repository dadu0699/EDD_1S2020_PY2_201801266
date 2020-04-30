package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.didierdominguez.beans.Book;
import org.didierdominguez.beans.Category;
import org.didierdominguez.beans.SessionProperties;
import org.didierdominguez.beans.User;
import org.didierdominguez.controllers.CategoryController;
import org.didierdominguez.util.Alert;
import org.didierdominguez.util.FileControl;
import org.didierdominguez.util.ScreenSize;

import java.util.ArrayList;

public class CategoryView extends Stage {
    private static CategoryView instance;
    private HBox hBoxPanels;
    private VBox vBoxApplications;
    private TableView tableViewCategory;
    private TableView tableViewBook;
    private ObservableList observableListCategory;
    private ObservableList observableListBook;

    private CategoryView() {
    }

    public static CategoryView getInstance() {
        if (instance == null) {
            instance = new CategoryView();
        }
        return instance;
    }

    public void restartHBox() {
        hBoxPanels.getChildren().clear();
        vBoxApplications.getChildren().clear();
        vBoxApplications.getChildren().addAll(CreateCategory.getInstance().getGridPane(), getViewBook());
        hBoxPanels.getChildren().addAll(getViewCategory(), vBoxApplications);
        updateTableViewItemsBook(null);
    }

    private void updateObservableListCategory() {
        ArrayList<Category> arrayListCategory = new ArrayList<>();
        for (Category category : CategoryController.getInstance().getCategories()) {
            arrayListCategory.add(category);
        }

        if (observableListCategory != null) {
            observableListCategory.clear();
        }
        observableListCategory = FXCollections.observableArrayList(arrayListCategory);
    }

    private void updateObservableListBook(Category category) {
        ArrayList<Book> arrayListBook = new ArrayList<>();
        if (category != null) {
            for (Book book : CategoryController.getInstance().searchBooksCategory(category)) {
                arrayListBook.add(book);
            }
        }

        if (observableListBook != null) {
            observableListBook.clear();
        }
        observableListBook = FXCollections.observableArrayList(arrayListBook);
    }

    public void updateTableViewItemsCategory() {
        updateObservableListCategory();
        tableViewCategory.setItems(observableListCategory);
    }

    public void updateTableViewItemsBook(Category category) {
        updateObservableListBook(category);
        tableViewBook.setItems(observableListBook);
    }

    public VBox getViewCategoryDetail() {
        VBox vBox = new VBox();
        GridPane gridPaneTitle = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPaneTitle.setVgap(10);
        gridPaneTitle.setPadding(new Insets(20));
        // gridPaneTitle.setGridLinesVisible(true);
        gridPaneTitle.setMinWidth(x / 2);
        gridPaneTitle.setPrefSize(x, y / 8);
        vBox.setPrefSize(x, y);

        Text textTitle = new Text("CATEGORIAS / LIBROS");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPaneTitle.add(textTitle, 0, 0);

        gridPaneTitle.setPadding(new Insets(20, 20, -10, 20));
        hBoxPanels = new HBox();
        hBoxPanels.setPrefSize(x, 7 * y / 8);

        vBoxApplications = new VBox();
        vBoxApplications.setPrefSize(x, 7 * y / 8);
        vBoxApplications.getChildren().addAll(CreateCategory.getInstance().getGridPane(), getViewBook());

        hBoxPanels.getChildren().addAll(getViewCategory(), vBoxApplications);
        vBox.getChildren().addAll(gridPaneTitle, hBoxPanels);
        return vBox;
    }

    private GridPane getViewCategory() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(5, 10, 20, 20));
        gridPane.setPrefSize(x, y);

        Text textTitle = new Text("CATEGORIAS");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(20));
        gridPane.add(textTitle, 0, 0);

        JFXTextField textFieldSearch = new JFXTextField();
        textFieldSearch.setPromptText("BUSCAR");
        textFieldSearch.setPrefSize(x, y / 8);
        gridPane.add(textFieldSearch, 0, 1);

        HBox hBoxButtons = new HBox();
        JFXButton buttonFile = new JFXButton("ARCHIVO");
        buttonFile.getStyleClass().addAll("customButton", "primaryButton");
        buttonFile.setButtonType(JFXButton.ButtonType.FLAT);
        buttonFile.setPrefSize(x, y);
        buttonFile.setOnAction(event -> {
            /*FileControl.getInstance().uploadFile("CustomerFile", "*.tmca");
            ArrayList<String> arrayList = FileControl.getInstance().readFile();
            if (arrayList != null) {
                for (String command: arrayList) {
                    String[] params = command.split("-");
                    if (ControllerCustomer.getInstance().searchCustomer(Integer.parseInt(params[0])) == null) {
                        boolean role = params[4].equalsIgnoreCase("ORO");
                        ControllerCustomer.getInstance().createCustomer(Integer.parseInt(params[0]),
                                params[1], role, params[2], params[3]);
                        String[] cars = params[5].split(";");
                        Customer customer = ControllerCustomer.getInstance().searchCustomer(Integer.parseInt(params[0]));
                        if (customer != null) {
                            for (String car: cars) {
                                String[] carDetail = car.split(",");
                                ControllerCar.getInstance().createCar(carDetail[0], carDetail[1], carDetail[2], carDetail[3], customer);
                            }
                        }
                    }
                }
                updateTableViewItemsCustomer();
                Alert.getInstance().showNotification("CLIENTES", "ARCHIVO LEIDO EXITOSAMENTE");
            }*/
        });

        JFXButton buttonAdd = new JFXButton("AGREGAR");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> restartHBox());

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Category category = (Category) tableViewCategory.getSelectionModel().getSelectedItem();
            if (category != null && SessionProperties.getInstance().isAuthenticated()
                    && category.getUser().getID() == SessionProperties.getInstance().getUser().getID()) {
                CategoryController.getInstance().delete(category.getName());
                restartHBox();
                Alert.getInstance().showNotification("CATEGORIAS", "CATEGORIA ELIMINADA EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(buttonFile, buttonAdd, buttonDelete);
        hBoxButtons.setMargin(buttonFile, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        hBoxButtons.setPrefSize(x, y / 8);
        gridPane.add(hBoxButtons, 0, 2);

        TableColumn<Category, String> columnName = new TableColumn<>("NOMBRE");
        columnName.setPrefWidth(x / 4);
        columnName.setCellValueFactory(new PropertyValueFactory<>("name"));

        updateObservableListCategory();
        tableViewCategory = new TableView<>(observableListCategory);
        tableViewCategory.getColumns().addAll(columnName);
        tableViewCategory.setOnMouseClicked(event -> {
            if (tableViewCategory.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, ShowCategory.getInstance().getGridPane(
                        (Category) tableViewCategory.getSelectionModel().getSelectedItem()));
                updateTableViewItemsBook((Category) tableViewCategory.getSelectionModel().getSelectedItem());
            }
        });
        tableViewCategory.setPrefSize(x, 7 * y / 8);

        gridPane.add(tableViewCategory, 0, 3);
        return gridPane;
    }

    private GridPane getViewBook() {
        GridPane gridPane = new GridPane();

        double x = ScreenSize.getInstance().getX();
        double y = ScreenSize.getInstance().getY();

        gridPane.setVgap(10);
        gridPane.setPadding(new Insets(10, 20, 20, 10));
        gridPane.setPrefSize(x, y);

        Text textTitleC = new Text("LIBROS");
        textTitleC.getStyleClass().add("textTitle");
        textTitleC.setFont(new Font(20));
        gridPane.add(textTitleC, 0, 0);

        HBox hBoxButtons = new HBox();

        JFXButton buttonAdd = new JFXButton("AGREGAR LIBRO");
        buttonAdd.getStyleClass().addAll("customButton", "primaryButton");
        buttonAdd.setButtonType(JFXButton.ButtonType.FLAT);
        buttonAdd.setPrefSize(x, y);
        buttonAdd.setOnAction(event -> {
            if (tableViewCategory.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, CreateBook.getInstance()
                        .getGridPane((Category) tableViewCategory.getSelectionModel().getSelectedItem()));
            }
        });

        JFXButton buttonDelete = new JFXButton("ELIMINAR");
        buttonDelete.getStyleClass().addAll("customButton", "dangerButton");
        buttonDelete.setButtonType(JFXButton.ButtonType.FLAT);
        buttonDelete.setPrefSize(x, y);
        buttonDelete.setOnAction(event -> {
            Book book = (Book) tableViewBook.getSelectionModel().getSelectedItem();
            if (book != null && SessionProperties.getInstance().isAuthenticated()
                    && book.getUser().getID() == SessionProperties.getInstance().getUser().getID()) {
                CategoryController.getInstance().deleteBook(
                        (Category) tableViewCategory.getSelectionModel().getSelectedItem(), book);
                vBoxApplications.getChildren().remove(0);
                vBoxApplications.getChildren().add(0, ShowCategory.getInstance().getGridPane(
                        (Category) tableViewCategory.getSelectionModel().getSelectedItem()));
                updateTableViewItemsBook((Category) tableViewCategory.getSelectionModel().getSelectedItem());
                Alert.getInstance().showNotification("LIBROS", "LIBRO ELIMINADO EXITOSAMENTE");
            }
        });

        hBoxButtons.getChildren().addAll(buttonAdd, buttonDelete);
        hBoxButtons.setPrefSize(x, y / 8);
        hBoxButtons.setMargin(buttonAdd, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1);

        TableColumn<Book, Integer> columnISBN = new TableColumn<>("ISBN");
        columnISBN.setPrefWidth(x / 10);
        columnISBN.setCellValueFactory(new PropertyValueFactory<>("ISBN"));
        TableColumn<Book, String> columnTitle = new TableColumn<>("TITULO");
        columnTitle.setPrefWidth(x / 10);
        columnTitle.setCellValueFactory(new PropertyValueFactory<>("title"));
        TableColumn<Book, String> columnAuthor = new TableColumn<>("AUTOR");
        columnAuthor.setPrefWidth(x / 10);
        columnAuthor.setCellValueFactory(new PropertyValueFactory<>("author"));
        TableColumn<Book, User> columnUser = new TableColumn<>("USUARIO");
        columnUser.setPrefWidth(x / 10);
        columnUser.setCellValueFactory(new PropertyValueFactory<>("user"));

        tableViewBook = new TableView<>(observableListBook);
        tableViewBook.getColumns().addAll(columnISBN, columnTitle, columnAuthor, columnUser);
        tableViewBook.setPrefSize(x, 7 * y / 8);
        tableViewBook.setOnMouseClicked(event -> {
            if (tableViewBook.getSelectionModel().getSelectedItem() != null) {
                vBoxApplications.getChildren().remove(0);
                //vBoxApplications.getChildren().add(0, ShowBook.getInstance().getGridPane(
                //  (Book) tableViewBook.getSelectionModel().getSelectedItem()));
            }
        });
        gridPane.add(tableViewBook, 0, 2);
        return gridPane;
    }
}
