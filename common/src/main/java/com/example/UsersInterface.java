/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface UsersInterface extends Remote{
    public List<User> get(String userName) throws RemoteException;
    public User get(int id) throws RemoteException;
    public boolean login(String userName, String password) throws RemoteException;
    public boolean add(String userName, String password, String codiceFiscale, String email, String nome, String Cognome) throws RemoteException;
} 