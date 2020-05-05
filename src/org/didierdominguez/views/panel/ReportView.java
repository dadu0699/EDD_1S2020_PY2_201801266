package org.didierdominguez.views.panel;

import com.jfoenix.controls.JFXButton;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.TableView;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import org.didierdominguez.beans.User;
import org.didierdominguez.controllers.CategoryController;
import org.didierdominguez.controllers.UserController;
import org.didierdominguez.util.ScreenSize;

public class ReportView {
    private static ReportView instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
    private ImageView imageView;
    private ObservableList observableList;

    private ReportView() {
    }

    public static ReportView getInstance() {
        if (instance == null) {
            instance = new ReportView();
        }
        return instance;
    }

    public void restartHBox() {
        hBox.getChildren().clear();
        hBox.getChildren().add(gridPane);
    }


    public HBox getReportView() {
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

        Text textTitle = new Text("REPORTES");
        textTitle.getStyleClass().add("textTitle");
        textTitle.setFont(new Font(25));
        gridPane.add(textTitle, 0, 0);

        HBox hBoxButtons = new HBox();
        JFXButton btnAVL = new JFXButton("ARBOL AVL");
        btnAVL.getStyleClass().addAll("customButton", "primaryButton");
        btnAVL.setButtonType(JFXButton.ButtonType.FLAT);
        btnAVL.setPrefSize(x, y);
        btnAVL.setOnAction(event -> {
            System.out.println(CategoryController.getInstance().graphTreeAVL());
        });

        JFXButton btnB = new JFXButton("ARBOL B");
        btnB.getStyleClass().addAll("customButton", "primaryButton");
        btnB.setButtonType(JFXButton.ButtonType.FLAT);
        btnB.setPrefSize(x, y);
        btnB.setOnAction(event -> {
        });

        JFXButton btnTH = new JFXButton("TABLA DISPERSION");
        btnTH.getStyleClass().addAll("customButton", "warningButton");
        btnTH.setButtonType(JFXButton.ButtonType.FLAT);
        btnTH.setPrefSize(x, y);
        btnTH.setOnAction(event -> {
            System.out.println(UserController.getInstance().getGraph());
        });

        JFXButton btnList = new JFXButton("LISTA");
        btnList.getStyleClass().addAll("customButton", "dangerButton");
        btnList.setButtonType(JFXButton.ButtonType.FLAT);
        btnList.setPrefSize(x, y);
        btnList.setOnAction(event -> {
        });

        JFXButton btnBlock = new JFXButton("BLOCKCHAIN");
        btnBlock.getStyleClass().addAll("customButton", "dangerButton");
        btnBlock.setButtonType(JFXButton.ButtonType.FLAT);
        btnBlock.setPrefSize(x, y);
        btnBlock.setOnAction(event -> {
        });


        hBoxButtons.getChildren().addAll(btnAVL, btnB, btnTH, btnList, btnBlock);
        hBoxButtons.setPrefSize(x, y / 10);
        hBoxButtons.setMargin(btnAVL, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(btnB, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(btnTH, new Insets(0, 5, 0, 0));
        hBoxButtons.setMargin(btnList, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons, 0, 1,1,1);

        HBox hBoxButtons2 = new HBox();
        JFXButton btnPreorder = new JFXButton("PREORDEN");
        btnPreorder.getStyleClass().addAll("customButton", "dangerButton");
        btnPreorder.setButtonType(JFXButton.ButtonType.FLAT);
        btnPreorder.setPrefSize(x, y);
        btnPreorder.setOnAction(event -> {
            System.out.println(CategoryController.getInstance().graphPreOrder());
        });

        JFXButton btnInorder = new JFXButton("INORDEN");
        btnInorder.getStyleClass().addAll("customButton", "dangerButton");
        btnInorder.setButtonType(JFXButton.ButtonType.FLAT);
        btnInorder.setPrefSize(x, y);
        btnInorder.setOnAction(event -> {
            System.out.println(CategoryController.getInstance().graphInOrder());
        });

        JFXButton btnPostOrder = new JFXButton("POSTORDEN");
        btnPostOrder.getStyleClass().addAll("customButton", "dangerButton");
        btnPostOrder.setButtonType(JFXButton.ButtonType.FLAT);
        btnPostOrder.setPrefSize(x, y);
        btnPostOrder.setOnAction(event -> {
            System.out.println(CategoryController.getInstance().graphPostOrder());
        });

        hBoxButtons2.getChildren().addAll(btnPreorder, btnInorder, btnPostOrder);
        hBoxButtons2.setPrefSize(x, y / 10);
        hBoxButtons2.setMargin(btnPreorder, new Insets(0, 5, 0, 0));
        hBoxButtons2.setMargin(btnInorder, new Insets(0, 5, 0, 0));
        gridPane.add(hBoxButtons2, 0, 2,1,1);

        imageView = new ImageView();
        imageView.setFitWidth(x/2);
        imageView.setPreserveRatio(true);
        gridPane.add(imageView, 0, 3);
        gridPane.setMargin(imageView, new Insets(-10, -5, -25, 0));

        hBox.getChildren().add(gridPane);

        return hBox;
    }
}
