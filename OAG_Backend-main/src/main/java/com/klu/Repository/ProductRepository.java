package com.klu.Repository;

import com.klu.Interfaces.IProductRepository;
import com.klu.Models.Products;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public class ProductRepository implements IProductRepository {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Products> getAllProducts() {
        return entityManager.createQuery("SELECT p FROM Products p", Products.class)
                .getResultList();
    }

    @Override
    public Optional<Products> getProductById(int id) {
        Products product = entityManager.find(Products.class, id);
        return Optional.ofNullable(product);
    }

    @Override
    public void addProduct(Products product) {
        entityManager.persist(product);
    }

    @Override
    public void updateProduct(Products product) {
        entityManager.merge(product);
    }

    @Override
    public void deleteProduct(int id) {
        Products product = entityManager.find(Products.class, id);
        if (product != null) {
            entityManager.remove(product);
        }
    }

    @Override
    public List<Products> findRelatedProducts(String category, String subCategory, int excludeId) {
        return entityManager.createQuery(
                "SELECT p FROM Products p WHERE p.category = :category " +
                        "AND p.subCategory = :subCategory AND p.id <> :excludeId",
                Products.class)
                .setParameter("category", category)
                .setParameter("subCategory", subCategory)
                .setParameter("excludeId", excludeId)
                .getResultList();
    }
}
