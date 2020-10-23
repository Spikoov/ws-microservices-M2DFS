package com.circuitbreaker.circuitbreaker.delegate;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

// Service class
@Service
public class ProductDelegate {
    @Autowired
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "fallback")
    public String callProducts(){
        String request = restTemplate.exchange("http://localhost:9090/Produits"
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<String>() {}).getBody();
        return "Products : " + request;
    }

    @HystrixCommand(fallbackMethod = "fallbackId")
    public String callProductAndGetData(String id){
        String request = restTemplate.exchange("http://localhost:9090/Produit/{id}"
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<String>() {}
                , id).getBody();
        return "Product id: " + id + " | Product details : " + request;
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String callProductAndOrderByName(){
        String request = restTemplate.exchange("http://localhost:9090/ProduitsOrderByName"
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<String>() {}).getBody();
        return "Products sorted by name : " + request;
    }

    @HystrixCommand(fallbackMethod = "fallback")
    public String callProductAndCalculate(){
        String request = restTemplate.exchange("http://localhost:9090/AdminProduits"
                , HttpMethod.GET
                , null
                , new ParameterizedTypeReference<String>() {}).getBody();
        return "Products details : " + request;
    }

    public String fallback(){
        return "Service Indisponible";
    }

    public String fallbackId(String id){
        return "Service Indisponible";
    }

    // this is a bean
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}