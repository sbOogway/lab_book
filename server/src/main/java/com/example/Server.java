/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */

package com.example;

// import com.example.common;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Scanner;
import java.util.stream.Collectors;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Server {

    public static String HOST = System.getenv("DB_HOST"); // "root"
    public static String PORT = System.getenv("DB_PORT"); // "root"
    public static String USER = System.getenv("DB_USER"); // "user"; // "root"
    public static String PASS = System.getenv("DB_PASS");

    public static String URL = String.format("jdbc:postgresql://%s:%s/book", HOST, PORT);

    
    /** 
     * initialize the database from data/init.sql script
     * 
     * @throws SQLException
     * @throws IOException
     */
    private static void initDb() throws SQLException, IOException {
        var path = Paths.get("data", "BooksDatasetClean.csv").toString();
        String init = Utils.readFile(Paths.get("data", "init.sql").toString());

        Reader in = new FileReader(path);
        Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader("Title", "Authors", "Description", "Category", "Publisher", "Price Starting With ($)",
                        "Publish Date (Month)", "Publish Date (Year)")
                .setSkipHeaderRecord(true).build().parse(in);

        try (Connection conn = Utils.connect(URL, USER, PASS)) {
            var stmt = conn.createStatement();
            System.out.println("initdb " + stmt.execute(init));

            for (CSVRecord record : records) {
                String column1 = record.get("Title").replace("'", "");
                String column2 = record.get("Authors").replace("By ", "").replace(",", "").replace("'", "");
                String column3 = record.get("Publish Date (Year)");
                String column4 = record.get("Category").trim().replace("'", "");
                String column5 = record.get("Publisher").replace("'", "");

                var categorie = Arrays.asList(column4.split(",")).stream()
                        .map(e -> e.trim())
                        .map(e -> String.format("'%s'", e))
                        .collect(Collectors.joining(", "));

                System.out.println(
                        String.format("title:\t\t%s\nauthor:\t\t%s\nyear:\t\t%s\ncategory:\t%s\npublisher:\t%s\n\n",
                                column1, column2, column3, column4, column5));

                String sql = String.format(
                        "INSERT INTO Libri (titolo, autore, editore, categoria, anno) VALUES ('%s', '%s', '%s', ARRAY[%s], %s);",
                        column1, column2, column5, categorie, column3);

                stmt.execute(sql);

            }

        }

    }

    /**
     * launch the server
     * @param args
     */
    public static void main(String[] args) {

        try {
            // initDb();

            Books books = new Books(URL);
            Users users = new Users(URL);


            Registry registry = LocateRegistry.createRegistry(1099);

            registry.rebind("books", books);
            registry.rebind("users", users);

            Scanner s = new Scanner(System.in);
            System.out.println("press enter to stop the server");
            s.nextLine();
            s.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
