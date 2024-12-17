package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * Utility class for comparing JSON objects and providing detailed differences.
 *
 * Supports the following features:
 * * Compare two JSON strings or JsonNode objects.
 * * Pretty-print the differences with highlighted changes.
 * * Check equality and subset relationships between JSONs.
 *
 * Author: Nikhil Pachpande
 * Version: 1.0
 */
public class JsonDiff {

    private final ObjectMapper objectMapper;

    // Using the ANSI escape codes for colorizing the output
    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m"; // For mismatches in the provided JSON strings or JsonNodes
    private static final String YELLOW = "\u001B[33m"; // For missing fields from the JSON strings or JsonNodes
    private static final String GREEN = "\u001B[32m"; // For extra fields in the JSON strings or JsonNodes

    /**
     * The Constructor initializes the ObjectMapper for JSON processing.
     */
    public JsonDiff() {
        this.objectMapper = new ObjectMapper();
    }

    /**
     * This method parses a JSON string into a JsonNode object.
     *
     * @param jsonString The JSON string to parse.
     * @return A JsonNode representing the parsed JSON.
     * @throws IOException If the input string is not a valid JSON.
     */
    private JsonNode parseJson(String jsonString) throws IOException {
        return objectMapper.readTree(jsonString);
    }

    /**
     * This method compares two JsonNode objects and appends the differences to the result.
     *
     * @param node1      The first JsonNode to compare.
     * @param node2      The second JsonNode to compare.
     * @param path       The current JSON path (for nested objects).
     * @param diffResult The StringBuilder to append the differences to.
     */
    private void compareNodes(JsonNode node1, JsonNode node2, String path, StringBuilder diffResult) {
        if (node1.isArray() && node2.isArray()) {
            compareArrays(node1, node2, path, diffResult);
        } else if (node1.isObject() && node2.isObject()) {
            compareObjects(node1, node2, path, diffResult);
        } else if (!node1.equals(node2)) {
            diffResult.append(indent(path))
                    .append(RED).append("Value mismatch at ").append(path)
                    .append(": ").append(node1).append(" vs ").append(node2).append(RESET).append("\n");
        }
    }

    /**
     * This method compares two JSON objects and appends the differences to the result.
     *
     * @param node1      The first JSON object.
     * @param node2      The second JSON object.
     * @param path       The current JSON path (for nested objects).
     * @param diffResult The StringBuilder to append the differences to.
     */
    private void compareObjects(JsonNode node1, JsonNode node2, String path, StringBuilder diffResult) {
        Iterator<Map.Entry<String, JsonNode>> firstJsonFields = node1.fields();

        while (firstJsonFields.hasNext()) {
            Map.Entry<String, JsonNode> field = firstJsonFields.next();
            String fieldName = field.getKey();
            JsonNode value1 = field.getValue();
            JsonNode value2 = node2.get(fieldName);

            String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;

            if (value2 == null) {
                diffResult.append(indent(currentPath))
                        .append(YELLOW).append("Missing in second JSON: ").append(currentPath).append(RESET).append("\n");
            } else {
                compareNodes(value1, value2, currentPath, diffResult);
            }
        }

        Iterator<Map.Entry<String, JsonNode>> secondJsonFields = node2.fields();
        while (secondJsonFields.hasNext()) {
            Map.Entry<String, JsonNode> field = secondJsonFields.next();
            String fieldName = field.getKey();
            if (!node1.has(fieldName)) {
                String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;
                diffResult.append(indent(currentPath))
                        .append(GREEN).append("Extra in second JSON: ").append(currentPath).append(RESET).append("\n");
            }
        }
    }

    /**
     * This method compares two JSON arrays and appends the differences to the result.
     *
     * @param array1     The first JSON array.
     * @param array2     The second JSON array.
     * @param path       The current JSON path (for nested arrays).
     * @param diffResult The StringBuilder to append the differences to.
     */
    private void compareArrays(JsonNode array1, JsonNode array2, String path, StringBuilder diffResult) {
        int maxLength = Math.max(array1.size(), array2.size());
        for (int i = 0; i < maxLength; i++) {
            String currentPath = path + "[" + i + "]";
            if (i >= array1.size()) {
                diffResult.append(indent(currentPath))
                        .append(GREEN).append("Extra in second JSON: ").append(array2.get(i)).append(RESET).append("\n");
            } else if (i >= array2.size()) {
                diffResult.append(indent(currentPath))
                        .append(YELLOW).append("Missing in second JSON: ").append(array1.get(i)).append(RESET).append("\n");
            } else {
                compareNodes(array1.get(i), array2.get(i), currentPath, diffResult);
            }
        }
    }

