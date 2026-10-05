package com.ecommerce.ui;

import com.ecommerce.model.Product;
import com.ecommerce.service.SearchService;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class SearchFrame extends JFrame {

    private final SearchService searchService;
    private final List<Product> products;

    private final JTextField searchField;
    private final JTextArea resultArea;
    private final JButton searchButton;
    private final JButton closeButton;

    public SearchFrame(SearchService searchService,
                       List<Product> products) {

        this.searchService = searchService;
        this.products = products;

        setTitle("Product Search");
        setSize(650, 450);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        searchField = new JTextField();
        resultArea = new JTextArea();
        resultArea.setEditable(false);

        searchButton = new JButton("Search");
        closeButton = new JButton("Close");

        JPanel topPanel = new JPanel(new BorderLayout(10, 10));
        topPanel.add(new JLabel("Search:"), BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchButton, BorderLayout.EAST);

        JPanel bottomPanel = new JPanel();
        bottomPanel.add(closeButton);

        setLayout(new BorderLayout(10, 10));

        add(topPanel, BorderLayout.NORTH);
        add(new JScrollPane(resultArea), BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        searchButton.addActionListener(
                e -> searchProducts()
        );

        closeButton.addActionListener(
                e -> dispose()
        );
    }

    private void searchProducts() {

        String keyword = searchField.getText();

        if (keyword == null || keyword.trim().isEmpty()) {
            resultArea.setText("Enter a product name to search.");
            return;
        }

        List<Product> results =
                searchService.searchByName(products, keyword);

        if (results.isEmpty()) {
            resultArea.setText("No products found.");
            return;
        }

        StringBuilder output = new StringBuilder();

        for (Product product : results) {
            output.append("----------------------------\n");
            output.append("ID: ")
                    .append(product.getProductId())
                    .append("\n");
            output.append("Name: ")
                    .append(product.getProductName())
                    .append("\n");
            output.append("Category: ")
                    .append(product.getCategory())
                    .append("\n");
            output.append("Price: ₹")
                    .append(product.getPrice())
                    .append("\n");
            output.append("Brand: ")
                    .append(product.getBrand())
                    .append("\n");
        }

        resultArea.setText(output.toString());
    }
}