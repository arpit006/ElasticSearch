package com.demo.elasticsearch.business;

import com.demo.elasticsearch.framework.controller.ElasticRestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author <a href = "mailto: iarpitsrivastava06@gmail.com"> Arpit Srivastava</a>
 */
@RestController
@RequestMapping("/elasticapi")
public class BusinessController extends ElasticRestController<String> {

    private IBusinessService iBusinessService;

    BusinessController(IBusinessService iBusinessService) {
        super(iBusinessService);
        this.iBusinessService = iBusinessService;
    }

    @GetMapping("/abc")
    public String getString() {
        return "API is working in com.demo.elastickafka.business Controller......";
    }
}
