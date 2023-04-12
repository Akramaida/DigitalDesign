package com.digdes.school;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JavaSchoolStarter {

    private final List<Map<String,Object>> data = new ArrayList<>();
    public List<Map<String,Object>> execute(String request) throws Exception {

        //Validate
        validateRequest(request);

        // Split the request string into tokens
        String[] tokens = request.split(" ");

        List<Map<String,Object>> result = new ArrayList<>();

        if (tokens[0].equalsIgnoreCase("INSERT")) {
            // Parse the INSERT command and add the new row to the data list
            Map<String,Object> row = new HashMap<>();
            for (int i = 2; i < tokens.length; i++) {

                String key = returnKey(tokens[i].split("="));
                Object value = returnValue(tokens[i].split("="));
                row.put(key, value);
            }
            data.add(row);
        } else if (tokens[0].equalsIgnoreCase("UPDATE")) {
            // Parse the UPDATE command and modify the matching row(s) in the data list
            String where;
            for (int i = 2; i < tokens.length; i++) {
                if (tokens[i].equalsIgnoreCase("WHERE")) {
                    where = tokens[i+1];
                    String whereKey = returnKey(where.split("="));
                    Object whereValue = returnValue(where.split("="));
                    for(int j = 2; j < tokens.length; j++){
                        if (!tokens[j].equalsIgnoreCase("WHERE")) {
                            String key = returnKey(tokens[j].split("="));
                            Object value = returnValue(tokens[j].split("="));

                            data.stream()
                                .filter(map -> map.get(whereKey).equals(whereValue))
                                .findFirst()
                                .ifPresent(map -> map.put(key, value));
                        }
                    }
                }
            }

        } else if (tokens[0].equalsIgnoreCase("SELECT")) {
            // Return all rows in the data list
            result.addAll(data);
        } else if (tokens[0].equalsIgnoreCase("DELETE")) {
            // Parse the DELETE command and remove matching row from the data list
            String where;
            for (int i = 2; i < tokens.length; i += 2) {
                if (tokens[i].equalsIgnoreCase("WHERE")) {
                    where = tokens[i+1];
                    String whereKey = returnKey(where.split("="));
                    Object whereValue = returnValue(where.split("="));
                    data.stream()
                        .filter(map -> map.get(whereKey).equals(whereValue))
                        .findFirst()
                        .ifPresent(data::remove);
                }
            }

        } else {
            // Invalid command
            throw new IllegalArgumentException("Invalid command: " + request);
        }

        return result;
    }

    // Helper method to parse a string value

    private Object returnValue(String[] split) {
        return parseValue(split[1]);
    }

    private String returnKey(String[] split) {
        return split[0].replace("'", "").replace(",", ";");
    }
    private Object parseValue(String value) {
        value = value.replace("'", "").replace(",", "");
        if (value.equalsIgnoreCase("true")) {
            return true;
        } else if (value.equalsIgnoreCase("false")) {
            return false;
        } else if (value.matches("\\d+")) {
            return Integer.parseInt(value);
        } else if (value.matches("\\d+\\.\\d+")) {
            return Double.parseDouble(value);
        } else {
            return value;
        }
    }

    private void validateRequest(String request) {
        // Validate input
        if (request == null || request.isEmpty()) {
            throw new IllegalArgumentException("Invalid input: request cannot be null or empty");
        }

        // Check for potential SQL injection vulnerabilities
        if (request.contains(";") || request.contains("--")) {
            throw new IllegalArgumentException("Invalid input: potential SQL injection vulnerability");
        }

        // Split the request string into tokens
        String[] tokens = request.split(" ");

        if (tokens.length < 1) {
            throw new IllegalArgumentException("Invalid input: request must contain at least one token");
        }

        if (!tokens[0].equalsIgnoreCase("INSERT")
            && !tokens[0].equalsIgnoreCase("UPDATE")
            && !tokens[0].equalsIgnoreCase("SELECT")
            && !tokens[0].equalsIgnoreCase("DELETE")) {
            throw new IllegalArgumentException("Invalid input: invalid command");
        }
    }
}
