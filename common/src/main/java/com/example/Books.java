/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Books interface for rmi allows communication between client and server for book related queries
 */
@SuppressWarnings("CallToPrintStackTrace")
public class Books extends UnicastRemoteObject implements BooksInterface {

    ArrayList<Book> c = new ArrayList<>();
    String dbUrl = "";
    String user = "";
    String pass = "";

    // TODO pass user and pass as arguments
    /**
     * read database libri table
     *
     * @throws RemoteException
     */
    private void readDatabase() throws RemoteException {
        String sql = "SELECT * FROM libri order by id;";
        // var conn = Utils.connect(dbUrl, this.user, this.pass);
        var conn = Utils.connect(dbUrl, user, pass);

        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {
                var t = rs.getString("titolo");
                var a = rs.getString("autore");
                var e = rs.getString("editore");
                var c = rs.getArray("categoria");
                var y = (short) rs.getInt("anno");
                var i = rs.getInt("id");
                // System.out.println(i);
                // System.exit(10);
                Book b = new Book(t, a, e, c.toString(), y, i);
                add(b);
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }

    }

    /**
     * construct books object and read database
     *
     * @param dbUrl
     */
    public Books(String dbUrl, String user, String pass) throws RemoteException {
        this.user = user;
        this.pass = pass;
        this.dbUrl = dbUrl;
        readDatabase();

        // System.out.println("sewyng the srv my g");
    }

    /**
     * add book to list of books
     *
     * @param b
     */
    public void add(Book b) {
        this.c.add(b);
    }

    /**
     * Retrieves a list of books based on a search query and mode.
     *
     * This method searches for books in the collection based on the specified
     * query and mode. The search can be performed by book title or author,
     * depending on the mode provided.
     *
     * @param query The search term used to filter books by title or author.
     * @param mode The mode of the search: "t" for title and "a" for author.
     * @return A list of `Book` objects that match the search criteria, or null
     * if the mode is invalid.
     */
    @Override
    public List<Book> get(String query, String mode) {
        Utils.log("client book query -> " + query);
        if (mode.equals("t")) {
            return Utils.cerca(this.c, book -> book.getTitle().toLowerCase().contains(query.toLowerCase()));
        }
        if (mode.equals("a")) {
            return Utils.cerca(this.c, book -> book.getAuthor().toLowerCase().contains(query.toLowerCase()));
        }
        return null;
    }

    /**
     * Displays a string representation of the collection.
     *
     * @return A string representation of the collection.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public String display() throws RemoteException {
        return this.c.toString();
    }

    /**
     * Creates a new library for a specified user.
     *
     * This method inserts a new entry into the `Librerie` table, associating
     * the library name and the list of books with the user identified by the
     * provided user ID. The method retrieves the user ID from the
     * `utentiregistrati` table based on the provided user ID.
     *
     * @param user The user ID of the person creating the library.
     * @param nome The name of the library to be created.
     * @par m libri A string representing the list of books associated with the
     * library.
     * @return True if the library was successfully created; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public boolean createLibrary(String user, String nome, String libri) throws RemoteException {

        // System.out.println(libri);
        String sql = String.format("INSERT INTO Librerie (nome_libreria, userid, libri) VALUES ('%s', (SELECT id from utentiregistrati where userid = '%s'), %s);", nome, user, libri);
        var conn = Utils.connect(dbUrl, this.user, this.pass);

        try (conn) {
            Utils.queryDB(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        readDatabase();
        return true;
    }

    /**
     * Retrieves a book by its unique identifier.
     *
     * @param id The unique identifier of the book to be retrieved.
     * @return The `Book` object associated with the specified ID, or null if no
     * such book exists.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public Book get(Integer id) throws RemoteException {
        return this.c.get(id);
    }

    /**
     * Creates a review for a specified book by a user.
     *
     * This method inserts a new review into the `valutazionilibri` table,
     * including various ratings and notes provided by the user. The final score
     * is calculated as the average of the individual ratings. The method
     * retrieves the user and book IDs from their respective tables based on the
     * provided user ID and book title.
     *
     * @param user The user ID of the person creating the review.
     * @param book The title of the book being reviewed.
     * @param stile The rating for the style of the book.
     * @param contenuto The rating for the content of the book.
     * @param gradevolezza The rating for the readability of the book.
     * @param originalita The rating for the originality of the book.
     * @param edizione The rating for the edition of the book.
     * @param notes Additional notes or comments about the review.
     * @ret rn True if the review was successfully created; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    public boolean createReview(String user, String book, int stile, int contenuto, int gradevolezza, int originalita,
            int edizione, String notes) throws RemoteException {

        int votofinale = (stile + contenuto + gradevolezza + originalita + edizione) / 5;
        String sql = String.format("insert into valutazionilibri (utente_id, libro_id, stile, contenuto, gradevolezza, originalita, edizione, voto_finale, note) values ((select id from utentiregistrati where userid = '%s'), (select id from libri where titolo = '%s'), %d, %d, %d, %d, %d, %d, '%s' );", user, book, stile, contenuto, gradevolezza, originalita, edizione, votofinale, notes);

        var conn = Utils.connect(dbUrl, this.user, this.pass);

        try (conn) {
            Utils.queryDB(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        readDatabase();
        return true;
    }

    /**
     * Retrieves a list of reviews for a given book title.
     *
     * This met od queries the `valutazionilibri` table and joins it with the
     * `utentiregistrati` table to find all reviews associated with the
     * specified book title. It returns a list of `Review` objects, each
     * containing various ratings and the user ID of the reviewer.
     *
     * @param title The title of the book for which reviews are to be retrieved.
     * @return A list of `Review` objects containing the reviews for the
     * specified book, or null if an error occurs during the database query. 
     */
       
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public List<Review> getReviews(String title) {
        String sql = String.format("select * from valutazionilibri  join utentiregistrati on utentiregistrati.id = valutazionilibri.utente_id where libro_id = (select id from libri where titolo = '%s');", title);

        var conn = Utils.connect(dbUrl, this.user, this.pass);

        List<Review> result = new ArrayList<>();
        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {

                // System.out.println(rs.getString("userid"));

                Review rev = new Review(
                        rs.getInt("stile"),
                        rs.getInt("contenuto"),
                        rs.getInt("gradevolezza"),
                        rs.getInt("originalita"),
                        rs.getInt("edizione"),
                        rs.getString("userid"),
                        rs.getString("note"));

                result.add(rev);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }

        // System.out.println(result.toString());
        return result;

    }

