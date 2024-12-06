package jsondiff;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 25, \"skills\": {\"coding\": true}}";
        String json2 = "{\"name\": \"John\", \"age\": 26, \"skills\": {\"coding\": false, \"design\": true}}";

        String diff = JsonDiff.getDiff(json1, json2);
        System.out.println(diff);
    }
}
