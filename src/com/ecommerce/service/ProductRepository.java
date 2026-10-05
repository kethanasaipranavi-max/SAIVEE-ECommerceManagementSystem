package com.ecommerce.service;

import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.List;

public class ProductRepository {

    private final List<Product> products;

    public ProductRepository() {
        products = new ArrayList<>();
    }

    public void save(Product product) {

        if (product == null) {
            return;
        }

        products.add(product);
    }

    public Product findById(int productId) {

        for (Product product : products) {

            if (product.getProductId() == productId) {
                return product;
            }
        }

        return null;
    }

    public List<Product> findAll() {

        return new ArrayList<>(products);
    }

    public boolean deleteById(int productId) {

        Product product = findById(productId);

        if (product == null) {
            return false;
        }

        products.remove(product);
        return true;
    }

    public int count() {

        return products.size();
    }

    public void clear() {

        products.clear();
    }
}