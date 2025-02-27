package com.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * A class that represents a user.
 */
public class User implements Serializable {

    /**
     * Basic user information. Each field is self descriptive.
     */
    private String nome, cognome, codiceFiscale, email, userid, password;

    /**
     * Basic user information put together in a list of string to easily write
     * it in a csv file.
     */
    private List<String> data;

    /**
     * List of libraries written by the user. @see Library
     */
    private List<Library> libs;

    /**
     * user constructor
     *
     * @param nome the name of the user.
     * @param cognome the surname of the user.
     * @param codiceFiscale the codiceFiscale of the user.
     * @param email the email of the user.
     * @param userid the userid of the user.
     * @param password the password of the user.
     */
    public User(String nome, String cognome, String codiceFiscale, String email, String userid, String password) {
        this.nome = nome;
        this.cognome = cognome;
        this.codiceFiscale = codiceFiscale;
        this.email = email;
        this.userid = userid;
        this.password = password;
        this.data = List.of(nome, cognome, codiceFiscale, email, userid, password);
    }

    /**
     * user constructor
     *
     * @param csvLine the csvLine we read from file containing book information.
     */
    public User(String csvLine) {
        String[] infos = csvLine.split(",");
        this.nome = infos[0];
        this.cognome = infos[1];
        this.codiceFiscale = infos[2];
        this.email = infos[3];
        this.userid = infos[4];
        this.password = infos[5];

    }

    /**
     * data field getter.
     *
     * @return the user data, ready to be written to a csv file.
     */
    public List<String> getData() {
        return new ArrayList<>(this.data);
    }

    /**
     * userid field getter.
     *
     * @return the user userid.
     */
    public String getUserid() {
        return this.userid;
    }

    /**
     * password field getter.
     *
     * @return the user password.
     */
    public String getPassword() {
        return this.password;
    }

    /**
     * user libraries getter.
     *
     * @return the user libraries.
     */
    public List<Library> getLibs() {
        return this.libs;
    }

    /**
     * returns the User object to string.
     *
     * @return User object to string.
     */
    @Override
    public String toString() {
        return String.format("nome:\t\t%s\ncognome:\t%s\ncodiceFiscale:\t%s\nemail:\t\t%s\nuserid:\t\t%s\npassword:\t%s\n", this.nome, this.cognome, this.codiceFiscale, this.email, this.userid, this.password);

    }

    /**
     * library field setter.
     *
     * @param l the libraries read from file that belong to the user.
     */
    public void setLibrary(List<Library> l) {
        this.libs = l;
    }
}
