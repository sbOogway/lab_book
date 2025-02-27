package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
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
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    public static FXMLLoader loader = new FXMLLoader();
    private boolean login = false;
    private String user = "";

    public Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    private PasswordField passwordLogin;
    @FXML
    private TextField userLogin;

    @FXML
    private TextField usernameSignup;
    @FXML
    private TextField nomeSignup;
    @FXML
    private TextField cognomeSignup;
    @FXML
    private TextField codiceFiscaleSignup;
    @FXML
    private TextField emailSignup;
    @FXML
    private TextField passwordSignup;

    @FXML
    private Label labelLoginStatus;

    @FXML
    private ListView<HBox> bookQuery;
    @FXML
    private ListView<HBox> listViewLibrary;

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
    private Button buttonLibrary;

    @FXML
    private VBox vboxQuery;
    @FXML
    private VBox vboxLogin;
    @FXML
    private VBox vboxSignup;
    @FXML
    private VBox vboxBook;
    @FXML
    private VBox vboxLibrary;

    @FXML
    private Button buttonQueryAuthor;
    @FXML
    private Button buttonQueryTitle;

    @FXML
    private Label labelBookTitle;
    @FXML
    private Label labelBookAuthor;
    @FXML
    private Label labelBookCategory;
    @FXML
    private Label labelBookYear;
    @FXML
    private Label labelBookPublisher;

    @FXML
    private Button submitLogin;
    @FXML
    private Button submitSignup;

    private List<VBox> pages = new ArrayList<VBox>();

    private void viewPage(Button btn, VBox page) {
        btn.setOnAction(event -> {
            pages.forEach(e -> {
                e.opacityProperty().set(0);
            });
            page.opacityProperty().set(1);
            page.toFront();
        });
    }

    @FXML
    public void initialize() {
        pages.add(vboxQuery);
        pages.add(vboxLogin);
        pages.add(vboxSignup);
        pages.add(vboxBook);
        pages.add(vboxLibrary);

        // home.setPrefWidth(120);

        viewPage(buttonQuery, vboxQuery);
        viewPage(buttonLogin, vboxLogin);
        viewPage(buttonSignup, vboxSignup);
        viewPage(buttonLibrary, vboxLibrary);

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

        // login
        submitLogin.setOnAction(e -> {
            try {
                if (Client.users.login(userLogin.getText(), passwordLogin.getText())) {
                    System.out.println("succesfull login");
                    labelLoginStatus.setText(userLogin.getText());
                    pages.forEach(el -> {
                        el.opacityProperty().set(0);
                    });
                    vboxQuery.opacityProperty().set(1);
                    vboxQuery.toFront();
                    login = true;
                    user = userLogin.getText();

                } else {
                    System.out.println("failed login");
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // signup
        submitSignup.setOnAction(e -> {
            try {
                Client.users.add(
                        usernameSignup.getText(),
                        passwordSignup.getText(),
                        codiceFiscaleSignup.getText(),
                        emailSignup.getText(),
                        nomeSignup.getText(),
                        cognomeSignup.getText());

                pages.forEach(el -> {
                    el.opacityProperty().set(0);
                });
                vboxLogin.opacityProperty().set(1);
                vboxLogin.toFront();
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonLibrary.setOnAction(e -> {
            try {
                var libs = Client.users.get(user).get(0).getLibs();

                libs.stream().forEach(l -> {
                    HBox box = new HBox();

                    Button btn = new Button("view lib");

                    box.getChildren().addAll(new Label(l.getName()), btn);

                    listViewLibrary.getItems().add(box);

                });

                pages.forEach(el -> {
                    el.opacityProperty().set(0);
                });
                vboxLibrary.opacityProperty().set(1);
                vboxLibrary.toFront();

            } catch (Exception ex) {
                ex.printStackTrace();// TODO: handle exception
            }
        });

    }

    @FXML
    private void handleQuery(String mode) throws Exception {
        while (!bookQuery.getItems().isEmpty()) {
            bookQuery.getItems().removeFirst();
        }
        // bookQuery.getItems().removeAll();
        // System.out.println(query.getText());
        var books = Client.books.get(query.getText(), mode);

        // VBox q = (VBox) loader.load(getFxml("query.fxml"));
        // bookQuery.getParent().getChildrenUnmodifiable().add(q);

        books.stream().forEach(b -> {
            HBox box = new HBox();

            Button btn = new Button("view book");

            btn.setOnAction(evt -> {
                pages.forEach(e -> {
                    e.opacityProperty().set(0);
                });
                vboxBook.opacityProperty().set(1);
                vboxBook.toFront();
                labelBookTitle.setText(b.getTitle());
                labelBookAuthor.setText(b.getAuthor());
                labelBookCategory.setText(b.getCategory());
                labelBookPublisher.setText(b.getPublisher());
                labelBookYear.setText(Short.toString(b.getYear()));

            });

            box.getChildren().addAll(new Label(b.toString()), btn);

            if (login) {
                Button atl = new Button("add to library");
                atl.setOnAction(e -> {

                });
                box.getChildren().addAll(atl);
            }

            bookQuery.getItems().add(box);
        });
    }

    private void handleLogin() throws Exception {

    }

    public static FileInputStream getFxml(String name) throws FileNotFoundException {
        return new FileInputStream(Paths.get("resources", name).toString());
    }

}
