package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;

import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class ProductControllerTest {
    @Mock
    ProductDao productDao;
    @InjectMocks
    ProductController productController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testListeProduits() {
        MappingJacksonValue result = productController.listeProduits();
        Assertions.assertEquals(null, result);
    }

    @Test
    void testAfficherUnProduit() {
        when(productDao.findById(anyInt())).thenReturn(new Product(0, "nom", 0, 0));

        Product result = productController.afficherUnProduit(0);
        Product p = new Product(0, "nom", 0, 0);

        Assertions.assertEquals(result.getId(), p.getId());
        Assertions.assertSame(p.getNom(), result.getNom());
        Assertions.assertEquals(result.getPrix(), p.getPrix());
        Assertions.assertEquals(result.getPrixAchat(), p.getPrixAchat());
    }

    @Test
    void testAjouterProduit() {
        ResponseEntity<Void> result = productController.ajouterProduit(new Product(0, null, 10, 0));
        Assertions.assertTrue(result.getStatusCodeValue() >= 200);
        Assertions.assertTrue( result.getStatusCodeValue() < 300);
    }

    @Test
    void testSupprimerProduit() {
        productController.supprimerProduit(0);
    }

    @Test
    void testUpdateProduit() {
        productController.updateProduit(new Product(0, null, 10, 0));
    }

    @Test
    void testTriParOrdreAlphabetique() {
        when(productDao.findByOrderByNomAsc()).thenReturn(Arrays.<Product>asList(new Product(0, "nom", 0, 0)));

        List<Product> result = productController.triParOrdreAlphabetique();
        List<Product> p = Arrays.<Product>asList(new Product(0, "nom", 0, 0));

        Assertions.assertEquals(p.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(p.get(0).getNom(), result.get(0).getNom());
        Assertions.assertEquals(p.get(0).getPrix(), result.get(0).getPrix());
        Assertions.assertEquals(p.get(0).getPrixAchat(), result.get(0).getPrixAchat());
    }

    @Test
    void testTesteDeRequetes() {
        when(productDao.chercherUnProduitCher(anyInt())).thenReturn(Arrays.<Product>asList(new Product(0, "nom", 0, 0)));

        List<Product> result = productController.testeDeRequetes(0);
        List<Product> p = Arrays.<Product>asList(new Product(0, "nom", 0, 0));

        Assertions.assertEquals(p.get(0).getId(), result.get(0).getId());
        Assertions.assertEquals(p.get(0).getNom(), result.get(0).getNom());
        Assertions.assertEquals(p.get(0).getPrix(), result.get(0).getPrix());
        Assertions.assertEquals(p.get(0).getPrixAchat(), result.get(0).getPrixAchat());
    }

    @Test
    void testCalculerMargeProduit() {
        String result = productController.calculerMargeProduit();
        Assertions.assertEquals(10, result);
    }
}

//Generated with love by TestMe :) Please report issues and submit feature requests at: http://weirddev.com/forum#!/testme