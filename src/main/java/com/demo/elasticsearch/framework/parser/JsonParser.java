package com.demo.elasticsearch.framework.parser;

import com.demo.elasticsearch.bean.DummyObject;
import com.demo.elasticsearch.framework.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public class JsonParser {

    /**
     * This is a simpleParser written just to extract a Key from the incoming JSON which will be used for indexing.
     *
     * @param inJson Json
     * @return T
     */
    public static DummyObject parseJson(String inJson) {
        Map<String, Object> jsonObject = JsonUtil.fromJson(inJson, new TypeReference<Map<String, Object>>() {
        });
        String key = (String) jsonObject.get("key");
        String value = (String) jsonObject.get("index");
        int version = (int) jsonObject.get("version");
        return new DummyObject(key, value, version, inJson);

    }

    public static List<String> parseStringToList(String inString) {
        return Arrays.asList(inString.split(","));
    }
}
