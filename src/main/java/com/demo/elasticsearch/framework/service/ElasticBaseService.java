package com.demo.elasticsearch.framework.service;

import com.demo.elasticsearch.bean.DummyObject;
import com.demo.elasticsearch.framework.parser.BulkJsonParser;
import com.demo.elasticsearch.framework.parser.JsonParser;
import com.demo.elasticsearch.framework.repository.IElasticRepository;

import java.util.List;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public class ElasticBaseService<T> implements IElasticBaseService<T> {

    private IElasticRepository<T> iElasticRepository;

    public ElasticBaseService(IElasticRepository<T> iElasticRepository) {
        this.iElasticRepository = iElasticRepository;
    }


    @Override
    public T save(T inJson) {
        DummyObject dummyObject = JsonParser.parseJson(String.valueOf(inJson));
        return iElasticRepository.save(inJson, dummyObject);
    }

    @Override
    public T get(String index, String id) {
        return iElasticRepository.get(index, id);
    }

    @Override
    public T checkIfDocumentExistsInIndex(String index, String id) {
        return iElasticRepository.checkIfDocumentExists(index, id);
    }

    @Override
    public T deleteIfDocumentExists(String index, String id) {
        return iElasticRepository.deleteIfDocumentExists(index, id);
    }

    @Override
    public T update(T inJson, String index, String id) {
        return iElasticRepository.update(index, id, inJson);
    }

    @Override
    public T bulkInsert(T inJsonList) {
        List<DummyObject> dummyObjectList = BulkJsonParser.parseListJson(String.valueOf(inJsonList));
        return iElasticRepository.bulkInsert(dummyObjectList);
    }

    @Override
    public T bulkUpdate(T inJsonList) {
        List<DummyObject> dummyObjectList = BulkJsonParser.parseListJson(String.valueOf(inJsonList));
        return iElasticRepository.bulkUpdate(dummyObjectList);
    }

    @Override
    public T multiGetRequest(T ids) {
        List<T> id = (List<T>) JsonParser.parseStringToList(String.valueOf(ids));
        return iElasticRepository.multiGetRequest(id);
    }

    @Override
    public T searchApi(String inFieldName, String inValue) {
        return iElasticRepository.searchApi(inFieldName, inValue);
    }

    /*@Override
    public T getSpecificFields(String index, String id, String[] includeFields) {
        return iElasticRepository.getSpecificFields(index, id, includeFields);
    }*/
}
