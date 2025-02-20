package com.example;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class Users extends UnicastRemoteObject implements UsersInterface {
    ArrayList<User> c = new ArrayList<>();

    public Users(String dbUrl) throws RemoteException {
        String sql = "SELECT * FROM libri;";
        var conn = Utils.connect(dbUrl, System.getenv("DB_USER"), System.getenv("DB_PASS"));

        try (conn) {
            var rs = Utils.queryDB(conn, sql);
            while (rs.next()) {

            }
        } catch (SQLException e){
            e.printStackTrace();
        }
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
        return get(userName).get(0).getPassword().equals(password);
    }
    
}