    /**
     * Creates a suggestion for a book based on the user's input.
     *
     * This method inserts a new suggestion into the `consiglilibri` table,
     * which includes the IDs of the suggested books and the user who made the
     * suggestion. The method retrieves the IDs of the books and user from their
     * respective tables based on the provided titles and user ID.
     *
     * @param user The user ID of the person making the suggestion.
     * @param book The title of the book for which suggestions are being made.
     * @param book1 The title of the first suggested book.
     * @par m book2 The title of the second suggested book.
     * @param book3 The title of the third suggested book.
     * @return True if the suggestion was successfully created; false otherwise.
     * @throws RemoteException if a remote communication error occurs.
     */
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean createSuggestion(String user, String book, String book1, String book2, String book3)
            throws RemoteException {

        String sql = String.format(
                "insert into consiglilibri (libro_id, utente_id, libro1_suggerito_id, libro2_suggerito_id, libro3_suggerito_id) "
                + "values ((select id from libri where titolo = '%s'), (select id from utentiregistrati where userid = '%s'),"
                + "(select id from libri where titolo = '%s'),"
                + "(select id from libri where titolo = '%s'),"
                + "(select id from libri where titolo = '%s'));",
                book, user, book1, book2, book3);

        var conn = Utils.connect(dbUrl, this.user, this.pass);

        try (conn) {
            Utils.queryDB(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        // readDatabase();
        return true;
    }

    /**
     * Retrieves a list of suggestions for a given book title.
     *
     * This method queries the `consiglilibri` table to find all suggestions
     * associat d with the specified book title. It returns a list of maps,
     * where each map contains the user ID and the titles of the suggested
     * books.
     *
     * @par m title The title of the book for which suggestions are to be
     * retrieved.
     * @ret rn A list of maps containing user IDs and suggested book titles, or
     * null if an error occurs.
     * @throws emoteException if a remote communication error occurs.
     */
    @Override
    public List<Map<String, Object>> getSuggestions(String title) throws RemoteException {
        String sql = String.format("select * from consiglilibri where libro_id = (select id from libri where titolo = '%s');", title);

        var result = new ArrayList<Map<String, Object>>();

        var conn = Utils.connect(dbUrl, this.user, this.pass);
        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {
                result.add(
                        // finish this shit

                        Map.of(
                                "user", rs.getInt("utente_id"),
                                "book", this.c.get(rs.getInt("libro_id")).getTitle(),
                                "book1", this.c.get(rs.getInt("libro1_suggerito_id")).getTitle(),
                                "book2", this.c.get(rs.getInt("libro2_suggerito_id")).getTitle(),
                                "book3", this.c.get(rs.getInt("libro3_suggerito_id")).getTitle()
                        ));
                // System.out.println(result);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;

    }

}
