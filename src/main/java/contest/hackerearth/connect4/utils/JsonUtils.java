package contest.hackerearth.connect4.utils;

import com.google.gson.Gson;

public class JsonUtils {

    private static final Gson GSON = new Gson();

    public static <T> String get(T t) {
        return GSON.toJson(t);
    }

}
