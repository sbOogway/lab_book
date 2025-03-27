/**
 * @author  Mattia Papaccioli 747053 CO
 * @version 1.0
 * @since 1.0
 */
package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * The Utils class provides utility methods for various operations such as file
 * reading, database connection, hashing, JSON parsing, and more. These methods
 * are designed to simplify common tasks and improve code reusability.
 */
public class Utils {

    /**
     * Parses a string representation of a map into a Map object. The input
     * string should be in the format "{key1=value1, key2=value2, ...}".
     *
     * @param d The string representation of the map.
     * @return A Map<String, Object> containing the parsed key-value pairs.
     */
    public static Map<String, Object> parseMap(String d) {
        return Arrays.stream(d.substring(1, d.length() - 1).trim().split(", "))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(
                        keyValue -> keyValue[0].trim(),
                        keyValue -> keyValue[1].trim()));
    }

    /**
     * Reads the contents of a file and returns it as a string.
     *
     * @param path The path to the file to read.
     * @return The contents of the file as a string.
     */
    public static String readFile(String path) {
        String content = "";

        try {
            content = new String(Files.readAllBytes(Paths.get(path)));
        } catch (IOException e) {
            e.printStackTrace();
            return null; // or handle the exception as needed
        }

        return content;

    }

    /**
     * Reads the contents of a file and returns it as a list of strings.
     * @param <T> the type of the class we want to instatiate for the list.
     * @param filepath The path to the file to read.
     * @param f The predicate to filter the lines.
     * @return A list of strings containing the lines of the file.
     */
    static <T> ArrayList<String> csvReaderFiltered(String filepath, Predicate<String> f) {
        ArrayList<String> fLines = new ArrayList<>();
        try (Stream<String> lines = Files.lines(Paths.get(filepath))) {
            fLines = lines.filter(f)
                    .collect(Collectors.toCollection(ArrayList::new));
        } catch (IOException e) {
            System.err.println("error reading file");
        }
        return fLines;

    }

    /**
     * Converts a map to a JSON string. The resulting JSON string will have keys
     * enclosed in double quotes and values as either strings (if they are strings)
     * or as raw values (if they are numbers or booleans).  Null values are
     * represented as "null" in the JSON string.    
     * 
     * @param map The map to convert to a JSON string.
     * @return The JSON string representation of the map.
     */
    public static String mapToJson(Map<String, Object> map) {
        StringJoiner jsonJoiner = new StringJoiner(", ", "{", "}");

        map.forEach((key, value) -> {
            String jsonValue;
            if (value == null) {
                jsonValue = "null";
            } else if (value instanceof String) {
                jsonValue = "\"" + value + "\""; // Add quotes for strings
            } else {
                jsonValue = value.toString(); // Use toString() for other types
            }
            jsonJoiner.add("\"" + key + "\": " + jsonValue); // Add key-value pair
        });

        return jsonJoiner.toString(); // Return the constructed JSON string
    }

    /**
     * Converts a JSON string to a map. The input JSON string should be in the format
     * "{key1: value1, key2: value2, ...}". The resulting map will have keys as
     * strings and values as either strings, integers, doubles, booleans, or null.
     * @param json The JSON string to convert to a map.
     * @return A Map<String, Object> containing the parsed key-value pairs.
     */
    public static Map<String, Object> jsonToMap(String json) {
        Map<String, Object> map = new HashMap<>();

        // Remove the curly braces at the start and end
        json = json.trim();
        if (json.startsWith("{") && json.endsWith("}")) {
            json = json.substring(1, json.length() - 1);
        }

        // Split the string by commas to get key-value pairs
        String[] keyValuePairs = json.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)"); // Regex to handle quoted strings
        for (String pair : keyValuePairs) {
            String[] entry = pair.split(":", 2); // Split into key and value
            String key = entry[0].trim().replaceAll("^\"|\"$", ""); // Remove quotes from key
            String value = entry[1].trim();

            // Determine the type of the value
            if (value.equals("null")) {
                map.put(key, null);
            } else if (value.equals("true") || value.equals("false")) {
                map.put(key, Boolean.valueOf(value));
            } else if (value.matches("-?\\d+")) { // Integer
                map.put(key, Integer.valueOf(value));
            } else if (value.matches("-?\\d+\\.\\d+")) { // Double
                map.put(key, Double.valueOf(value));
            } else {
                map.put(key, value.replaceAll("^\"|\"$", "")); // Remove quotes from string values
            }
        }

        return map;
    }

    /**
     * Hashes a string using the SHA-256 algorithm.
     *
     * @param input The string to hash.
     * @return The hashed string.
     */
    public static String hashString(String input) {
        try {
            // Create a MessageDigest instance for SHA-256
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            // Perform the hashing
            byte[] hashBytes = digest.digest(input.getBytes());

            // Convert the byte array to a hexadecimal string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Returns the current timestamp in the format "yyyy-MM-dd HH:mm:ss.SSS".
     * @return  The current timestamp as a string.
     */
    private static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return formatter.format(new Date());
    }

    /**
     * Logs a message to the console with a timestamp.
     * @param message The message to log.
     */
    public static void log(String message) {
        System.out.println("[" + getCurrentTimestamp() + "] " + message);
    }

    /**
     * Counts the number of occurrences of a substring in a string.
     * @param str The string to search.
     * @param pattern The substring to count.
     * @return The number of occurrences of the substring in the string.
     */
    public static int countSubs(String str, String pattern) {
        return str.split(pattern).length - 1;
    }

    /**
     * Removes parentheses from a string.
     * @param str The string to remove parentheses from.
     * @return The string with parentheses removed.
     */
    public static String removeParentheses(String str) {
        return str.replace("(", "").replace(")", "");
    }

    /**
     * Converts a string to a float.   
     * 
     * @param str The string to convert to a float.
     * @return The float value of the string.
     * @throws Exception if the string cannot be converted to a float. 
     */
    public static float stringToFloat(String str) throws Exception {
        return Float.parseFloat(str);
    }

    /**
     * Looks for an item in a list.
     *
     * @param items list of objects where we wanna search.
     * @param f predicate where we filter to look for a specific item.
     * @param <T> the type of the class we want to instatiate for the list.
     * @return a list where there are the items we looked for in the predicate.
     */
    static <T> List<T> cerca(List<T> items, Predicate<T> f) {
        return items.stream()
                .filter(f)
                .collect(Collectors.toList());
    }

    /**
     * Connects to a database using the specified URL, username, and password.
     *
     * @param url  The URL of the database.
     * @param user The username for the database connection.
     * @param pass The password for the database connection.
     * @return A Connection object representing the database connection.
     */
    public static Connection connect(String url, String user, String pass) {
        try {
            return DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Executes a SQL query on the specified database connection and returns the
     * result set.
     *
     * @param c   The database connection to use.
     * @param sql The SQL query to execute.
     * @return A ResultSet object containing the results of the query.
     * @throws SQLException if an error occurs while executing the query.
     */
    public static ResultSet queryDB(Connection c, String sql) throws SQLException {
        try (c) {
            java.sql.Statement stmt = c.createStatement();
            try {
                return stmt.executeQuery(sql);
            } catch (SQLException e) {
                e.printStackTrace();
                return null;
            }

        }
    }

}
