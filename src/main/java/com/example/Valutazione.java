package com.example;

/**
 * A class that represents a valutazione for a book.
 * Five Valutazione put together makes a Review.
 * @see Review
 */
class Valutazione {
	/**
	 * The vote for the category.
	 */
	private int voto;
	/**
	 * The note for the category.
	 */
	private String note = new String(new char[256]);
	/**
	 * The category name.
	 */
	private String nome;
	
	/**
	 * Valutazione constructor 
	 * @param nome the category being evaluated (stile, contenuto, gradevolezza, originalita, edizione).
	 * @param voto the voto for the category.
	 * @param note an eventual note for the category, max 256 characters. "NA" is the not available note.
	 */
	public Valutazione(String nome, int voto, String note) {
		this.nome = nome;
		this.voto = voto;
		this.note = note;
	}

	/**
	 * voto field getter.
	 * @return the category vote.
	 */
	public int getVoto() {
		return this.voto;
	}
	
	/**
	 * note field getter.
	 * @return the category note.
	 */
	public String getNote() {
		return this.note;
	}
	
	/**
	 * returns the valutazione object to string.
	 * @return Valutazione object to string.
	 */
	@Override
	public String toString() {
		return String.format("\nnome:\t%s\nvoto:\t%s\nnote:\t%s\n", this.nome, this.voto, this.note);
	}
}