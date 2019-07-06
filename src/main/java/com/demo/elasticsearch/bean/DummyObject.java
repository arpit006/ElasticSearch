package com.demo.elasticsearch.bean;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
@Data
@AllArgsConstructor
public class DummyObject {

    String key;

    String index;

    int version;

    Object inJson;
}
