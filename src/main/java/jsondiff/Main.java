package jsondiff;

import java.io.IOException;

public class Main {

    public static void main(String[] args) {
        String json1 = "{\"name\": \"John\", \"age\": 25, \"skills\": {\"coding\": true}}";
        String json2 = "{\"name\": \"John\", \"age\": 26, \"skills\": {\"coding\": false, \"design\": true}}";

        try {
            JsonDiff jsonDiff = new JsonDiff();

            String diff = jsonDiff.getDiff(json1, json2);

            System.out.println(diff);
        } catch (IOException e) {
            System.err.println("An error occurred while parsing JSON: " + e.getMessage());
        }
    }
}
