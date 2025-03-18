package com.example;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Controller {
    public static FXMLLoader loader = new FXMLLoader();
    private boolean login = true;
    private String user = "matia";
    // private int userid = 1;
    private String page = "home";

    public Stage stage;
    private Scene scene;
    private Parent root;
    private String regex = "([0-9]+)";
    private Pattern p1 = Pattern.compile(regex);

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
    private TextField libraryName;

    @FXML
    private Label labelLoginStatus;

    @FXML
    private ListView<HBox> bookQuery;
    @FXML
    private ListView<HBox> listViewLibrary;
    @FXML
    private ListView<HBox> userViewLibrary;

    @FXML
    private ListView<HBox> bookQueryLib;
    @FXML
    private ListView<HBox> booksToAddLib;

    @FXML
    private TextField query;
    @FXML
    private TextField queryLib;

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
    private Button buttonCreateLibrary;
    @FXML
    private Button createLibrary;

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
    private VBox vboxCreateLibrary;
    @FXML
    private VBox vboxViewLibrary;

    @FXML
    private Button buttonQueryAuthor;
    @FXML
    private Button buttonQueryTitle;
    @FXML
    private Button buttonQueryAuthorLib;
    @FXML
    private Button buttonQueryTitleLib;

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
        while (!bookQuery.getItems().isEmpty()) {
            bookQuery.getItems().removeFirst();
        }
        while (!bookQueryLib.getItems().isEmpty()) {
            bookQueryLib.getItems().removeFirst();
        }
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
        pages.add(vboxCreateLibrary);

        listViewLibrary.setMinHeight(200);
        bookQueryLib.setMinHeight(200);

        // vboxCreateLibrary.setMinHeight(800);

        // home.setPrefWidth(120);

        viewPage(buttonQuery, vboxQuery);
        viewPage(buttonLogin, vboxLogin);
        viewPage(buttonSignup, vboxSignup);
        viewPage(buttonLibrary, vboxLibrary);

        System.out.println("init controller");

        // query by title
        buttonQueryTitle.setOnAction(e -> {
            page = "query";
            try {
                handleQuery("t");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonQueryTitleLib.setOnAction(e -> {
            page = "lib";
            try {
                handleQuery("t");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // query by author
        buttonQueryAuthor.setOnAction(e -> {
            page = "query";
            try {
                handleQuery("a");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        buttonQueryAuthorLib.setOnAction(e -> {
            page = "lib";
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
                    // userid = Client.users.get(userLogin.getText());

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

        // libraries menu button
        buttonLibrary.setOnAction(e -> {
            try {
                var libs = Client.users.get(user).get(0).getLibs();

                while (!listViewLibrary.getItems().isEmpty()) {
                    listViewLibrary.getItems().removeFirst();
                }
                while (!userViewLibrary.getItems().isEmpty()) {
                    userViewLibrary.getItems().removeFirst();
                }

                libs.stream().forEach(l -> {
                    HBox box = new HBox();

                    ArrayList<Book> bs = new ArrayList<>();
                    Button btn = new Button("view lib");
                    btn.setOnAction(viewLib -> {

                        for (Integer bookid : l.getBooks()) {
                            try {
                                bs.add(Client.books.get(bookid));
                            } catch (RemoteException e1) {
                                e1.printStackTrace();
                            }
                        }

                        btn.setUserData(bs);
                        pages.forEach(el -> {
                            el.opacityProperty().set(0);
                        });
                        vboxViewLibrary.opacityProperty().set(1);
                        vboxViewLibrary.toFront();

                        @SuppressWarnings("unchecked")
                        ArrayList<Book> listViewLibraryBooks = (ArrayList<Book>) btn.getUserData();

                        for (Book bo : listViewLibraryBooks) {
                            Button addReview = new Button("add review");
                            Button addSuggestion = new Button("add suggestion");
                            Pane spacer = new Pane();
                            HBox.setHgrow(spacer, Priority.ALWAYS);

                            userViewLibrary.getItems()
                                    .add(new HBox(new Label(bo.toString()), spacer, addReview, addSuggestion));
                        }

                    });
                    Pane spacer = new Pane();
                    HBox.setHgrow(spacer, Priority.ALWAYS);

                    box.getChildren().addAll(new Label(l.getName()), spacer, btn);

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

        // create library button
        buttonCreateLibrary.setOnAction(e -> {

            pages.forEach(el -> {
                el.opacityProperty().set(0);
            });
            vboxCreateLibrary.opacityProperty().set(1);
            vboxCreateLibrary.toFront();
        });

        // do create library and add to db
        createLibrary.setOnAction(e -> {
            try {

                String payloadBookIds = "ARRAY[%s]";
                String bookIds = "";

                for (HBox el : booksToAddLib.getItems()) {
                    String infos = el.getChildren().get(0).toString();
                    var lines = infos.split("\n");

                    bookIds += lines[lines.length - 2].replace("id:\t", "") + ", ";
                }

                bookIds = bookIds.substring(0, bookIds.length() - 2);

                // System.exit(0);

                Client.books.createLibrary(user, libraryName.getText(), String.format(payloadBookIds, bookIds));
                buttonLibrary.fire();
                pages.forEach(el -> {
                    el.opacityProperty().set(0);
                });
                vboxLibrary.opacityProperty().set(1);
                vboxLibrary.toFront();
            } catch (RemoteException ex) {
                ex.printStackTrace();
            }

        });

    }

    @FXML
    private void handleQuery(String mode) throws Exception {
        while (!bookQuery.getItems().isEmpty()) {
            bookQuery.getItems().removeFirst();
        }
        while (!bookQueryLib.getItems().isEmpty()) {
            bookQueryLib.getItems().removeFirst();
        }
        // bookQuery.getItems().removeAll();
        // System.out.println(query.getText());

        String q = "";

        if (page.equals("lib")) {
            q = queryLib.getText();
        }

        if (page.equals("query")) {
            q = query.getText();

        }

        var books = Client.books.get(q, mode);

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

            if (login && page.equals("lib")) {
                Button atl = new Button("add to library");
                atl.setOnAction(e -> {
                    booksToAddLib.getItems().add(box);

                });
                box.getChildren().addAll(atl);
            }

            bookQuery.getItems().add(box);
            bookQueryLib.getItems().add(box);
        });
    }

    private void handleLogin() throws Exception {

    }

    public static FileInputStream getFxml(String name) throws FileNotFoundException {
        return new FileInputStream(Paths.get("resources", name).toString());
    }

}
