/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.awt.Color;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
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

/**
 * The Controller class serves as the main controller for the application's user
 * interface. It manages the interaction between the UI components and the
 * underlying logic, handling user input, navigation, and various actions within
 * the application.
 *
 * This class utilizes JavaFX annotations to bind UI elements and provides
 * methods for managing pages, user authentication, and other functionalities.
 */
public class Controller {

    // public static FXMLLoader loader = new FXMLLoader();
    private boolean login = true;
    private String user = "matia";
    // private int userid = 1;
    private String page = "home";

    public Stage stage;

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
    private Label labelBookTitleReview;
    @FXML
    private Label labelBookTitleSuggestion;
    // @FXML
    // private Label labelBookIdReview;

    @FXML
    private Label stileMedia;
    @FXML
    private Label contenutoMedia;
    @FXML
    private Label gradevolezzaMedia;
    @FXML
    private Label originalitaMedia;
    @FXML
    private Label edizioneMedia;
    @FXML
    private Label votoFinaleMedia;
    @FXML
    private Label numeroRecensioni;
    @FXML
    private Label numeroSuggerimenti;

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
    private ListView<HBox> bookQuerySugg;
    @FXML
    private ListView<HBox> booksToAddSugg;

    @FXML
    private ListView<VBox> reviewsList;
    @FXML
    private ListView<VBox> suggestionsList;

    @FXML
    private TextField query;
    @FXML
    private TextField queryLib;
    @FXML
    private TextField querySugg;
    @FXML
    private TextField edizione;
    @FXML
    private TextField originalita;
    @FXML
    private TextField gradevolezza;
    @FXML
    private TextField contenuto;
    @FXML
    private TextField stile;
    @FXML
    private TextField note;

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
    private Button buttonAddReview;
    @FXML
    private Button buttonAddSuggestion;

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
    private VBox vboxReview;
    @FXML
    private VBox vboxSuggestion;

    @FXML
    private Button buttonQueryAuthor;
    @FXML
    private Button buttonQueryTitle;
    @FXML
    private Button buttonQueryAuthorLib;
    @FXML
    private Button buttonQueryAuthorSugg;
    @FXML
    private Button buttonQueryTitleLib;
    @FXML
    private Button buttonQueryTitleSugg;

    @FXML 
    private HBox loginContainer;
    @FXML 
    private HBox signupContainer;
    @FXML
    private Button yearQueryButton;
    @FXML
    private TextField yearQuery;

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

    private final List<VBox> pages = new ArrayList<>();

    /**
     * Handles the visibility and navigation of pages within the application.
     * This method clears the items from `bookQuery` and `bookQueryLib` lists,
     * and sets up an action for the provided button to bring the specified page
     * to the front and make it visible while hiding all other pages.
     *
     * @param btn The button that triggers the page view action.
     * @param page The VBox representing the page to be displayed.
     */
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

