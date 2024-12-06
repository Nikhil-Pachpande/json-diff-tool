package jsondiff;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonDiffTest {

    @Test
    public void testJsonDiff() throws Exception {
        String json1 = "{\"name\": \"John\", \"age\": 30, \"city\": \"New York\"}";
        String json2 = "{\"name\": \"John\", \"age\": 31, \"country\": \"USA\"}";

        String diff = JsonDiff.getDiff(json1, json2);

        assertTrue(diff.contains("Value mismatch at age"));
        assertTrue(diff.contains("Missing in second JSON: city"));
        assertTrue(diff.contains("Extra in second JSON: country"));
    }
}
