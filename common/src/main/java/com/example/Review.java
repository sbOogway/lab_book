/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.io.Serializable;

/**
 * A class that represent a review for a book.
 */
public class Review implements  Serializable {

    public String owner;

    public String notes;
    public int stile;
    public int contenuto;
    public int gradevolezza;
    public int originalita;
    public int edizione;
    public int votofinale;

    /**
     * review constructor
     */
    public Review(int stile, int contenuto, int gradevolezza, int originalita, int edizione, String owner, String notes) {
        this.owner = owner;
        this.stile = stile;
        this.contenuto = contenuto;
        this.gradevolezza = gradevolezza;
        this.originalita = originalita;
        this.edizione = edizione;
        this.votofinale = (stile + contenuto + gradevolezza + originalita + edizione) / 5;
        this.notes = notes;

    }

    /**
     * returns the Review object to string.
     *
     * @return Valutazione object to string.
     */
    @Override
    public String toString() {
        return String.format("---review---\nowner:\t%s\nfinal:\t%d\n---end---\n", this.owner, this.votofinale);

    }

}
