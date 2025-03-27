/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * A class that represent a library. Each library is a list of books that a user puts together for various purposes.
 */
public class Library implements Serializable {

	/**
	 * The name that the user gives to the library.
	 */
	private String nome;

	/**
	 * List of book ids present in the library.
	 */
	private List<Integer> books;

	/**
	 * library constructor
	 * @param data    the library name.
	 * @param fromCsv it makes nothing. placeholder to distinguish constructor with only csvLine.
	 */
	public Library(String data, boolean fromCsv) {
		this.nome = data;
		this.books = new ArrayList<Integer>();
	}

	/**
	 * library constructor
	 * @param csvLine constructs Library object from csvLine parsing each field conventionally. @see valutazioneDati
	 */
	public Library(String csvLine) {
		String[] infos = csvLine.split(",");
		this.nome = infos[1];
		String[] bs = Arrays.copyOfRange(infos, 2, infos.length);
		this.books = Arrays.stream(bs).map(x -> Integer.parseInt(x)).collect(Collectors.toList()); 
	}

	public Library(String name, List<Integer> bookids) {
		this.nome = name;
		this.books = bookids;
	}
	
	/**
	 * if a book is alraedy in the library it will not be added.
	 * @param bookId the book id to add to the library.
	 * @return whether the book was added or not.
	 */
	boolean addBook(int bookId) {
		int found = Utils.cerca(this.books, id -> id == bookId).size();
		return switch (found) {
			case 0  -> {this.books.add(bookId); yield true;}
			default -> false; 
		};
	}

	/**
	 * books field getter.
	 * @return the books id in the library.
	 */
	public List<Integer> getBooks() {
		return this.books;
	}
	
	/**
	 * name field getter.
	 * @return the name of the library.
	 */
	public String getName() {
		return this.nome;
	}

	/**
	 * returns the Library object to string.
	 * @return Library object to string.
	 */
	@Override
	public String toString() {
		return String.format("name:\t%s\nids:\t%s\n", this.nome, this.books.toString());
	}
}