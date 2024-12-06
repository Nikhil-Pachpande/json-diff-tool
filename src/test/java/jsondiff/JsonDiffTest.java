package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.io.IOException;

public class JsonDiffTest {

    private final JsonDiff jsonDiff = new JsonDiff();

    @Test
    public void testSimpleJsonComparison() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 25}";
        String json2 = "{\"name\": \"John\", \"age\": 26, \"country\": \"USA\"}";

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at age: 25 vs 26"));
        assertTrue(diff.contains("Extra in second JSON: country"));
        assertFalse(diff.contains("Missing in second JSON"));
    }

    @Test
    public void testNestedJsonComparison() throws IOException {
        String json1 = """
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

        String json2 = """
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

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at person.address.city: \"New York\" vs \"Los Angeles\""));
        assertTrue(diff.contains("Value mismatch at person.address.zip: \"10001\" vs \"90001\""));
        assertFalse(diff.contains("Missing in second JSON"));
        assertFalse(diff.contains("Extra in second JSON"));
    }

    @Test
    public void testJsonArrayComparison() throws IOException {
        String json1 = """
                {
                  "skills": ["coding", "design", "testing"]
                }
                """;

        String json2 = """
                {
                  "skills": ["coding", "writing"]
                }
                """;

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at skills[1]: \"design\" vs \"writing\""));
        assertFalse(diff.contains("Missing in second JSON: skills[2]"));
        assertFalse(diff.contains("Extra in second JSON"));
    }

    @Test
    public void testJsonNodeComparison() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 25}";
        String json2 = "{\"name\": \"John\", \"age\": 26, \"country\": \"USA\"}";

        JsonNode node1 = jsonDiff.parseJson(json1);
        JsonNode node2 = jsonDiff.parseJson(json2);

        String diff = jsonDiff.getDiff(node1, node2);

        assertTrue(diff.contains("Value mismatch at age: 25 vs 26"));
        assertTrue(diff.contains("Extra in second JSON: country"));
        assertFalse(diff.contains("Missing in second JSON"));
    }

    @Test
    public void testIdenticalJsonComparison() throws IOException {
        String json1 = "{\"name\": \"Bob\", \"age\": 30}";
        String json2 = "{\"name\": \"Bob\", \"age\": 30}";

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.isEmpty(), "Expected no differences for identical JSON objects");
    }

    @Test
    public void testEmptyJsonComparison() throws IOException {
        String json1 = "{}";
        String json2 = "{\"name\": \"John\"}";

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Extra in second JSON: name"));
        assertFalse(diff.contains("Missing in second JSON"));
    }

    @Test
    public void testJsonWithMissingFields() throws IOException {
        String json1 = "{\"name\": \"John\", \"age\": 25}";
        String json2 = "{\"name\": \"John\"}";

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Missing in second JSON: age"));
        assertFalse(diff.contains("Extra in second JSON"));
    }

    @Test
    public void testJsonWithExtraFields() throws IOException {
        String json1 = "{\"name\": \"John\"}";
        String json2 = "{\"name\": \"John\", \"age\": 25}";

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Extra in second JSON: age"));
        assertFalse(diff.contains("Missing in second JSON"));
    }

    @Test
    public void testComplexJsonComparison() throws IOException {
        String json1 = """
                {
                  "person": {
                    "name": "John",
                    "age": 25,
                    "skills": ["coding", "design"],
                    "address": {
                      "city": "New York",
                      "zip": "10001"
                    }
                  }
                }
                """;

        String json2 = """
                {
                  "person": {
                    "name": "John",
                    "age": 26,
                    "skills": ["coding", "writing"],
                    "address": {
                      "city": "Los Angeles",
                      "zip": "90001"
                    }
                  }
                }
                """;

        String diff = jsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at person.age: 25 vs 26"));
        assertTrue(diff.contains("Value mismatch at person.skills[1]: \"design\" vs \"writing\""));
        assertTrue(diff.contains("Value mismatch at person.address.city: \"New York\" vs \"Los Angeles\""));
        assertTrue(diff.contains("Value mismatch at person.address.zip: \"10001\" vs \"90001\""));
        assertFalse(diff.contains("Missing in second JSON"));
        assertFalse(diff.contains("Extra in second JSON"));
    }
}
