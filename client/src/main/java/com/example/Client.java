package com.example;


import java.io.FileInputStream;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fxml = new FileInputStream(Paths.get("resources", "home.fxml").toString());
        VBox root = (VBox) loader.load(fxml);
        primaryStage.setTitle("Book Recommender");
        primaryStage.setScene(new Scene(root, 1920, 1080));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
