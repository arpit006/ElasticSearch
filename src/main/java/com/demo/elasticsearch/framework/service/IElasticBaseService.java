package com.demo.elasticsearch.framework.service;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public interface IElasticBaseService<T> {

    T save(T inJson);

    T get(String index, String id);

    T checkIfDocumentExistsInIndex(String index, String id);

    T deleteIfDocumentExists(String index, String id);

    T update(T inJson, String index, String id);

    T bulkInsert(T inJsonList);

    T bulkUpdate(T inJsonList);

    T multiGetRequest(T ids);

    T searchApi(String inFieldName, String inValue);

//    T getSpecificFields(String index, String id, String[] includeFields);
}
