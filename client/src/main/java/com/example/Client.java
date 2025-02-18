package com.example;


import java.io.File;
import java.io.FileInputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Client extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader();
        FileInputStream fxml = new FileInputStream(Paths.get("resources", "home.fxml").toString());
        // String cssFile = new String(Files.readAllBytes(Paths.get("resources", "style.css")));

        File css = new File(Paths.get("resources", "style.css").toString());
        System.out.println(css.getAbsolutePath());
        // System.out.println(cssFile);
        GridPane root = (GridPane) loader.load(fxml);
        Scene scene = new Scene(root, 800, 600);
        scene.getStylesheets().add("file://" + css.getAbsolutePath().replace("\\", "/"));
        primaryStage.setTitle("Book Recommender");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
