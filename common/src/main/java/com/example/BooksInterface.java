package com.example;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;

public interface BooksInterface extends Remote  {
    public List<Book> get(String query, String mode) throws RemoteException;
    public Book get(Integer id) throws RemoteException;
    public String display() throws RemoteException;
    public boolean createLibrary(String user, String nome, String libri) throws RemoteException;
    public boolean createReview(String user, String book, int stile, int contenuto, int gradevolezza, int originalita, int edizione, String notes) throws RemoteException;
    public List<Review> getReviews(String title) throws RemoteException;
    public boolean createSuggestion(String user, String book, String book1, String book2, String book3) throws RemoteException;
    public List<Map<String, Object>> getSuggestions(String title) throws RemoteException;


}
