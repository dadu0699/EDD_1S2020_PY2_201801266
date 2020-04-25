package org.didierdominguez;

import com.jfoenix.controls.JFXDecorator;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.didierdominguez.views.login.Login;

public class Main extends Application {
    private static Stage stage;
    private static VBox root;
    private static JFXDecorator decorator;

    @Override
    public void start(Stage primaryStage) throws Exception{
        stage = primaryStage;
        root = new VBox();

        decorator = new JFXDecorator(stage, root, false, false, true);
        Scene scene = new Scene(decorator, 480, 270);
        scene.getStylesheets().add("/org/didierdominguez/assets/stylesheets/style.css");
        stage.setTitle("Library");
        stage.setResizable(false);
        stage.setScene(scene);

        root.getChildren().addAll(Login.getInstance().getLogin());
        stage.centerOnScreen();
        stage.show();
    }


    public static void main(String[] args) {
        // ControllerEmployee.getInstance().createEmployee("ADMINISTRADOR", "ADMINISTRADOR", "ADMIN", "admin", true);
        launch(args);
    }

    public static Stage getStage() {
        return stage;
    }

    public static VBox getRoot() {
        return root;
    }
}