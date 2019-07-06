package com.demo.elasticsearch.framework.parser;

import com.demo.elasticsearch.bean.DummyObject;
import com.demo.elasticsearch.framework.util.JsonUtil;
import com.fasterxml.jackson.core.type.TypeReference;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public class BulkJsonParser {

    public static List<DummyObject> parseListJson(String inJson) {
        List<Map<String, Object>> jsonObjectList = JsonUtil.fromJson(inJson, new TypeReference<List<Map<String, Object>>>() {
        });
        return jsonObjectList
                .stream()
                .map(t -> new DummyObject((String)t.get("key"), (String)t.get("index"), (int)t.get("version"), t))
                .collect(Collectors.toList());
    }
}
