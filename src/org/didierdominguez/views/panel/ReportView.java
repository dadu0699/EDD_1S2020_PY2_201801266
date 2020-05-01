package org.didierdominguez.views.panel;

import javafx.collections.ObservableList;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;

public class ReportView {
    private static ReportView instance;
    private HBox hBox;
    private GridPane gridPane;
    private TableView tableView;
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
}
