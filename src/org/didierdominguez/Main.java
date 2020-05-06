package org.didierdominguez;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.didierdominguez.views.panel.AdministrativePanel;

public class Main extends Application {
    private static Stage stage;
    private static VBox root;
    private static JFXDecorator decorator;

    @Override
    public void start(Stage primaryStage) throws Exception {
        stage = primaryStage;
        root = new VBox();

        decorator = new JFXDecorator(stage, root, false, false, true);
        Scene scene = new Scene(decorator, 480, 270);
        scene.getStylesheets().add("/org/didierdominguez/assets/stylesheets/style.css");
        stage.setTitle("Library");
        stage.setResizable(false);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setMaximized(true);

        root.getChildren().addAll(AdministrativePanel.getInstance().getPane());
        stage.centerOnScreen();
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public static VBox getRoot() {
        return root;
    }
}
