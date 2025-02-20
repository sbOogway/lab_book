package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
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
    private ListView<HBox> bookQuery;

    @FXML
    private TextField query;

    @FXML
    private Button buttonHome;
    @FXML
    private Button buttonLogin;
    @FXML
    private Button buttonSignup;
    @FXML
    private Button buttonQuery;

    @FXML
    private VBox vboxQuery;
    @FXML
    private VBox vboxLogin;
    @FXML
    private VBox vboxSignup;
    @FXML
    private VBox vboxBook;

    @FXML
    private Button buttonQueryAuthor;
    @FXML
    private Button buttonQueryTitle;

    private List<VBox> pages = new ArrayList<VBox>();

    private void viewPage(Button btn, VBox page) {
        btn.setOnAction(event -> {
            pages.forEach(e -> {
                System.out.println(e);
                e.opacityProperty().set(0);
            });
            page.opacityProperty().set(1);
        });
    }

    @FXML
    public void initialize() {
        pages.add(vboxQuery);
        pages.add(vboxLogin);
        pages.add(vboxSignup);
        pages.add(vboxBook);

        // home.setPrefWidth(120);

        viewPage(buttonQuery, vboxQuery);
        viewPage(buttonLogin, vboxLogin);
        viewPage(buttonSignup, vboxSignup);

        System.out.println("init controller");
        
        buttonQueryTitle.setOnAction(e -> {
            try {
                handleQuery("t");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonQueryAuthor.setOnAction(e -> {
            try {
                handleQuery("a");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

    }

    @FXML
    private void handleQuery(String mode) throws Exception {
        // bookQuery.getItems().removeAll();
        // System.out.println(query.getText());
        var books = Client.books.get(query.getText(), mode);

        // VBox q = (VBox) loader.load(getFxml("query.fxml"));
        // bookQuery.getParent().getChildrenUnmodifiable().add(q);

        books.stream().forEach(b -> {
            HBox box = new HBox();
            box.getChildren().addAll(new Label(b.toString()), new Button());

            bookQuery.getItems().add(box);
        });
    }

    public static FileInputStream getFxml(String name) throws FileNotFoundException {
        return new FileInputStream(Paths.get("resources", name).toString());
    }

}
