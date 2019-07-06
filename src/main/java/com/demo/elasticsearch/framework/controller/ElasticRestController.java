package com.demo.elasticsearch.framework.controller;

import com.demo.elasticsearch.framework.service.IElasticBaseService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public abstract class ElasticRestController<T> {

    private Logger LOGGER = LoggerFactory.getLogger(ElasticRestController.class);

    private IElasticBaseService<T> iElasticBaseService;

    public ElasticRestController(IElasticBaseService<T> iElasticBaseService) {
        this.iElasticBaseService = iElasticBaseService;
    }

    @PostMapping(value = "", consumes = "application/json", produces = "application/json")
    public T save(@RequestBody T inJson) {
        return iElasticBaseService.save(inJson);
    }

    @GetMapping(value = "/findById/{index}/{id}", consumes = "application/json", produces = "application/json")
    public T get(@PathVariable String index, @PathVariable String id) {
        return iElasticBaseService.get(index, id);
    }

    /*@PostMapping(value = "/find/{index}/{id}", consumes = "application/json", produces = "application/json")
    public T getSpecificFields(@PathVariable String index, @PathVariable String id, @RequestBody String[] includeFields) {
        return iElasticBaseService.getSpecificFields(index, id, includeFields);
    }*/

    @GetMapping(value = "/exists/{index}/{id}", consumes = "application/json", produces = "application/json")
    public T checkIfDocumentExistsInIndex(@PathVariable String index, @PathVariable String id) {
        return iElasticBaseService.checkIfDocumentExistsInIndex(index, id);
    }

    @DeleteMapping(value = "/delete/{index}/{id}", consumes = "application/json", produces = "application/json")
    public T deleteIfDocumentExists(@PathVariable String index, @PathVariable String id) {
        return iElasticBaseService.deleteIfDocumentExists(index, id);
    }

    @PutMapping(value = "/update/{index}/{id}", consumes = "application/json", produces = "application/json")
    public T update(@RequestBody T inJson, @PathVariable String index, @PathVariable String id) {
        return iElasticBaseService.update(inJson, index, id);
    }

    @PostMapping(value = "/bulkinsert", consumes = "application/json", produces = "application/json")
    public T bulkInsert(@RequestBody T inJsonList) {
        return iElasticBaseService.bulkInsert(inJsonList);
    }
    @PutMapping(value = "/bulkupdate", consumes = "application/json", produces = "application/json")
    public T bulkUpdate(@RequestBody T inJsonList) {
        return iElasticBaseService.bulkUpdate(inJsonList);
    }

    @PostMapping(value = "/multiget", produces = "application/json")
    public T multiGetRequest(@RequestBody T ids) {
        return iElasticBaseService.multiGetRequest(ids);
    }

    @GetMapping(value = "/search/{inFieldName}/{inFieldValue}", produces = "application/json")
    public T searchApi(@PathVariable String inFieldName, @PathVariable String inFieldValue) {
        return iElasticBaseService.searchApi(inFieldName, inFieldValue);
    }

}
