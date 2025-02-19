package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    public static FXMLLoader loader = new FXMLLoader();

    public Stage stage;
    private Scene scene;
    private Parent root;

    public Controller() {
    }

    @FXML
    private ListView<Book> bookQuery;

    @FXML
    private TextField query;

    @FXML
    private VBox home;

    @FXML
    public void initialize() {
        home.setPrefWidth(120);
    }

    @FXML
    private void handleTextArea(ActionEvent event) throws Exception {
        // System.out.println(query.getText());
        var books = Client.books.get(query.getText());

        // VBox q = (VBox) loader.load(getFxml("query.fxml"));
        // bookQuery.getParent().getChildrenUnmodifiable().add(q);
        books.stream().forEach(b -> bookQuery.getItems().add(b));
    }

  

    public static FileInputStream getFxml(String name) throws FileNotFoundException {
        return new FileInputStream(Paths.get("resources", name).toString());
    }

}
