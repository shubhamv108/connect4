package contest.hackerearth.connect4.models;

import lombok.Data;

import java.util.LinkedHashMap;
import java.util.Map;

@Data
public abstract class Response {

    private Map<String, String> message = new LinkedHashMap<String, String>();
    private boolean isSuccessful;

    public void addAllToMessage(Map<String, String> validationResult) {
        message.putAll(validationResult);
    }

    public void addToMessage(final String key, final String value) {
        message.put(key, value);
    }

}
