package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonDiff {

    private final ObjectMapper objectMapper;

    private static final String RESET = "\u001B[0m";
    private static final String RED = "\u001B[31m"; // For mismatches in both the JSON
    private static final String YELLOW = "\u001B[33m"; // For missing fields
    private static final String GREEN = "\u001B[32m"; // For extra fields

    public JsonDiff() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonNode parseJson(String jsonString) throws IOException {
        return objectMapper.readTree(jsonString);
    }

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

    public String getDiff(String json1, String json2) throws IOException {
        JsonNode node1 = parseJson(json1);
        JsonNode node2 = parseJson(json2);

        return getDiff(node1, node2);
    }

    public String getDiff(JsonNode node1, JsonNode node2) {
        StringBuilder diffResult = new StringBuilder();
        compareNodes(node1, node2, "", diffResult);

        return diffResult.toString();
    }

    public boolean isEqual(String json1, String json2) throws IOException {
        JsonNode node1 = parseJson(json1);
        JsonNode node2 = parseJson(json2);
        return isEqual(node1, node2);
    }

    public boolean isEqual(JsonNode node1, JsonNode node2) {
        return node1.equals(node2);
    }

    public boolean isSubset(String json1, String json2) throws IOException {
        JsonNode node1 = parseJson(json1);
        JsonNode node2 = parseJson(json2);
        return isSubset(node1, node2);
    }

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

    public boolean isNotEqual(String json1, String json2) throws IOException {
        return !isEqual(json1, json2);
    }

    public boolean isNotEqual(JsonNode node1, JsonNode node2) {
        return !isEqual(node1, node2);
    }

    private String indent(String path) {
        int depth = path.isEmpty() ? 0 : path.split("\\.").length + path.split("\\[").length - 1;
        return "  ".repeat(depth);
    }
}
