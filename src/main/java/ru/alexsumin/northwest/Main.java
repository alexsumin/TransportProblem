package ru.alexsumin.northwest;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {

    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/view/Controller.fxml"));
        primaryStage.setTitle("North-West");
        primaryStage.setScene(new Scene(root, 800, 500));
        primaryStage.show();


    }
}
