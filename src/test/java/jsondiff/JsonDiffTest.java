package jsondiff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

public class JsonDiffTest {

    @Test
    public void testJsonDiff() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        String json2 = "{\"name\": \"John\", \"age\": 31, \"country\": \"USA\"}";

        JsonDiff jsonDiff = new JsonDiff();
        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at age"));
        assertTrue(diff.contains("Missing in second JSON: city"));
        assertTrue(diff.contains("Extra in second JSON: country"));
    }

    @Test
    public void testJsonDiffNestedObjects() throws IOException {
        String json1 = "{\"person\": {\"name\": \"John\", \"age\": 30}}";
        String json2 = "{\"person\": {\"name\": \"John\", \"age\": 31}}";

        JsonDiff jsonDiff = new JsonDiff();
        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at person.age"));
    }

    @Test
    public void testJsonDiffEmptyJson() throws IOException {
        String json1 = "{}";
        String json2 = "{\"name\": \"John\"}";

        JsonDiff jsonDiff = new JsonDiff();
        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Extra in second JSON: name"));
    }
}
