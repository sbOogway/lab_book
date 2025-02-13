package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Utils {

    public static Map<String, Object> parseMap(String d) {
        return Arrays.stream(d.substring(1, d.length() - 1).trim().split(", "))
                .map(pair -> pair.split("="))
                .collect(Collectors.toMap(
                        keyValue -> keyValue[0].trim(),
                        keyValue -> keyValue[1].trim()));
    }

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

    private static String getCurrentTimestamp() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        return formatter.format(new Date());
    }

    public static void log(String message) {
        System.out.println("[" + getCurrentTimestamp() + "] " + message);
    }

    public static int countSubs(String str, String pattern) {
        return str.split(pattern).length-1;
    }

    public static String removeParentheses(String str) {
        return str.replace("(", "").replace(")", "");
    }

    public static float stringToFloat(String str) throws Exception {
        return Float.parseFloat(str);
    }
}
