package jsondiff;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

public class JsonDiff {

    private final ObjectMapper objectMapper;

    public JsonDiff() {
        this.objectMapper = new ObjectMapper();
    }

    public JsonNode parseJson(String jsonString) throws IOException {
        return objectMapper.readTree(jsonString);
    }

    private void compareNodes(JsonNode node1, JsonNode node2, String path, StringBuilder diffResult) {
        Iterator<Map.Entry<String, JsonNode>> firstJsonFields = node1.fields();

        while (firstJsonFields.hasNext()) {
            Map.Entry<String, JsonNode> field = firstJsonFields.next();
            String fieldName = field.getKey();
            JsonNode value1 = field.getValue();
            JsonNode value2 = node2.get(fieldName);

            String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;

            if (value2 == null) {
                diffResult.append("Missing in second JSON: ").append(currentPath).append("\n");
            } else if (value1.isObject() && value2.isObject()) {
                compareNodes(value1, value2, currentPath, diffResult);
            } else if (!value1.equals(value2)) {
                diffResult.append("Value mismatch at ").append(currentPath)
                        .append(": ").append(value1).append(" vs ").append(value2).append("\n");
            }
        }

        Iterator<Map.Entry<String, JsonNode>> secondJsonFields = node2.fields();
        while (secondJsonFields.hasNext()) {
            Map.Entry<String, JsonNode> field = secondJsonFields.next();
            String fieldName = field.getKey();
            if (!node1.has(fieldName)) {
                String currentPath = path.isEmpty() ? fieldName : path + "." + fieldName;
                diffResult.append("Extra in second JSON: ").append(currentPath).append("\n");
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
}
