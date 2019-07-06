package com.demo.elasticsearch.framework.repository;

import com.demo.elasticsearch.bean.DummyObject;
import org.elasticsearch.action.ActionListener;
import org.elasticsearch.action.DocWriteRequest;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.delete.DeleteRequest;
import org.elasticsearch.action.delete.DeleteResponse;
import org.elasticsearch.action.get.GetRequest;
import org.elasticsearch.action.get.GetResponse;
import org.elasticsearch.action.get.MultiGetRequest;
import org.elasticsearch.action.get.MultiGetResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.action.support.WriteRequest;
import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.Strings;
import org.elasticsearch.common.unit.TimeValue;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.elasticsearch.search.fetch.subphase.FetchSourceContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
public class ElasticHighLevelRepository<T> implements IElasticRepository<T> {

    private Logger LOGGER = LoggerFactory.getLogger(ElasticHighLevelRepository.class);

    private RestHighLevelClient CLIENT;

    private T RESPONSE = (T) "Document given to Elasticsearch to be processed.";

    public ElasticHighLevelRepository(RestHighLevelClient client) {
        this.CLIENT = client;
    }

    /**
     * This method saves the document at the given index(DB), indices(Key), type(Table) #deprecated.
     * Elasticsearch maintains it's own version to create and update data.
     *
     * @param inJson T
     * @param obj    DummyObject :- got from parsing JSON
     * @return T
     */
    @Override
    public T save(T inJson, DummyObject obj) {
        IndexRequest indexRequest = new IndexRequest()
                .index(obj.getIndex())
                .id(obj.getKey())
                .source(inJson, XContentType.JSON)
                .timeout(TimeValue.timeValueSeconds(1))
//                .version(obj.getVersion())
//                .versionType(VersionType.INTERNAL)
                .opType(DocWriteRequest.OpType.INDEX);


        CLIENT.indexAsync(indexRequest, RequestOptions.DEFAULT, new ActionListener<IndexResponse>() {
            @Override
            public void onResponse(IndexResponse indexResponse) {
                LOGGER.info("The record has been saved to database::" +
                        "\nID:-> " + indexResponse.getId() +
                        "\nINDEX:-> " + indexResponse.getIndex() +
                        "\nRESULT:-> " + indexResponse.getResult() +
                        "\nVERSION:-> " + indexResponse.getVersion() +
                        "\nSHARD:-> " + indexResponse.getShardInfo()
                );
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("Could not save this record into database because, " + e);
            }
        });
        return (T) "Document given to Elasticsearch to be processed.";
    }

