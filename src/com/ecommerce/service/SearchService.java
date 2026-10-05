package com.ecommerce.service;

import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Locale;

public class SearchService {

    // =========================================================
    // SEARCH BY PRODUCT NAME
    // =========================================================

    public List<Product> searchByName(
            List<Product> products,
            String keyword) {

        List<Product> results = new ArrayList<>();

        if (products == null) {
            return results;
        }

        if (keyword == null
                || keyword.trim().isEmpty()) {

            return copyProducts(products);
        }

        String searchText =
                keyword.trim().toLowerCase(Locale.ROOT);

        for (Product product : products) {

            if (product != null
                    && product.getProductName() != null
                    && product.getProductName()
                    .toLowerCase(Locale.ROOT)
                    .contains(searchText)) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // FILTER BY CATEGORY
    // =========================================================

    public List<Product> filterByCategory(
            List<Product> products,
            String category) {

        List<Product> results = new ArrayList<>();

        if (products == null) {
            return results;
        }

        if (category == null
                || category.trim().isEmpty()
                || category.equalsIgnoreCase(
                        "All Categories")) {

            return copyProducts(products);
        }

        String categoryText =
                category.trim();

        for (Product product : products) {

            if (product != null
                    && product.getCategory() != null
                    && product.getCategory()
                    .equalsIgnoreCase(
                            categoryText
                    )) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // FILTER BY BRAND
    // =========================================================

    public List<Product> filterByBrand(
            List<Product> products,
            String brand) {

        List<Product> results = new ArrayList<>();

        if (products == null) {
            return results;
        }

        if (brand == null
                || brand.trim().isEmpty()
                || brand.equalsIgnoreCase(
                        "All Brands")) {

            return copyProducts(products);
        }

        String brandText =
                brand.trim();

        for (Product product : products) {

            if (product != null
                    && product.getBrand() != null
                    && product.getBrand()
                    .equalsIgnoreCase(
                            brandText
                    )) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // FILTER BY PRICE RANGE
    // =========================================================

    public List<Product> filterByPriceRange(
            List<Product> products,
            double minimumPrice,
            double maximumPrice) {

        List<Product> results = new ArrayList<>();

        if (products == null) {
            return results;
        }

        if (minimumPrice < 0) {
            minimumPrice = 0;
        }

        if (maximumPrice < minimumPrice) {
            return results;
        }

        for (Product product : products) {

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
    // SORT BY PRICE: LOW TO HIGH
    // =========================================================

    public List<Product> sortByPriceLowToHigh(
            List<Product> products) {

        List<Product> results =
                copyProducts(products);

        results.sort(
                Comparator.comparingDouble(
                        Product::getPrice
                )
        );

        return results;
    }

    // =========================================================
    // SORT BY PRICE: HIGH TO LOW
    // =========================================================

    public List<Product> sortByPriceHighToLow(
            List<Product> products) {

        List<Product> results =
                copyProducts(products);

        results.sort(
                Comparator.comparingDouble(
                        Product::getPrice
                ).reversed()
        );

        return results;
    }

    // =========================================================
    // SORT BY NAME: A TO Z
    // =========================================================

    public List<Product> sortByNameAToZ(
            List<Product> products) {

        List<Product> results =
                copyProducts(products);

        results.sort(
                Comparator.comparing(
                        Product::getProductName,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER
                        )
                )
        );

        return results;
    }

    // =========================================================
    // SORT BY NAME: Z TO A
    // =========================================================

    public List<Product> sortByNameZToA(
            List<Product> products) {

        List<Product> results =
                copyProducts(products);

        results.sort(
                Comparator.comparing(
                        Product::getProductName,
                        Comparator.nullsLast(
                                String.CASE_INSENSITIVE_ORDER
                        )
                ).reversed()
        );

        return results;
    }

    // =========================================================
    // SORT BY STOCK: HIGH TO LOW
    // =========================================================

    public List<Product> sortByStockHighToLow(
            List<Product> products) {

        List<Product> results =
                copyProducts(products);

        results.sort(
                Comparator.comparingInt(
                        Product::getQuantity
                ).reversed()
        );

        return results;
    }

    // =========================================================
    // SORT BY STOCK: LOW TO HIGH
    // =========================================================

    public List<Product> sortByStockLowToHigh(
            List<Product> products) {

        List<Product> results =
                copyProducts(products);

        results.sort(
                Comparator.comparingInt(
                        Product::getQuantity
                )
        );

        return results;
    }

    // =========================================================
    // ADVANCED MULTI-FILTER
    // =========================================================

    public List<Product> advancedFilter(
            List<Product> products,
            String keyword,
            String category,
            String brand,
            double minimumPrice,
            double maximumPrice) {

        List<Product> results =
                new ArrayList<>();

        if (products == null) {
            return results;
        }

        boolean useKeyword =
                keyword != null
                        && !keyword.trim().isEmpty();

        boolean useCategory =
                category != null
                        && !category.trim().isEmpty()
                        && !category.equalsIgnoreCase(
                                "All Categories"
                        );

        boolean useBrand =
                brand != null
                        && !brand.trim().isEmpty()
                        && !brand.equalsIgnoreCase(
                                "All Brands"
                        );

        boolean usePrice =
                minimumPrice >= 0
                        && maximumPrice >= minimumPrice;

        String searchText =
                useKeyword
                        ? keyword.trim()
                        .toLowerCase(Locale.ROOT)
                        : "";

        String categoryText =
                useCategory
                        ? category.trim()
                        : "";

        String brandText =
                useBrand
                        ? brand.trim()
                        : "";

        for (Product product : products) {

            if (product == null) {
                continue;
            }

            boolean matchesKeyword = true;
            boolean matchesCategory = true;
            boolean matchesBrand = true;
            boolean matchesPrice = true;

            if (useKeyword) {

                matchesKeyword =
                        product.getProductName() != null
                                && product.getProductName()
                                .toLowerCase(Locale.ROOT)
                                .contains(searchText);
            }

            if (useCategory) {

                matchesCategory =
                        product.getCategory() != null
                                && product.getCategory()
                                .equalsIgnoreCase(
                                        categoryText
                                );
            }

            if (useBrand) {

                matchesBrand =
                        product.getBrand() != null
                                && product.getBrand()
                                .equalsIgnoreCase(
                                        brandText
                                );
            }

            if (usePrice) {

                double price =
                        product.getPrice();

                matchesPrice =
                        price >= minimumPrice
                                && price <= maximumPrice;
            }

            if (matchesKeyword
                    && matchesCategory
                    && matchesBrand
                    && matchesPrice) {

                results.add(product);
            }
        }

        return results;
    }

    // =========================================================
    // DISPLAY SEARCH RESULTS
    // =========================================================

    public void displaySearchResults(
            List<Product> products) {

        if (products == null
                || products.isEmpty()) {

            System.out.println(
                    "No matching products found."
            );

            return;
        }

        System.out.println(
                "\n===== SAIVEE SEARCH RESULTS ====="
        );

        System.out.println(
                "Products Found: "
                        + products.size()
        );

        for (Product product : products) {

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
    // CREATE SAFE COPY
    // =========================================================

    private List<Product> copyProducts(
            List<Product> products) {

        List<Product> results =
                new ArrayList<>();

        if (products == null) {
            return results;
        }

        for (Product product : products) {

            if (product != null) {
                results.add(product);
            }
        }

        return results;
    }
}