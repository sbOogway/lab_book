/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.io.File;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

public class Client extends Application {

    public static BooksInterface books;
    public static UsersInterface users;

    /**
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        books = (BooksInterface) registry.lookup("books");
        users = (UsersInterface) registry.lookup("users");

        File css = new File(Paths.get("resources", "style.css").toString());
        System.out.println(css.getAbsolutePath());

        StackPane homeRoot = (StackPane) Controller.loader.load(Controller.getFxml("v2.fxml"));

        Scene scene = new Scene(homeRoot, 1920, 1080);
        scene.getStylesheets().add("file://" + css.getAbsolutePath().replace("\\", "/"));
        stage.setTitle("Book Recommender");
        stage.setScene(scene);
        stage.show();
    }

    /**
     *
     * @param args
     */
    public static void main(String[] args) {
        launch(args);
    }
}
