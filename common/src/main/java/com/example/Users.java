package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Users extends UnicastRemoteObject implements UsersInterface {
    ArrayList<User> c = new ArrayList<>();
    String dbUrl;

    private void readDatabase() {
        String sql = "SELECT * FROM utentiregistrati;";
        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {
                var userid = rs.getString("userid");
                var password = rs.getString("password");

                User b = new User("", "", "", "", userid, password);
                add(b);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public Users(String dbUrl) throws RemoteException {
        this.dbUrl = dbUrl;
        readDatabase();

    }

    public void add(User b) {
        this.c.add(b);
    }

    public List<User> get(String userName) {
        Utils.log("client user query -> " + userName);
        return Utils.cerca(this.c, u -> u.getUserid().equals(userName));
    }

    @Override
    public boolean login(String userName, String password) throws RemoteException {
        try {
            return get(userName).get(0).getPassword().equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean add(String userName, String password, String codiceFiscale, String email, String nome,
            String cognome) throws RemoteException {
        String sql = String.format(
                "INSERT INTO utentiregistrati (userid, nome, cognome, codice_fiscale, email, password) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                userName, nome, cognome, codiceFiscale, email, password);

        var conn = Utils.connect(this.dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));
        boolean f = true;
        try (conn) {
            System.out.println(Utils.queryDB(conn, sql));
            // this.c = new ArrayList<>();
            // readDatabase();

        } catch (Exception e) {
            e.printStackTrace();
            f = false;
        }

        this.c = new ArrayList<>();
        readDatabase();
        return f;

    }

}