    @Override
    public T get(String index, String id) {
        GetRequest getRequest = new GetRequest()
                .index(index)
                .id(id)
                .fetchSourceContext(FetchSourceContext.FETCH_SOURCE)
                .refresh(true);
//                .version(1)
//                .versionType(VersionType.EXTERNAL);

        CLIENT.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse documentFields) {
                LOGGER.info("Record fetched from Database:: " +
                        "\nID:-> " + documentFields.getId() +
                        "\nINDEX:-> " + documentFields.getIndex() +
                        "\nSOURCE:-> " + documentFields.getSourceAsString() +
                        "\nTYPE:-> " + documentFields.getType() +
                        "\nFIELDS:-> " + documentFields.getFields() +
                        "\nSEQUENCE:-> " + documentFields.getSeqNo());
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("No record in the database for the given id exists. \n" + e);
            }
        });
        return (T) "Document given to Elasticsearch to be processed.";
    }

    /*@Override
    public T getSpecificFields(String index, String id, String[] includeFields) {
//        String[] excludeFields = new String[]{"designation", "company"};
        String[] excludeFields = Strings.EMPTY_ARRAY;
        FetchSourceContext fetchSourceContext = new FetchSourceContext(true, includeFields, excludeFields);
        GetRequest getRequest = new GetRequest()
                .index(index)
                .id(id)
                .fetchSourceContext(fetchSourceContext)
                .refresh(true);
//                .storedFields("name", "age", "key", "designation", "company", "version", "index");

        CLIENT.getAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<GetResponse>() {
            @Override
            public void onResponse(GetResponse documentFields) {
                LOGGER.info("Record fetched from Database:: " +
                        "\nINDEX:-> " + documentFields.getIndex() +
                        "\nSOURCE:-> " + documentFields.getSourceAsString() +
                        "\nID:-> " + documentFields.getId() +
                        "\nTYPE:-> " + documentFields.getType() +
                        "\nFIELDS:-> " + documentFields.getFields() +
                        "\nSEQUENCE:-> " + documentFields.getSeqNo());
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("No record in the database for the given id exists. \n" + e);
            }
        });
        return (T) "Document given to Elasticsearch to be processed.";
    }*/

    @Override
    public T checkIfDocumentExists(String index, String id) {
        GetRequest getRequest = new GetRequest()
                .index(index)
                .id(id)
                .fetchSourceContext(FetchSourceContext.DO_NOT_FETCH_SOURCE)
                .storedFields(Strings.EMPTY_ARRAY);

        CLIENT.existsAsync(getRequest, RequestOptions.DEFAULT, new ActionListener<Boolean>() {
            @Override
            public void onResponse(Boolean aBoolean) {
                if (aBoolean) {
                    LOGGER.info("The queried record exists in ElasticSearch Database at index :- " + index);
                } else {
                    LOGGER.error("No record found with id = " + id + " in ElasticSearch Database at index :- " + index);
                }
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("No record found with id = " + id + " in ElasticSearch Database at index :- " + index
                        + "\nError:-> " + e);
            }
        });
        return (T) "Document given to Elasticsearch to be processed.";
    }

    @Override
    public T deleteIfDocumentExists(String index, String id) {
        DeleteRequest deleteRequest = new DeleteRequest()
                .index(index)
                .id(id)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL)
                .timeout(TimeValue.timeValueMinutes(1));

        CLIENT.deleteAsync(deleteRequest, RequestOptions.DEFAULT, new ActionListener<DeleteResponse>() {
            @Override
            public void onResponse(DeleteResponse deleteResponse) {
                LOGGER.info("Document at" +
                        "\nINDEX:-> " + deleteResponse.getIndex() +
                        "\nID:-> " + deleteResponse.getId() +
                        "\nRESULT:-> " + deleteResponse.getResult() +
                        "\nSucessfully Deleted!");
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("Error occurred while deleting document at index : " + index + "and id : " + id);
            }
        });
        return (T) "Document given to Elasticsearch to be processed.";
    }

    public T update(String index, String id, T inJson) {
        IndexRequest indexRequest = new IndexRequest()
                .index(index)
                .id(id)
                .source(inJson, XContentType.JSON)
                .opType(DocWriteRequest.OpType.INDEX)
                .timeout(TimeValue.timeValueMillis(100));
        UpdateRequest updateRequest = new UpdateRequest()
                .index(index)
                .id(id)
                .docAsUpsert(true)
                .doc(indexRequest)
                .upsert(indexRequest)
                .fetchSource(true)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        CLIENT.updateAsync(updateRequest, RequestOptions.DEFAULT, new ActionListener<UpdateResponse>() {
            @Override
            public void onResponse(UpdateResponse updateResponse) {
                LOGGER.info("The record has been updated in Database" +
                        "\nID:-> " + updateResponse.getId() +
                        "\nINDEX:-> " + updateResponse.getIndex() +
                        "\nRESULT:-> " + updateResponse.getResult() +
                        "\nSOURCE:-> " + updateResponse.getGetResult().getSource().toString());
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("Could not update this document in Index " + index + " with Id " + id +
                        "\nERROR : " + e);
            }
        });
        return (T) "Document given to Elasticsearch to be processed.";
    }

    //TODO: TermVectorsAPI

    @Override
    public T bulkInsert(List<DummyObject> dummyObject) {
        List<DocWriteRequest<?>> docWriteRequestList = dummyObject
                .stream()
                .map(t -> new IndexRequest()
                        .index(t.getIndex())
                        .id(t.getKey())
                        .source(t.getInJson(), XContentType.JSON)
                        .opType(DocWriteRequest.OpType.INDEX))
                .collect(Collectors.toList());

        BulkRequest bulkRequest = new BulkRequest()
                .add(docWriteRequestList)
                .setRefreshPolicy(WriteRequest.RefreshPolicy.WAIT_UNTIL);

        CLIENT.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                Arrays.stream(bulkItemResponses.getItems()).forEach(t -> LOGGER.info("Bulk Insert Responses: " +
                        "\nID:-> " + t.getId() +
                        "\nINDEX:-> " + t.getIndex() +
                        "\nRESPONSE:-> " + t.getResponse() +
                        "\nVERSION:-> " + t.getVersion()));
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("Could not process Bulk Insert." +
                        "\nERROR:-> " + e);
            }
        });

        return (T) "Document given to Elasticsearch to be processed.";
    }

    @Override
    public T bulkUpdate(List<DummyObject> dummyObjects) {
        List<DocWriteRequest<?>> docWriteRequestList = dummyObjects
                .stream()
                .map(t -> {
                    IndexRequest indexRequest = new IndexRequest()
                            .index(t.getIndex())
                            .id(t.getKey())
                            .source(t.getInJson(), XContentType.JSON)
                            .opType(DocWriteRequest.OpType.INDEX);
                    return new UpdateRequest()
                            .index(t.getIndex())
                            .id(t.getKey())
                            .docAsUpsert(true)
                            .doc(indexRequest)
                            .upsert(indexRequest)
                            .fetchSource(true);
                })
                .collect(Collectors.toList());
        BulkRequest bulkRequest = new BulkRequest();
        bulkRequest.add(docWriteRequestList);
        CLIENT.bulkAsync(bulkRequest, RequestOptions.DEFAULT, new ActionListener<BulkResponse>() {
            @Override
            public void onResponse(BulkResponse bulkItemResponses) {
                Arrays.stream(bulkItemResponses.getItems()).forEach(t -> LOGGER.info("Bulk Insert Responses: " +
                        "\nID:-> " + t.getId() +
                        "\nRESPONSE:-> " + t.getResponse() +
                        "\nINDEX:-> " + t.getIndex() +
                        "\nVERSION:-> "));
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("Could not process Bulk Insert." +
                        "\nERROR:-> " + e);
            }
        });
        return RESPONSE;
    }

    @Override
    public T multiGetRequest(List<T> id) {
        MultiGetRequest multiGetRequest = new MultiGetRequest();
        id.forEach(t -> multiGetRequest.add(
                new MultiGetRequest.Item("info", String.valueOf(t))
                        .fetchSourceContext(FetchSourceContext.FETCH_SOURCE)));
        CLIENT.mgetAsync(multiGetRequest, RequestOptions.DEFAULT, new ActionListener<MultiGetResponse>() {
            @Override
            public void onResponse(MultiGetResponse multiGetItemResponses) {
                Arrays.stream(multiGetItemResponses.getResponses()).forEach(t ->
                        LOGGER.info("#### Multi Get Response : " +
                                "\nID : -> " + t.getId() +
                                "\nINDEX : -> " + t.getIndex() +
                                "\nSOURCE : -> " + t.getResponse().getSource() +
                                "\nFIELDS : -> " + t.getResponse().getFields()));
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("###Could not perform Multi Get on this Index...." +
                        "\nERROR : -> " + e);
            }
        });
        return RESPONSE;
    }

    // delete by Query,  Update by query,  throttle api

    @Override
    public T searchApi(String inFieldName, String inValue) {
        SearchRequest searchRequest = new SearchRequest(); // for all indices
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        QueryBuilder queryBuilder = QueryBuilders.termQuery(inFieldName, inValue);

        searchSourceBuilder.query(queryBuilder);

        searchRequest.indices("info");
        searchRequest.source(searchSourceBuilder);

        CLIENT.searchAsync(searchRequest, RequestOptions.DEFAULT, new ActionListener<SearchResponse>() {
            @Override
            public void onResponse(SearchResponse searchResponse) {
                LOGGER.info("#### SEARCH Query Response :" +
                        "\nHITS : -> " + searchResponse.getHits() +
                        "\nTIME TOOK : -> " + searchResponse.getTook() +
                        "\nSTATUS : -> " + searchResponse.status() +
                        "\nSEARCH RESPONSE : -> " + searchResponse.toString());
            }

            @Override
            public void onFailure(Exception e) {
                LOGGER.error("Could not perform Search Request on this index..." +
                        "\nERROR :-> " + e);
            }
        });
        return RESPONSE;
    }

}