    /**
     * Initializes the controller by setting up the pages for the application.
     * This method adds various VBox elements representing different pages
     * (e.g., query, login, signup, book, library, etc.) to the `pages` list.
     * These pages are used for navigation and visibility management within the
     * application.
     */
    @FXML
    @SuppressWarnings({"UseSpecificCatch", "CallToPrintStackTrace"})
    public void initialize() {

        pages.add(vboxQuery);
        pages.add(vboxLogin);
        pages.add(vboxSignup);
        pages.add(vboxBook);
        pages.add(vboxLibrary);
        pages.add(vboxCreateLibrary);
        pages.add(vboxViewLibrary);
        pages.add(vboxReview);
        pages.add(vboxSuggestion);

        listViewLibrary.setMinHeight(200);
        bookQueryLib.setMinHeight(200);

        // vboxCreateLibrary.setMinHeight(800);
        // home.setPrefWidth(120);
        viewPage(buttonQuery, vboxQuery);
        viewPage(buttonLogin, vboxLogin);
        viewPage(buttonSignup, vboxSignup);
        viewPage(buttonLibrary, vboxLibrary);

        // System.out.println("init controller");

        // query author year
        yearQueryButton.setOnAction(e -> {
            page = "query";
            try {
                handleQuery("y");
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

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

        buttonQueryTitleSugg.setOnAction(e -> {
            page = "sugg";
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

        buttonQueryAuthorSugg.setOnAction(e -> {
            page = "sugg";
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
                    Label error = new Label("wrong password or username");
                    error.setStyle("-fx-text-fill: red;");
                    loginContainer.getChildren().add(error);
                }

            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });

        // signup
        submitSignup.setOnAction(e -> {
            if (!Utils.isValidEmail(emailSignup.getText())) {
                Label error = new Label("wrong email");
                error.setStyle("-fx-text-fill: red;");
                signupContainer.getChildren().add(error);
                return;
            } 

            
            

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
                            addReview.setOnAction(addRvw -> {
                                labelBookTitleReview.setText(bo.getTitle());
                                // labelBookIdReview.setText(bo.get);
                                pages.forEach(el -> {
                                    el.opacityProperty().set(0);
                                });
                                vboxReview.opacityProperty().set(1);
                                vboxReview.toFront();

                            });
                            Button addSuggestion = new Button("add suggestion");
                            addSuggestion.setOnAction(addSugg -> {
                                labelBookTitleSuggestion.setText(bo.getTitle());
                                pages.forEach(el -> {
                                    el.opacityProperty().set(0);
                                });
                                vboxSuggestion.opacityProperty().set(1);
                                vboxSuggestion.toFront();

                            });

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

        // add review for a book
        buttonAddReview.setOnAction(action -> {
            try {
                // int bookid = Integer.parseInt(labelBookIdReview.getText());
                String book = labelBookTitleReview.getText();
                int stileVoto = Integer.parseInt(stile.getText());
                int contenutoVoto = Integer.parseInt(contenuto.getText());
                int gradevolezzaVoto = Integer.parseInt(gradevolezza.getText());
                int originalitaVoto = Integer.parseInt(originalita.getText());
                int edizioneVoto = Integer.parseInt(edizione.getText());
                String notes = note.getText();

                var success = Client.books.createReview(user, book, stileVoto, contenutoVoto, gradevolezzaVoto, originalitaVoto, edizioneVoto, notes);
                System.out.println(success);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        // add suggestions for a book
        buttonAddSuggestion.setOnAction(action -> {
            String book = labelBookTitleSuggestion.getText();
            String book1, book2, book3;

            try {
                book1 = booksToAddSugg.getItems().get(0).getChildren().get(0).toString().split("title:")[1].split("\n")[0].strip();
            } catch (Exception e) {
                e.printStackTrace();
                book1 = null;
            }

            try {
                book2 = booksToAddSugg.getItems().get(1).getChildren().get(0).toString().split("title:")[1].split("\n")[0].strip();
            } catch (Exception e) {
                e.printStackTrace();
                book2 = null;
            }

            try {
                book3 = booksToAddSugg.getItems().get(2).getChildren().get(0).toString().split("title:")[1].split("\n")[0].strip();
            } catch (Exception e) {
                e.printStackTrace();
                book3 = null;
            }
            System.out.println(book1);

            try {
                Client.books.createSuggestion(user, book, book1, book2, book3);
            } catch (RemoteException ex) {
            }
        });
    }

    /**
     * Handles the query operation based on the provided mode. Clears the items
     * from the query lists and retrieves the query text based on the current
     * page.
     *
     * @param mode The mode of the query operation (e.g., "lib").
     * @throws Exception If an error occurs during the query handling process.
     */
    @FXML
    @SuppressWarnings({"CallToPrintStackTrace", "UseSpecificCatch"})
    private void handleQuery(String mode) throws Exception {
        while (!bookQuery.getItems().isEmpty()) {
            bookQuery.getItems().removeFirst();
        }
        while (!bookQueryLib.getItems().isEmpty()) {
            bookQueryLib.getItems().removeFirst();
        }
        while (!bookQuerySugg.getItems().isEmpty()) {
            bookQuerySugg.getItems().removeFirst();
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

        if (page.equals("sugg")) {
            q = querySugg.getText();
        }

        List<Book> books;

        if (mode.equals("y")) {
            int year = Integer.parseInt(yearQuery.getText());
            books = Client.books.get(q, "a").stream().filter(e -> e.getYear() == year).collect(Collectors.toList());
        } else {
            books = Client.books.get(q, mode);
        }

        // System.out.println(books);
        // VBox q = (VBox) loader.load(getFxml("query.fxml"));
        // bookQuery.getParent().getChildrenUnmodifiable().add(q);
        books.stream().forEach(b -> {
            if (b.getTitle().equals("null")) {
                return;
            }

            HBox box = new HBox();

            Button btn = new Button("view book");

            btn.setOnAction(evt -> {
                while (!reviewsList.getItems().isEmpty()) {
                    reviewsList.getItems().removeFirst();
                }
                while (!suggestionsList.getItems().isEmpty()) {
                    suggestionsList.getItems().removeFirst();
                }
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

                // need to add reviews and suggestions summary here
                var reviews = new ArrayList<Review>();
                try {
                    reviews = (ArrayList<Review>) Client.books.getReviews(b.getTitle());

                } catch (RemoteException ex) {
                    ex.printStackTrace();
                }

                for (Review rev : reviews) {
                    VBox reviewDisplay = new VBox();
                    reviewDisplay.getChildren().addAll(
                            new Label("user:\t\t\t" + rev.owner),
                            new Label("stile:\t\t\t" + rev.stile),
                            new Label("contenuto:\t" + rev.contenuto),
                            new Label("gradevolezza:\t" + rev.gradevolezza),
                            new Label("originalita:\t" + rev.originalita),
                            new Label("edizione:\t\t" + rev.edizione),
                            new Label("voto finale:\t" + rev.votofinale),
                            new Label("note:\t\t" + rev.notes)
                    );
                    reviewsList.getItems().add(reviewDisplay);
                }

                // System.out.println(reviews);
                var suggestions = new ArrayList<Map<String, Object>>();
                try {
                    suggestions = (ArrayList<Map<String, Object>>) Client.books.getSuggestions(b.getTitle());
                } catch (Exception e) {
                    e.printStackTrace();
                }

                for (Map<String, Object> sugg : suggestions) {
                    VBox suggDisplay = new VBox();
                    try {
                        suggDisplay.getChildren().addAll(
                                new Label("user:\t\t\t" + Client.users.get((int) sugg.get("user") - 1).getUserid()),
                                new Label("book1:\t\t" + sugg.get("book1")),
                                new Label("book2:\t\t" + sugg.get("book2")),
                                new Label("book3:\t\t" + sugg.get("book3"))
                        );
                        suggestionsList.getItems().add(suggDisplay);
                    } catch (RemoteException ex) {
                        ex.printStackTrace();
                    }
                }

                stileMedia.setText(String.format("media voto stile:\t\t\t%.2f", reviews.stream().map(e -> e.stile).mapToInt(Integer::intValue).summaryStatistics().getAverage()));
                contenutoMedia.setText(String.format("media voto contenuto:\t\t%.2f", reviews.stream().map(e -> e.contenuto).mapToInt(Integer::intValue).summaryStatistics().getAverage()));
                gradevolezzaMedia.setText(String.format("media voto gradevolezza:\t%.2f", reviews.stream().map(e -> e.gradevolezza).mapToInt(Integer::intValue).summaryStatistics().getAverage()));
                originalitaMedia.setText(String.format("media voto originalita:\t\t%.2f", reviews.stream().map(e -> e.originalita).mapToInt(Integer::intValue).summaryStatistics().getAverage()));
                edizioneMedia.setText(String.format("media voto edizione:\t%.2f", reviews.stream().map(e -> e.edizione).mapToInt(Integer::intValue).summaryStatistics().getAverage()));
                votoFinaleMedia.setText(String.format("media voto finale:\t\t%.2f", reviews.stream().map(e -> e.votofinale).mapToInt(Integer::intValue).summaryStatistics().getAverage()));
                numeroRecensioni.setText("numero recensioni:\t\t" + reviews.size());
                numeroSuggerimenti.setText("numero suggerimenti:\t" + suggestions.size());
                // System.out.println(suggestions);
            });

            box.getChildren().addAll(new Label(b.toString()), btn);

            if (login && page.equals("lib")) {
                Button atl = new Button("add to library");
                atl.setOnAction(e -> {
                    booksToAddLib.getItems().add(box);

                });
                box.getChildren().addAll(atl);
                bookQueryLib.getItems().add(box);
                return;
            }

            if (login && page.equals("sugg")) {
                Button ats = new Button("add to suggestions");
                ats.setOnAction(e -> {
                    booksToAddSugg.getItems().add(box);

                });
                box.getChildren().addAll(ats);
                bookQuerySugg.getItems().add(box);
                return;
            }

            bookQuery.getItems().add(box);

        });
    }

    /**
     * Retrieves a FileInputStream for the specified FXML file. This method
     * constructs the file path using the "resources" directory and the provided
     * file name, then opens a FileInputStream for it.
     *
     * @param name The name of the FXML file to be loaded.
     * @return A FileInputStream for the specified FXML file.
     * @throws FileNotFoundException If the specified file is not found.
     */
    // public static FileInputStream getFxml(String name) throws FileNotFoundException {
    //     return new FileInputStream(Paths.get("resources", name).toString());
    // }

}
