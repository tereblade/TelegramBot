package com.example.TelegramBot.api.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.List;

public class JsonConverter {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static <T> T fromJson(String content, Class<T> type) {
        Gson gson = new Gson();
        return gson.fromJson(content, type);
    }

    public static String toJson(Object model) {
        Gson gson = new Gson();
        return gson.toJson(model);
    }

    public static String toJsonWithNullValues(Object model) {
        GsonBuilder builder = new GsonBuilder().serializeNulls();
        Gson gson = builder.create();
        return gson.toJson(model);
    }

    public static String getSingleAttribute(String json, String attributeName) {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return jsonObject.get(attributeName).getAsString();
    }

    public static List<String> getListOfObjects(String json, String attributeName) throws JsonProcessingException {
        JsonObject jsonObject = JsonParser.parseString(json).getAsJsonObject();
        return new ObjectMapper().readValue(jsonObject.get(attributeName).toString(), List.class);
    }

    public static <T> T deserializeResponse(String response, Class<T> clazz) {
        try {
            return objectMapper.readValue(response, clazz);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
