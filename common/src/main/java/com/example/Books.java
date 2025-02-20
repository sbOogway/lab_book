package com.example;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Books extends UnicastRemoteObject implements BooksInterface {
    ArrayList<Book> c = new ArrayList<>();

    public Books(String dbUrl) throws RemoteException {
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

        System.out.println("sewyng the srv my g");

    }

    public void add(Book b) {
        this.c.add(b);
    }

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
        // TODO Auto-generated method stub
        return this.c.toString();
        // throw new UnsupportedOperationException("Unimplemented method 'display'");
    }

}
