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
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Users interface for rmi allows communication between client and server for
 * book related queries
 */
public class Users extends UnicastRemoteObject implements UsersInterface {

    public ArrayList<User> c = new ArrayList<>();
    String dbUrl;
    String user;
    String pass;

    /**
     * reads database content for utentiregistrati table
     */
    private void readDatabase() {
        this.c = new ArrayList<>();
        String sql = "SELECT * FROM utentiregistrati order by id;";
        var conn = Utils.connect(dbUrl, this.user, this.pass);

        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {
                var userid = rs.getString("userid");
                var password = rs.getString("password");
                var id = rs.getInt("id");

                User b = new User("", "", "", "", userid, password);

                var c2 = Utils.connect(dbUrl, this.user, this.pass);
                var libs = Utils.queryDB(c2, String.format("select * from librerie where userid = %d;", id));
                List<Library> userLibs = new ArrayList<Library>();
                while (libs.next()) {
                    var libri = libs.getArray("libri");

                    var stream = Arrays.stream(libri.toString().split(","))
                            .map(e -> e.replace("{", ""))
                            .map(e -> e.replace("}", ""))
                            .map(e -> Integer.parseInt(e))
                            .collect(Collectors.toList());

                    // System.out.println(libri[0]);
                    // System.out.println(stream);
                    // System.out.println(stream.size());
                    Library lib = new Library(libs.getString("nome_libreria"), stream);
                    userLibs.add(lib);

                }
                b.setLibrary(userLibs);

                // System.out.println(userLibs);
                add(b);

            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * construct the users object
     *
     * @param dbUrl
     * @throws RemoteException
     */
    public Users(String dbUrl, String user, String pass) throws RemoteException {
        this.user = user;
        this.pass = pass;
        this.dbUrl = dbUrl;
        readDatabase();

    }

    /**
     * add user to the list
     *
     * @param b user to add
     */
    public void add(User b) {
        this.c.add(b);
    }

    /**
     * get user by username
     *
     * @param userName username to search
     * @return list of users
     */
    @Override
    public List<User> get(String userName) {
        readDatabase();
        Utils.log("client user query -> " + userName);
        return Utils.cerca(this.c, u -> u.getUserid().equals(userName));
    }

    /**
     * @param userName
     * @param password
     * @return boolean
     * @throws RemoteException
     */
    @Override
    public boolean login(String userName, String password) throws RemoteException {
        try {
            return get(userName).get(0).getPassword().equals(password);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * @param userName
     * @param password
     * @param codiceFiscale
     * @param email
     * @param nome
     * @param cognome
     *
     * @return boolean. success or not success
     *
     */
    @Override
    @SuppressWarnings("CallToPrintStackTrace")
    public boolean add(String userName, String password, String codiceFiscale, String email, String nome,
            String cognome) throws RemoteException {
        String sql = String.format(
                "INSERT INTO utentiregistrati (userid, nome, cognome, codice_fiscale, email, password) VALUES ('%s', '%s', '%s', '%s', '%s', '%s')",
                userName, nome, cognome, codiceFiscale, email, password);

        var conn = Utils.connect(this.dbUrl, this.user, this.pass);
        boolean f = true;
        try (conn) {
            Utils.queryDB(conn, sql);
            System.out.println("adding user to database");

        } catch (Exception e) {
            e.printStackTrace();
            f = false;
        }

        this.c = new ArrayList<>();
        readDatabase();
        return f;

    }

    /**
     * @param id
     * @return User
     * @throws RemoteException
     */
    @Override
    public User get(int id) throws RemoteException {
        return this.c.get(id);
    }

}
