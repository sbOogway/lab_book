/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.List;

/**
 * Book helper class 
 */
public class Book implements Serializable{
    /**
     * Self descriptive.
     */
    private String title, authors, publisher, category;

    /**
     * Year of the book publication.
     */
    private final short year;

    /**
     * Index of the book in the csv file. it is handy because it univocally
     * represents a book (primary key).
     */
    private int index;

    /**
     * List of Review objects. @see Review
     */
    private List<Review> reviews;

    /**
     * Libro constructor
     *
     * @param title the title of the book.
     * @param authors the authors of the book.
     * @param publisher the publisher of the book.
     * @param category the category of the book.
     * @param year the publication year of the book.
     * @param id id book
     * @throws RemoteException
     */
    public Book(String title, String authors, String publisher, String category, short year, int id) throws RemoteException {
        this.title = title;
        this.authors = authors;
        this.publisher = publisher;
        this.category = category;
        this.year = year;
        this.index = id;
    }

    /**
     * In category field we replace double space with single space because of
     * first char of each line in the csv is empty.
     *
     * @param csvLine the csvLine read from file. we create one object for each
     * line read in the file.
     * @throws RemoteException
     */
    public Book(String csvLine) throws RemoteException {
        String[] infos = csvLine.split(",");
        this.index = Integer.parseInt(infos[0]);
        this.title = infos[1];
        this.authors = infos[2];
        // first category char is empty
        try {
            this.category = infos[3].replace("  ", " ").substring(1);
        } catch (Exception e) {
            this.category = "";
        }
        this.publisher = infos[4];
        this.year = Short.parseShort(infos[5]);

    }

    /**
     * returns the Libro object to string.
     *
     * @return Libro object to string.
     */
    @Override
    public String toString() {
        return String.format("title:\t%s\nauths:\t%s\npubl:\t%s\ncat:\t%s\ndate:\t%d\nid:\t%d\n" , this.title, this.authors, this.publisher, this.category, this.year, this.index);
    }

    /**
     * title field getter.
     *
     * @return the book title.
     */
    public String getTitle() {
        return this.title;
    }

    /**
     * author field getter.
     *
     * @return the book author.
     */
    public String getAuthor() {
        return this.authors;
    }
    /**
     * author field getter.
     *
     * @return the book category.
     */
    public String getCategory() {
        return this.category;
    }
    /**
     * author field getter.
     *
     * @return the book publisher.
     */
    public String getPublisher() {
        return this.publisher;
    }

    /**
     * year field getter.
     *
     * @return the book publication year.
     */
    public short getYear() {
        return this.year;
    }

}
