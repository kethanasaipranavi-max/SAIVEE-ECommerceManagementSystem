package com.ecommerce.service;

import com.ecommerce.model.Product;

import java.util.List;

public class SearchTask implements Runnable {

    private final SearchService searchService;
    private final List<Product> products;
    private final String keyword;

    public SearchTask(SearchService searchService,
                      List<Product> products,
                      String keyword) {

        this.searchService = searchService;
        this.products = products;
        this.keyword = keyword;
    }

    @Override
    public void run() {

        System.out.println(
                "Searching in thread: "
                + Thread.currentThread().getName()
        );

        List<Product> results =
                searchService.searchByName(
                        products,
                        keyword
                );

        searchService.displaySearchResults(results);
    }
}