package edith.task;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for parsing JSON strings into key-value pairs.
 * Provides common JSON parsing functionality shared across task types.
 */
public class JsonParser {

    /**
     * Parses a JSON string into a map of key-value pairs.
     * Handles the common JSON parsing logic shared across all task types.
     *
     * @param jsonLine the JSON string to parse
     * @return map containing parsed key-value pairs
     */
    public static Map<String, String> parseJsonToMap(String jsonLine) {
        String json = jsonLine.trim();
        String content = json.substring(1, json.length() - 1);
        String[] pairs = content.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");

        Map<String, String> fieldMap = new HashMap<>();

        for (String pair : pairs) {
            String[] keyValue = pair.split(":", 2);
            if (keyValue.length != 2) {
                continue;
            }

            String key = keyValue[0].trim().replace("\"", "");
            String value = keyValue[1].trim();
            fieldMap.put(key, value);
        }

        return fieldMap;
    }

    /**
     * Extracts and unescapes a string value from a JSON value.
     * Handles the common pattern of removing quotes and unescaping JSON strings.
     *
     * @param jsonValue the JSON value (including quotes)
     * @return the unescaped string value, or empty string if null/invalid
     */
    public static String extractStringValue(String jsonValue) {
        if (jsonValue == null || jsonValue.length() < 2) {
            return "";
        }
        return Task.unescapeJson(jsonValue.substring(1, jsonValue.length() - 1));
    }

    /**
     * Extracts a boolean value from a JSON value.
     *
     * @param jsonValue the JSON value containing a boolean
     * @return the parsed boolean value, or false if null/invalid
     */
    public static boolean extractBooleanValue(String jsonValue) {
        if (jsonValue == null) {
            return false;
        }
        return Boolean.parseBoolean(jsonValue);
    }
}