package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public interface BooksInterface extends Remote  {
    public List<Book> get(String query, String mode) throws RemoteException;
    public String display() throws RemoteException;
    public boolean createLibrary(String user, String nome, String libri) throws RemoteException;


}
