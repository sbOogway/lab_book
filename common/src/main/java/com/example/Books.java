package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CallToPrintStackTrace")
public class Books extends UnicastRemoteObject implements BooksInterface {

    ArrayList<Book> c = new ArrayList<>();
    String dbUrl = "";

    private void readDatabase() throws RemoteException {
        String sql = "SELECT * FROM libri;";
        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

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

    public Books(String dbUrl) throws RemoteException {
        this.dbUrl = dbUrl;
        readDatabase();

        System.out.println("sewyng the srv my g");

    }

    public void add(Book b) {
        this.c.add(b);
    }

    @Override
    public List<Book> get(String query, String mode) {
        Utils.log("client book query -> " + query);
        if (mode.equals("t")) {
            return Utils.cerca(this.c, book -> book.getTitle().contains(query));
        }
        if (mode.equals("a")) {
            return Utils.cerca(this.c, book -> book.getAuthor().contains(query));
        }
        return null;
    }

    @Override
    public String display() throws RemoteException {
        return this.c.toString();
    }

    @Override
    public boolean createLibrary(String user, String nome, String libri) throws RemoteException {

        System.out.println(libri);
        String sql = String.format("INSERT INTO Librerie (nome_libreria, userid, libri) VALUES ('%s', (SELECT id from utentiregistrati where userid = '%s'), %s);", nome, user, libri);
        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

        try (conn) {
            Utils.queryDB(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        readDatabase();
        return true;
    }

    @Override
    public Book get(Integer id) throws RemoteException {
        return this.c.get(id);
    }

    @Override
    public boolean createReview(String user, String book, int stile, int contenuto, int gradevolezza, int originalita,
            int edizione, String notes) throws RemoteException {

        int votofinale = (stile + contenuto + gradevolezza + originalita + edizione) / 5;
        String sql = String.format("insert into valutazionilibri (utente_id, libro_id, stile, contenuto, gradevolezza, originalita, edizione, voto_finale, note) values ((select id from utentiregistrati where userid = '%s'), (select id from libri where titolo = '%s'), %d, %d, %d, %d, %d, %d, '%s' );", user, book, stile, contenuto, gradevolezza, originalita, edizione, votofinale, notes);

        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

        try (conn) {
            Utils.queryDB(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        readDatabase();
        return true;
    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public List<Review> getReviews(String title) {
        String sql = String.format("select * from valutazionilibri  join utentiregistrati on utentiregistrati.id = valutazionilibri.utente_id where libro_id = (select id from libri where titolo = '%s');", title);

        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

        List<Review> result = new ArrayList<>();
        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {

                System.out.println(rs.getString("userid"));

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

        System.out.println(result.toString());

        return result;

    }

    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean createSuggestion(String user, String book, String book1, String book2, String book3)
            throws RemoteException {

        String sql = String.format(
            "insert into consiglilibri (libro_id, utente_id, libro1_suggerito_id, libro2_suggerito_id, libro3_suggerito_id) " +
            "values ((select id from libri where titolo = '%s'), (select id from utentiregistrati where userid = '%s')," + 
            "(select id from libri where titolo = '%s')," + 
            "(select id from libri where titolo = '%s')," +
            "(select id from libri where titolo = '%s'));"
            
            , book, user, book1, book2, book3);

        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

        try (conn) {
            Utils.queryDB(conn, sql);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        // readDatabase();
        return true;
    }

    @Override
    public List<Map<String, Object>> getSuggestions(String title) throws RemoteException {
        String sql = String.format("select * from consiglilibri where libro_id = (select id from libri where titolo = '%s');", title);

        var result = new ArrayList<Map<String, Object>>();

        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));
        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {
                result.add(
                    // finish this shit
                    
                    Map.of(
                        "user", rs.getInt("utente_id"),
                        "book",  this.c.get(rs.getInt("libro_id")-1).getTitle(),
                        "book1", this.c.get(rs.getInt("libro1_suggerito_id")-1).getTitle(),
                        "book2", this.c.get(rs.getInt("libro2_suggerito_id")-1).getTitle(),
                        "book3", this.c.get(rs.getInt("libro3_suggerito_id")-1).getTitle()
                        ));
                System.out.println(result);

            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
        return result;

    }


}
