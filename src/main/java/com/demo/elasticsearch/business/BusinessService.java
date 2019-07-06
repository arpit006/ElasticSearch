package com.demo.elasticsearch.business;

import com.demo.elasticsearch.framework.service.ElasticBaseService;
import org.springframework.stereotype.Service;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
@Service
public class BusinessService extends ElasticBaseService<String> implements IBusinessService {

    private IBusinessRepository iBusinessRepository;

    BusinessService(IBusinessRepository iBusinessRepository) {
        super(iBusinessRepository);
        this.iBusinessRepository = iBusinessRepository;
    }

}
