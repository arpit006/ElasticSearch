package com.demo.elasticsearch.framework.repository;

import com.demo.elasticsearch.bean.DummyObject;

import java.util.List;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public interface IElasticRepository<T> {

    T save(T inJson, DummyObject obj);

    T get(String index, String id);

//    T getSpecificFields(String index, String id, String[] includeFields);

    T checkIfDocumentExists(String index, String id);

    T deleteIfDocumentExists(String index, String id);

    T update(String index, String id, T inJson);

    T bulkInsert(List<DummyObject> dummyObject);

    T bulkUpdate(List<DummyObject> dummyObjects);

    T multiGetRequest(List<T> id);

    T searchApi(String inFieldName, String inValue);
}
