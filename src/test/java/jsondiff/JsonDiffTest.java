package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

public class JsonDiffTest {

    @Test
    public void testJsonDiffUsingStrings() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        String json2 = "{\"name\": \"John\", \"age\": 31, \"country\": \"USA\"}";

        JsonDiff jsonDiff = new JsonDiff();
        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at age"));
        assertTrue(diff.contains("Missing in second JSON: city"));
        assertTrue(diff.contains("Extra in second JSON: country"));
    }

    @Test
    public void testJsonDiffUsingNodes() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        String json2 = "{\"name\": \"John\", \"age\": 31, \"country\": \"USA\"}";

        JsonDiff jsonDiff = new JsonDiff();

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.contains("Value mismatch at age"));
        assertTrue(diff.contains("Missing in second JSON: city"));
        assertTrue(diff.contains("Extra in second JSON: country"));
    }

    @Test
    public void testJsonDiffNestedObjects() throws IOException {
        String json1 = "{\"person\": {\"name\": \"John\", \"age\": 30}}";
        String json2 = "{\"person\": {\"name\": \"John\", \"age\": 31}}";

        JsonDiff jsonDiff = new JsonDiff();

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.contains("Value mismatch at person.age"));
    }

    @Test
    public void testJsonDiffEmptyJson() throws IOException {
        String json1 = "{}";
        String json2 = "{\"name\": \"John\"}";

        JsonDiff jsonDiff = new JsonDiff();

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.contains("Extra in second JSON: name"));
    }

    @Test
    public void testJsonDiffWithMissingKeys() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 30}";
        String json2 = "{\"name\": \"John\"}";

        JsonDiff jsonDiff = new JsonDiff();

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.contains("Missing in second JSON: age"));
    }

    @Test
    public void testJsonDiffWithExtraKeys() throws IOException {
        String json1 = "{\"name\": \"John\"}";
        String json2 = "{\"name\": \"John\", \"age\": 30}";

        JsonDiff jsonDiff = new JsonDiff();

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.contains("Extra in second JSON: age"));
    }

    @Test
    public void testJsonDiffIdenticalJson() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 30}";
        String json2 = "{\"name\": \"John\", \"age\": 30}";

        JsonDiff jsonDiff = new JsonDiff();

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.isEmpty(), "Expected no differences for identical JSON objects");
    }
}
