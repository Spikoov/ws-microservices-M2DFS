package com.ecommerce.microcommerce.web.controller;

import com.ecommerce.microcommerce.dao.ProductDao;
import com.ecommerce.microcommerce.model.Product;
import com.ecommerce.microcommerce.web.exceptions.ProduitGratuitException;
import com.ecommerce.microcommerce.web.exceptions.ProduitIntrouvableException;
import com.fasterxml.jackson.databind.ser.FilterProvider;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.ser.impl.SimpleFilterProvider;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJacksonValue;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.validation.Valid;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


@RestController
@Api
public class ProductController {

    @Autowired
    private ProductDao productDao;

    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 401, message = "Unauthorized"),
            @ApiResponse(code = 403, message = "Forbidden Access"),
            @ApiResponse(code = 404, message = "Not Found")
    })

    //Récupérer la liste des produits
    @ApiOperation(
            value = "Get list of products",
            response = Iterable.class,
            tags = "listeProduits"
    )
    @RequestMapping(value = "/Produits", method = RequestMethod.GET)
    public MappingJacksonValue listeProduits() {
        Iterable<Product> produits = productDao.findAll();
        SimpleBeanPropertyFilter monFiltre = SimpleBeanPropertyFilter.serializeAllExcept("prixAchat");
        FilterProvider listDeNosFiltres = new SimpleFilterProvider().addFilter("monFiltreDynamique", monFiltre);
        MappingJacksonValue produitsFiltres = new MappingJacksonValue(produits);
        produitsFiltres.setFilters(listDeNosFiltres);
        return produitsFiltres;
    }


    //Récupérer un produit par son Id
    @ApiOperation(
            value = "Get a product by id",
            response = Product.class,
            tags = "afficherUnProduit"
    )
    @RequestMapping(value = "/Produit/{id}", method = RequestMethod.GET)
    public Product afficherUnProduit(@PathVariable int id ) {
        return productDao.findById(id);
    }




    //ajouter un produit
    @ApiOperation(
            value = "Add a product",
            tags = "ajouterProduit"
    )
    @PostMapping(value = "/Produits")
    public ResponseEntity<Void> ajouterProduit(@Valid @RequestBody Product product) {

        if(product.getPrix() > 0){
            Product productAdded = productDao.save(product);

            if (productAdded == null)
                return ResponseEntity.noContent().build();

            URI location = ServletUriComponentsBuilder
                    .fromCurrentRequest()
                    .path("/{id}")
                    .buildAndExpand(productAdded.getId())
                    .toUri();

            return ResponseEntity.created(location).build();
        }
        else {
            throw new ProduitGratuitException("Le prix de vente ne peux être 0");
        }
    }

    // supprimer un produit
    @ApiOperation(
            value = "Delete a product",
            tags = "supprimerProduit"
    )
    @RequestMapping(value = "/DeleteProduit/{id}", method = RequestMethod.DELETE)
    public void supprimerProduit(@PathVariable int id) {
        productDao.delete(id);
    }

    // Mettre à jour un produit
    @ApiOperation(
            value = "Update a product",
            tags = "updateProduit"
    )
    @PostMapping(value = "/UpdateProduit")
    public void updateProduit(@RequestBody Product product) {
        if(product.getPrix() > 0){
            productDao.save(product);
        }
        else {
            throw new ProduitGratuitException("Le prix de vente ne peut   être 0");
        }
    }

    @ApiOperation(
            value = "Get list of products ordered by name",
            response = Iterable.class,
            tags = "triParOrdreAlphabetique"
    )
    @GetMapping(value = "/ProduitsOrderByName")
    public List<Product> triParOrdreAlphabetique(){
        return productDao.findByOrderByNomAsc();
    }

    //Pour les tests
    @ApiOperation(
            value = "Request test",
            response = Iterable.class,
            tags = "testeDeRequetes"
    )
    @GetMapping(value = "test/produits/{prix}")
    public List<Product>  testeDeRequetes(@PathVariable int prix) {
        return productDao.chercherUnProduitCher(400);
    }

    @ApiOperation(
            value = "Calculate margin",
            response = String.class,
            tags = "calculerMargeProduit"
    )
    @GetMapping(value = "/AdminProduits")
    public String calculerMargeProduit() {
        String response = "{<br>";
        for (Product produit : productDao.findAll()) {
            response += produit + ": " + Integer.toString(produit.getPrix() - produit.getPrixAchat()) + "<br>";
        }
        return response + "}";
    }
}
