package com.circuitbreaker.circuitbreaker.controller;

import com.circuitbreaker.circuitbreaker.delegate.ProductDelegate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class CircuitBreakerController {

    @Autowired
    ProductDelegate productDelegate;

    @GetMapping("/Produits")
    public String getProducts() {
        return productDelegate.callProducts();
    }

    @GetMapping("/Produit/{id}")
    public String getProductById(@PathVariable String id) {
        return productDelegate.callProductAndGetData(id);
    }

    @GetMapping("/ProduitsOrderByName")
    public String getProductOrderByName() {
        return productDelegate.callProductAndOrderByName();
    }

    @GetMapping(value = "/AdminProduits")
    public String getProductsAndCalculateData(){
        return productDelegate.callProductAndCalculate();
    }
}