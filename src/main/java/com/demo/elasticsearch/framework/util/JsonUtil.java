package com.demo.elasticsearch.framework.util;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public class JsonUtil {

    private static ObjectMapper objectMapper = new ObjectMapper();

    static {
        objectMapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
        objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        objectMapper.configure(JsonParser.Feature.ALLOW_COMMENTS, true);
        objectMapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
    }

    private static ObjectMapper getObjectMapper() {
        return objectMapper;
    }

    public static <T> T fromJson(String inJson, TypeReference<T> inTypeReference) {
        T t = null;
        try {
            t = getObjectMapper().readValue(inJson, new TypeReference<T>() {
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return t;
    }

    public static String toJson(Object inObject) {
        String s = null;
        try {
            s = getObjectMapper().writeValueAsString(inObject);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return s;
    }
}
