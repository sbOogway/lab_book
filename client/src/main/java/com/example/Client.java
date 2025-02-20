package com.example;

import java.io.File;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Client extends Application {
    public static BooksInterface books;
    public static UsersInterface users;
    // public static VBox root;

    @Override
    public void start(Stage stage) throws Exception {

        Registry registry = LocateRegistry.getRegistry("localhost", 1099);
        books = (BooksInterface) registry.lookup("books");
        users = (UsersInterface) registry.lookup("users");

        File css = new File(Paths.get("resources", "style.css").toString());
        System.out.println(css.getAbsolutePath());
            
        // Parent root = FXMLLoader.load(getClass().getResource("file:///home/oogway/uni/lab_b/resources/home.fxml")); 

        // System.out.println(cssFile);
        
        // Controller.loader.setRoot(Controller.getFxml("query.fxml"));
        StackPane homeRoot = (StackPane) Controller.loader.load(Controller.getFxml("BookRecommender.fxml"));
        // VBox QueryRroot = (VBox) Controller.loader.load(Controller.getFxml("query.fxml"));
        
        
        Scene scene = new Scene(homeRoot, 800, 600);
        scene.getStylesheets().add("file://" + css.getAbsolutePath().replace("\\", "/"));
        stage.setTitle("Book Recommender");
        stage.setScene(scene);
        stage.show();

        // Controller c = new Controller();
        

        // Thread.sleep(1000);

        // root = Controller.loader.load(Controller.getFxml("query.fxml"));
        // scene = new Scene(root, 800, 600);
        // scene.getStylesheets().add("file://" + css.getAbsolutePath().replace("\\", "/"));
        // primaryStage.setTitle("Book Recommender");
        // primaryStage.setScene(scene);
        // primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
