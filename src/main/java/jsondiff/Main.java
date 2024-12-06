package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        JsonDiff jsonDiff = new JsonDiff();

        String simpleJson1 = "{\"name\": \"John\", \"age\": 25}";
        String simpleJson2 = "{\"name\": \"John\", \"age\": 26, \"country\": \"USA\"}";

        String nestedJson1 = """
                {
                  "person": {
                    "name": "John",
                    "age": 25,
                    "address": {
                      "city": "New York",
                      "zip": "10001"
                    }
                  }
                }
                """;

        String nestedJson2 = """
                {
                  "person": {
                    "name": "John",
                    "age": 25,
                    "address": {
                      "city": "Los Angeles",
                      "zip": "90001"
                    }
                  }
                }
                """;

        String arrayJson1 = """
                {
                  "skills": ["coding", "design", "testing"]
                }
                """;

        String arrayJson2 = """
                {
                  "skills": ["coding", "writing"]
                }
                """;

        try {
            JsonNode jsonNode1 = jsonDiff.parseJson(simpleJson1);
            JsonNode jsonNode2 = jsonDiff.parseJson(simpleJson2);

            System.out.println(" Case 4: JsonNode Comparison ");
            System.out.println(jsonDiff.getDiff(jsonNode1, jsonNode2));
        } catch (IOException e) {
            System.err.println("Error parsing JSON nodes: " + e.getMessage());
        }

        String identicalJson1 = "{\"name\": \"Bob\", \"age\": 30}";
        String identicalJson2 = "{\"name\": \"Bob\", \"age\": 30}";

        try {
            System.out.println("\n Case 1: Simple JSON Comparison ");
            System.out.println(jsonDiff.getDiff(simpleJson1, simpleJson2));

            System.out.println("\n Case 2: Nested JSON Object Comparison ");
            System.out.println(jsonDiff.getDiff(nestedJson1, nestedJson2));

            System.out.println("\n Case 3: JSON Array Comparison ");
            System.out.println(jsonDiff.getDiff(arrayJson1, arrayJson2));

            System.out.println("\n Case 5: Identical JSON Comparison ");
            String identicalDiff = jsonDiff.getDiff(identicalJson1, identicalJson2);
            if (identicalDiff.isEmpty()) {
                System.out.println("No differences found!");
            } else {
                System.out.println(identicalDiff);
            }
        } catch (IOException e) {
            System.err.println("Error generating JSON diff: " + e.getMessage());
        }
    }
}
