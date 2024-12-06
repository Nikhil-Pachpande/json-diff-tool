package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String jsonString1 = "{\"name\": \"John\", \"age\": 25, \"skills\": {\"coding\": true}}";
        String jsonString2 = "{\"name\": \"John\", \"age\": 26, \"skills\": {\"coding\": false, \"design\": true}}";

        try {
            System.out.println("JSON Diff using Strings");
            JsonDiff jsonDiff = new JsonDiff();

            String diffString = jsonDiff.getDiff(jsonString1, jsonString2);

            System.out.println(diffString);

            System.out.println("JSON Diff using JsonNode");

            JsonNode node1 = jsonDiff.parseJson(jsonString1);
            JsonNode node2 = jsonDiff.parseJson(jsonString2);

            String diffNode = jsonDiff.getDiff(node1, node2);

            System.out.println(diffNode);

        } catch (IOException e) {
            System.err.println("An error occurred while parsing JSON: " + e.getMessage());
        }
    }
}
