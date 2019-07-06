package com.demo.elasticsearch.business;

import com.demo.elasticsearch.framework.repository.ElasticHighLevelRepository;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.stereotype.Repository;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
@Repository
public class BusinessRepository extends ElasticHighLevelRepository<String> implements IBusinessRepository {

    private RestHighLevelClient client;

    BusinessRepository(RestHighLevelClient restHighLevelClient) {
        super(restHighLevelClient);
        this.client = restHighLevelClient;
    }
}
