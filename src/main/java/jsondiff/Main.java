package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

/**
 * Demo class to showcase the use of the methods in JsonDiff class
 */
public class Main {

    public static void main(String[] args) {
        JsonDiff jsonDiff = new JsonDiff();

        String jsonString1 = "{\"name\": \"John\", \"age\": 25}";
        String jsonString2 = "{\"name\": \"John\", \"age\": 26, \"country\": \"USA\"}";

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
            ObjectMapper mapper = new ObjectMapper();
            JsonNode jsonNode1 = mapper.readTree(jsonString1);
            JsonNode jsonNode2 = mapper.readTree(jsonString2);

            System.out.println("JsonNode Comparison ");
            System.out.println(jsonDiff.getDiff(jsonNode1, jsonNode2));

            boolean isNodesNotEqual = jsonDiff.isNotEqual(jsonNode1, jsonNode2);
            System.out.println("Are JsonNode objects not equal? " + isNodesNotEqual);
            
        } catch (IOException e) {
            System.err.println("Error parsing JSON nodes: " + e.getMessage());
        }

        String identicalJson1 = "{\"name\": \"Bob\", \"age\": 30}";
        String identicalJson2 = "{\"name\": \"Bob\", \"age\": 30}";

        try {
            System.out.println("\nSimple JSON Comparison ");
            System.out.println(jsonDiff.getDiff(jsonString1, jsonString2));

            System.out.println("\nNested JSON Object Comparison ");
            System.out.println(jsonDiff.getDiff(nestedJson1, nestedJson2));

            System.out.println("\nJSON Array Comparison ");
            System.out.println(jsonDiff.getDiff(arrayJson1, arrayJson2));

            System.out.println("\nIdentical JSON Comparison ");
            String identicalDiff = jsonDiff.getDiff(identicalJson1, identicalJson2);
            if (identicalDiff.isEmpty()) {
                System.out.println("No differences found!");
            } else {
                System.out.println(identicalDiff);
            }

            boolean isEqual = jsonDiff.isEqual(jsonString1, jsonString2);
            System.out.println("\nJSON String 1 " + jsonString1);
            System.out.println("JSON String 2 " + jsonString2);

            System.out.println("\nAre JSONs equal? " + isEqual);

            boolean isSubset = jsonDiff.isSubset(jsonString1, jsonString2);
            System.out.println("Is JSON1 a subset of JSON2? " + isSubset);

            boolean isNotEqual = jsonDiff.isNotEqual(jsonString1, jsonString2);
            System.out.println("Are JSONs not equal? " + isNotEqual);
            
        } catch (IOException e) {
            System.err.println("Error generating JSON diff: " + e.getMessage());
        }
    }
}
