package com.example;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public class Server {
    public static String URL = "jdbc:postgresql://192.168.0.100:5432/book";
    public static String USER = "root";
    public static String PASS = "root";

    private static String readFile(String path) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null; // or handle the exception as needed
        }

        return content;

    }

    private static void readCsv(String path) {
        try {
            Reader in = new FileReader(path);
            Iterable<CSVRecord> records = CSVFormat.DEFAULT.builder()
                .setHeader("Title", "Authors", "Description", "Category", "Publisher", "Price Starting With ($)", "Publish Date (Month)", "Publish Date (Year)")
                .setSkipHeaderRecord(true).build().parse(in);

            for (CSVRecord record : records) {
                String column1 = record.get("Title");
                String column2 = record.get("Authors").replace("By ", "").replace(",", "");
                String column3 = record.get("Publish Date (Year)");
                System.out.println(String.format("title:\t%s\nauthor:\t%s\nyear:\t%s\n\n", column1, column2, column3));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static Connection connect() {
        try {
            return DriverManager.getConnection(URL, USER, PASS);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static void initDb() throws SQLException {
        var path = Paths.get("data", "BooksDatasetClean.csv").toString();
        // var file =
        readCsv(path);
        // System.out.println(file.get(0));
        // System.out.println(file.get(1));
        // String init = readFile(Paths.get("data", "init.sql").toString());

        try (Connection conn = connect()) {
            var stmt = conn.createStatement();
            // stmt.execute(init);
        }

    }

    public static void main(String[] args) {
        try {
            initDb();
            System.out.println("lol");
            rmiBook obj = new rmiBook();
            Registry registry = LocateRegistry.createRegistry(1099);
            registry.rebind("MyRemoteObject", obj);
            System.out.println("Server is ready.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
