package com.example;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * A class that represent a review for a book.
 */
public class Review {
	/**
	 * For each category we have a Valutazione.
	 */
	private List<Valutazione> vals;

	/**
	 * The average of the votes for each category.
	 */
	private float votoFinale;

	/**
	 * The userid of the review writer.
	 */
	private String owner;


	/**
	 * review constructor
	 * @param l     a list of Valutazione, one for each category. @see Valutazione
	 * @param owner the userid of the Review writer.
	 */
	public Review(List<Valutazione> l, String owner) {
		this.owner      = owner;
		this.vals       = l;
		this.votoFinale = (float) vals.stream().mapToInt(v -> v.getVoto()).average().orElse(1.0); 
	}

	/**
	 * review constructor from csvLine
	 * @param csvLine the csvLine read from file. we create one object for each line read in the file.
	 */
	public Review(String csvLine) {
		String[] infos = csvLine.split(","); 
		this.owner = infos[1];
		Valutazione s = new Valutazione("stile",        Integer.parseInt(infos[2]), infos[7]);
		Valutazione c = new Valutazione("contenuto",    Integer.parseInt(infos[3]), infos[8]);
		Valutazione g = new Valutazione("gradevolezza", Integer.parseInt(infos[4]), infos[9]);
		Valutazione o = new Valutazione("originalita",  Integer.parseInt(infos[5]), infos[10]);
		Valutazione e = new Valutazione("edizione",     Integer.parseInt(infos[6]), infos[11]);
		this.vals = Arrays.asList(s, c, g, o, e);
		this.votoFinale = (float) vals.stream().mapToInt(v -> v.getVoto()).average().orElse(1.0); 
	}
	
	/**
	 * returns the Review object to string.
	 * @return Valutazione object to string.
	 */
	@Override
	public String toString() {
		List<String> vs = this.vals.stream().map(Valutazione::toString).collect(Collectors.toList());
		return String.format("---review---\nowner:\t%s\n%s\nfinal:\t%.2f\n---end---\n", this.owner, vs.toString().replace(",", "").replace("[", "").replace("]", ""), this.votoFinale);

	}

	/**
	 * dumps the review to csv
	 * @return the votes and notes for each category concatenated in a list of strings, ready to be written to a file. @see Utils.csvWriter
	 */
	public List<String> toCsv() {
		List<String> votes =  this.vals.stream().map(Valutazione::getVoto).map(String::valueOf).collect(Collectors.toList());
		List<String> notes = this.vals.stream().map(Valutazione::getNote).collect(Collectors.toList());
		return Stream.concat(votes.stream(), notes.stream()).collect(Collectors.toList());
	}

	/**
	 * stile vote getter.
	 * @return the stile vote.
	 */
	public int getVotoStile() {
		return this.vals.get(0).getVoto();
	}

	/**
	 * contenuto vote getter.
	 * @return the contenuto vote.
	 */
	public int getVotoContenuto() {
		return this.vals.get(1).getVoto();
	}

	/**
	 * gradevolezza vote getter.
	 * @return the gradevolezza vote.
	 */
	public int getVotoGradevolezza() {
		return this.vals.get(2).getVoto();
	}

	/**
	 * originalita vote getter.
	 * @return the originalita vote.
	 */
	public int getVotoOriginalita() {
		return this.vals.get(3).getVoto();
	}

	/**
	 * edizione vote getter.
	 * @return the edizione vote.
	 */
	public int getVotoEdizione() {
		return this.vals.get(4).getVoto();
	}
}