    /**
     * This method returns a detailed, pretty-printed diff between two JSON strings.
     *
     * @param json1 The first JSON string.
     * @param json2 The second JSON string.
     * @return A string containing the differences between the two JSONs.
     * @throws IOException If the input strings are not valid JSON.
     */
    public String getDiff(String json1, String json2) throws IOException {
        JsonNode node1 = parseJson(json1);
        JsonNode node2 = parseJson(json2);

        return getDiff(node1, node2);
    }

    /**
     * This method returns a detailed, pretty-printed diff between two JsonNode objects.
     *
     * @param node1 The first JsonNode.
     * @param node2 The second JsonNode.
     * @return A string containing the differences between the two JsonNodes.
     */
    public String getDiff(JsonNode node1, JsonNode node2) {
        StringBuilder diffResult = new StringBuilder();
        compareNodes(node1, node2, "", diffResult);

        return diffResult.toString();
    }

    /**
     * This method checks if two JSON strings are equal.
     *
     * @param json1 The first JSON string.
     * @param json2 The second JSON string.
     * @return True if the JSONs are equal, false otherwise.
     * @throws IOException If the input strings are not valid JSON.
     */
    public boolean isEqual(String json1, String json2) throws IOException {
        JsonNode node1 = parseJson(json1);
        JsonNode node2 = parseJson(json2);
        return isEqual(node1, node2);
    }

    /**
     * This method checks if two JsonNode objects are equal.
     *
     * @param node1 The first JsonNode.
     * @param node2 The second JsonNode.
     * @return True if the JsonNodes are equal, false otherwise.
     */
    public boolean isEqual(JsonNode node1, JsonNode node2) {
        return node1.equals(node2);
    }

    /**
     * This method checks if two JSON strings are not equal.
     *
     * @param json1 The first JSON string.
     * @param json2 The second JSON string.
     * @return True if the JSONs are not equal, false otherwise.
     * @throws IOException If the input strings are not valid JSON.
     */
    public boolean isNotEqual(String json1, String json2) throws IOException {
        return !isEqual(json1, json2);
    }

    /**
     * This method checks if two JsonNode objects are not equal.
     *
     * @param node1 The first JsonNode.
     * @param node2 The second JsonNode.
     * @return True if the JsonNodes are not equal, false otherwise.
     */
    public boolean isNotEqual(JsonNode node1, JsonNode node2) {
        return !isEqual(node1, node2);
    }

    /**
     * This method checks if the first JSON string is a subset of the second JSON string.
     *
     * @param json1 The first JSON string.
     * @param json2 The second JSON string.
     * @return True if JSON1 is a subset of JSON2, false otherwise.
     * @throws IOException If the input strings are not valid JSON.
     */
    public boolean isSubset(String json1, String json2) throws IOException {
        JsonNode node1 = parseJson(json1);
        JsonNode node2 = parseJson(json2);
        return isSubset(node1, node2);
    }

    /**
     * This method checks if the first JsonNode is a subset of the second JsonNode.
     *
     * @param node1 The first JsonNode.
     * @param node2 The second JsonNode.
     * @return True if node1 is a subset of node2, false otherwise.
     */
    public boolean isSubset(JsonNode node1, JsonNode node2) {
        Iterator<Map.Entry<String, JsonNode>> fields = node1.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> field = fields.next();
            String key = field.getKey();
            JsonNode value1 = field.getValue();
            JsonNode value2 = node2.get(key);

            if (value2 == null || !value1.equals(value2)) {
                return false;
            }
        }
        return true;
    }

    /**
     * This method adds indentation for pretty-printed output.
     *
     * @param path The current JSON path.
     * @return A string with proper indentation.
     */
    private String indent(String path) {
        int depth = path.isEmpty() ? 0 : path.split("\\.").length + path.split("\\[").length - 1;
        return "  ".repeat(depth);
    }
}
