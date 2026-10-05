package com.ecommerce.service;

import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class ProductService {

    private final List<Product> products;

    public ProductService() {
        products = new ArrayList<>();
    }

    // =========================================================
    // ADD PRODUCT
    // =========================================================

    public synchronized boolean addProduct(
            Product product) {

        if (product == null) {

            System.out.println(
                    "Invalid product."
            );

            return false;
        }

        if (product.getProductName() == null
                || product.getProductName().isBlank()) {

            System.out.println(
                    "Product name cannot be empty."
            );

            return false;
        }

        if (product.getPrice() < 0) {

            System.out.println(
                    "Product price cannot be negative."
            );

            return false;
        }

        if (product.getQuantity() < 0) {

            System.out.println(
                    "Product quantity cannot be negative."
            );

            return false;
        }

        if (findProductById(
                product.getProductId()
        ) != null) {

            System.out.println(
                    "Product ID already exists."
            );

            return false;
        }

        products.add(product);
        AuditLogService.getInstance().log(
                "ADMIN",
                "Added product #" + product.getProductId()
                        + " - " + product.getProductName()
        );

        System.out.println(
                "Product added successfully."
        );

        return true;
    }

    // =========================================================
    // FIND PRODUCT
    // =========================================================

    public synchronized Product findProductById(
            int productId) {

        for (Product product :
                products) {

            if (product != null
                    && product.getProductId()
                    == productId) {

                return product;
            }
        }

        return null;
    }

    // =========================================================
    // UPDATE PRODUCT - FULL DETAILS
    // =========================================================

    public synchronized boolean updateProduct(
            int productId,
            String productName,
            String category,
            double price,
            int quantity,
            String brand) {

        Product product =
                findProductById(productId);

        if (product == null) {

            System.out.println(
                    "Product not found."
            );

            return false;
        }

        if (productName == null
                || productName.isBlank()) {

            System.out.println(
                    "Product name cannot be empty."
            );

            return false;
        }

        if (price < 0) {

            System.out.println(
                    "Product price cannot be negative."
            );

            return false;
        }

        if (quantity < 0) {

            System.out.println(
                    "Product quantity cannot be negative."
            );

            return false;
        }

        product.setProductName(
                productName.trim()
        );

        product.setCategory(
                category == null
                        ? ""
                        : category.trim()
        );

        product.setPrice(price);

        product.setQuantity(quantity);

        product.setBrand(
                brand == null
                        ? ""
                        : brand.trim()
        );

        AuditLogService.getInstance().log(
                "ADMIN",
                "Updated product #" + productId
        );

        System.out.println(
                "Product updated successfully."
        );

        return true;
    }

    // =========================================================
    // UPDATE PRODUCT - OBJECT
    // =========================================================

    public synchronized boolean updateProduct(
            Product updatedProduct) {

        if (updatedProduct == null) {

            System.out.println(
                    "Invalid product."
            );

            return false;
        }

        return updateProduct(
                updatedProduct.getProductId(),
                updatedProduct.getProductName(),
                updatedProduct.getCategory(),
                updatedProduct.getPrice(),
                updatedProduct.getQuantity(),
                updatedProduct.getBrand()
        );
    }

    // =========================================================
    // REMOVE PRODUCT
    // =========================================================

    public synchronized boolean removeProduct(
            int productId) {

        Product product =
                findProductById(productId);

        if (product == null) {

            System.out.println(
                    "Product not found."
            );

            return false;
        }

        products.remove(product);
        AuditLogService.getInstance().log(
                "ADMIN",
                "Removed product #" + productId
        );

        System.out.println(
                "Product removed successfully."
        );

        return true;
    }

    // =========================================================
    // SEARCH BY NAME
    // =========================================================

    public synchronized List<Product> searchProducts(
            String keyword) {

        List<Product> results =
                new ArrayList<>();

        if (keyword == null
                || keyword.isBlank()) {

            return results;
        }

        String searchText =
                keyword.trim().toLowerCase();

        for (Product product :
                products) {

            if (product == null) {
                continue;
            }

            String name =
                    product.getProductName();

            if (name != null
                    && name.toLowerCase()
                    .contains(searchText)) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // SEARCH BY NAME / CATEGORY / BRAND
    // =========================================================

    public synchronized List<Product>
    searchProducts(
            String name,
            String category,
            String brand) {

        List<Product> results =
                new ArrayList<>();

        for (Product product :
                products) {

            if (product == null) {
                continue;
            }

            boolean matchesName =
                    name == null
                            || name.isBlank()
                            || containsIgnoreCase(
                            product.getProductName(),
                            name
                    );

            boolean matchesCategory =
                    category == null
                            || category.isBlank()
                            || containsIgnoreCase(
                            product.getCategory(),
                            category
                    );

            boolean matchesBrand =
                    brand == null
                            || brand.isBlank()
                            || containsIgnoreCase(
                            product.getBrand(),
                            brand
                    );

            if (matchesName
                    && matchesCategory
                    && matchesBrand) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // FILTER BY CATEGORY
    // =========================================================

    public synchronized List<Product>
    getProductsByCategory(
            String category) {

        List<Product> results =
                new ArrayList<>();

        if (category == null
                || category.isBlank()) {

            return results;
        }

        for (Product product :
                products) {

            if (product != null
                    && containsIgnoreCase(
                    product.getCategory(),
                    category)) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // FILTER BY BRAND
    // =========================================================

    public synchronized List<Product>
    getProductsByBrand(
            String brand) {

        List<Product> results =
                new ArrayList<>();

        if (brand == null
                || brand.isBlank()) {

            return results;
        }

        for (Product product :
                products) {

            if (product != null
                    && containsIgnoreCase(
                    product.getBrand(),
                    brand)) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // FILTER BY PRICE RANGE
    // =========================================================

    public synchronized List<Product>
    getProductsByPriceRange(
            double minimumPrice,
            double maximumPrice) {

        List<Product> results =
                new ArrayList<>();

        if (minimumPrice < 0
                || maximumPrice < minimumPrice) {

            return results;
        }

        for (Product product :
                products) {

            if (product == null) {
                continue;
            }

            double price =
                    product.getPrice();

            if (price >= minimumPrice
                    && price <= maximumPrice) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // SORT BY PRICE - LOW TO HIGH
    // =========================================================

    public synchronized List<Product>
    sortByPriceLowToHigh() {

        List<Product> sortedProducts =
                new ArrayList<>(products);

        sortedProducts.sort(
                Comparator.comparingDouble(
                        Product::getPrice
                )
        );

        return sortedProducts;
    }

    // =========================================================
    // SORT BY PRICE - HIGH TO LOW
    // =========================================================

    public synchronized List<Product>
    sortByPriceHighToLow() {

        List<Product> sortedProducts =
                new ArrayList<>(products);

        sortedProducts.sort(
                Comparator.comparingDouble(
                        Product::getPrice
                ).reversed()
        );

        return sortedProducts;
    }

    // =========================================================
    // SORT BY NAME
    // =========================================================

    public synchronized List<Product>
    sortByName() {

        List<Product> sortedProducts =
                new ArrayList<>(products);

        sortedProducts.sort(
                Comparator.comparing(
                        product ->
                                product == null
                                        || product.getProductName() == null
                                        ? ""
                                        : product.getProductName(),
                        String.CASE_INSENSITIVE_ORDER
                )
        );

        return sortedProducts;
    }

    // =========================================================
    // CATEGORY HIERARCHY HELPERS
    // =========================================================

    public synchronized List<Product> filterBySubcategory(
            List<Product> source, String subcategory) {
        if (source == null) return new ArrayList<>();
        if (subcategory == null || subcategory.isBlank()
                || subcategory.equalsIgnoreCase("All Subcategories")) {
            return new ArrayList<>(source);
        }
        List<Product> result = new ArrayList<>();
        for (Product product : source) {
            if (product != null && product.getSubcategory() != null
                    && product.getSubcategory().equalsIgnoreCase(subcategory.trim())) {
                result.add(product);
            }
        }
        return result;
    }

    public synchronized List<String> getCategories() {
        return products.stream()
                .filter(p -> p != null && p.getCategory() != null && !p.getCategory().isBlank())
                .map(Product::getCategory).distinct().sorted().toList();
    }

    public synchronized List<String> getSubcategories(String category) {
        return products.stream()
                .filter(p -> p != null && p.getSubcategory() != null && !p.getSubcategory().isBlank()
                        && (category == null || category.isBlank()
                            || p.getCategory().equalsIgnoreCase(category)))
                .map(Product::getSubcategory).distinct().sorted().toList();
    }

    public synchronized List<String> getBrands(String category, String subcategory) {
        return products.stream()
                .filter(p -> p != null && p.getBrand() != null && !p.getBrand().isBlank()
                        && (category == null || category.isBlank()
                            || p.getCategory().equalsIgnoreCase(category))
                        && (subcategory == null || subcategory.isBlank()
                            || p.getSubcategory().equalsIgnoreCase(subcategory)))
                .map(Product::getBrand).distinct().sorted().toList();
    }

    // =========================================================
    // GET ALL PRODUCTS
    // =========================================================

    public synchronized List<Product>
    getProducts() {

        return Collections.unmodifiableList(
                new ArrayList<>(
                        products
                )
        );
    }

    // =========================================================
    // DISPLAY ALL PRODUCTS
    // =========================================================

    public synchronized void displayAllProducts() {

        if (products.isEmpty()) {

            System.out.println(
                    "No products available."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE PRODUCTS ====="
        );

        for (Product product :
                products) {

            if (product == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            System.out.println(product);
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // PRODUCT COUNT
    // =========================================================

    public synchronized int getProductCount() {

        return products.size();
    }

    // =========================================================
    // CHECK PRODUCT EXISTENCE
    // =========================================================

    public synchronized boolean productExists(
            int productId) {

        return findProductById(productId)
                != null;
    }

    // =========================================================
    // HELPER
    // =========================================================

    private boolean containsIgnoreCase(
            String value,
            String searchText) {

        if (value == null
                || searchText == null) {

            return false;
        }

        return value
                .toLowerCase()
                .contains(
                        searchText
                                .trim()
                                .toLowerCase()
                );
    }
}