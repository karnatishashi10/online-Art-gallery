package com.klu.Interfaces;

import com.klu.Models.Products;
import java.util.List;
import java.util.Optional;

public interface IProductRepository {

    List<Products> getAllProducts();

    Optional<Products> getProductById(int id); // return Optional

    void addProduct(Products product);

    void updateProduct(Products product);

    void deleteProduct(int id);

    List<Products> findRelatedProducts(String category, String subCategory, int excludeId);
}
