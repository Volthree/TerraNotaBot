package vladislavmaltsev.terranotabot.util.JsonData;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import vladislavmaltsev.terranotabot.mapgeneration.map.TerraNotaMap;

public class JsonParser {
    private JsonParser(){}

    public static String toJson(TerraNotaMap terraNotaMap) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.writeValueAsString(terraNotaMap);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static TerraNotaMap fromJson(String json) {
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(json, TerraNotaMap.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
