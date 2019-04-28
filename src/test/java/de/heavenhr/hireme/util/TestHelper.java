package de.heavenhr.hireme.util;

import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.format.ISODateTimeFormat;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public enum TestHelper {
    INSTANCE;
    
    private Gson gson;
    
    TestHelper() {

            JsonDeserializer<DateTime> deserializer = new JsonDeserializer<DateTime>() {
                @Override
                public DateTime deserialize(JsonElement json,
                                            Type typeOfT,
                                            JsonDeserializationContext context) throws JsonParseException {
                    return json == null ? null : ISODateTimeFormat.dateTimeParser()
                            .withOffsetParsed()
                            .parseDateTime(json.getAsString());
                }
            };

            JsonSerializer<DateTime> dateTimeSerializer = new JsonSerializer<DateTime>() {
                @Override
                public JsonElement serialize(DateTime src,
                                             Type typeOfSrc,
                                             JsonSerializationContext context) {
                    return new JsonPrimitive(src.toString());
                }
            };

            gson = new GsonBuilder()
                    .registerTypeAdapter(DateTime.class, deserializer)
                    .registerTypeAdapter(DateTime.class, dateTimeSerializer)
                    .create();
    }
    
    public <T> String convertToJsonString(final T obj, final Type type) {
        return gson.toJson(obj, type);
    }
    
    public <T> JSONArray convertListToJsonArray(final List<T> list) throws JSONException {
        final JSONArray jsonArray = new JSONArray();
        for (T t : list) {
            final String jsonString = gson.toJson(t);
            jsonArray.put(new JSONObject(jsonString));
        }
        
        return jsonArray;
    }
    
    public <T> List<T> convertJsonArrayToList(final JSONArray jsonArray, final Type type) throws JsonSyntaxException, JSONException {
        final List<T> list = new ArrayList<>(jsonArray.length());
        for (int i = 0; i < jsonArray.length(); i++) {
            final T t = gson.fromJson(jsonArray.get(i).toString(), type);
            list.add(t);
        }
        
        return list;
    }
    
    public <T> T convertJsonToObject(final JSONObject jsonObject, final Type type) {
        return gson.fromJson(jsonObject.toString(), type);
    }
    
    public <T> String convertObjectToJson(final T obj, final Type type) {
        return gson.toJson(obj, type);
    }

    public <T> T getJsonFromResource(String inputFile, final Type type) {
        JsonReader reader = new JsonReader(new InputStreamReader(this.getClass().getResourceAsStream(inputFile)));
        return gson.fromJson(reader, type);
    }
    
    public <T> T getJsonFromString(String input, final Type type) {
        return gson.fromJson(input, type);
    }
}
