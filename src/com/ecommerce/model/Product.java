package com.ecommerce.model;

public class Product {

    private final int productId;
    private String productName;
    private String category;
    private String subcategory;
    private double price;
    private int quantity;
    private String brand;
    private double averageRating;
    private int ratingCount;
    private final java.util.List<ProductVariant> variants = new java.util.ArrayList<>();

    public Product() {
        this.productId = 0;
    }

    // Backward-compatible constructor.
    public Product(int productId, String productName, String category,
                   double price, int quantity, String brand) {
        this(productId, productName, category, "", price, quantity, brand);
    }

    // Full catalogue constructor.
    public Product(int productId, String productName, String category,
                   String subcategory, double price, int quantity,
                   String brand) {
        this.productId = productId;
        this.productName = productName;
        this.category = category;
        this.subcategory = subcategory == null ? "" : subcategory;
        this.price = price;
        this.quantity = Math.max(0, quantity);
        this.brand = brand;
        this.averageRating = 0.0;
        this.ratingCount = 0;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public String getCategory() { return category; }
    public String getSubcategory() { return subcategory; }
    public String getSubCategory() { return subcategory; }
    public double getPrice() { return price; }
    public int getQuantity() { return quantity; }
    public int getAvailableStock() { return quantity; }
    public String getBrand() { return brand; }
    public double getAverageRating() { return averageRating; }
    public int getRatingCount() { return ratingCount; }
    public synchronized java.util.List<ProductVariant> getVariants() { return java.util.Collections.unmodifiableList(new java.util.ArrayList<>(variants)); }
    public synchronized void addVariant(ProductVariant variant) { if (variant != null) { variants.add(variant); recalculateQuantityFromVariants(); } }
    public synchronized void recalculateQuantityFromVariants() { if (!variants.isEmpty()) quantity = variants.stream().mapToInt(ProductVariant::getStock).sum(); }
    public synchronized int getVariantStock(String key) { for (ProductVariant v : variants) if (v.getKey().equals(key)) return v.getStock(); return 0; }
    public synchronized ProductVariant findVariant(String key) { for (ProductVariant v : variants) if (v.getKey().equals(key)) return v; return null; }

    public void setProductName(String productName) { this.productName = productName; }
    public void setCategory(String category) { this.category = category; }
    public void setSubcategory(String subcategory) { this.subcategory = subcategory == null ? "" : subcategory; }
    public void setSubCategory(String subcategory) { setSubcategory(subcategory); }
    public void setPrice(double price) { this.price = Math.max(0, price); }
    public void setQuantity(int quantity) { this.quantity = Math.max(0, quantity); }
    public void setBrand(String brand) { this.brand = brand; }

    public synchronized void addRating(int stars) {
        if (stars < 1 || stars > 5) return;
        double total = averageRating * ratingCount;
        ratingCount++;
        averageRating = (total + stars) / ratingCount;
    }

    @Override
    public String toString() {
        return "Product ID: " + productId +
                "\nProduct Name: " + productName +
                "\nCategory: " + category +
                "\nSubcategory: " + subcategory +
                "\nPrice: ₹" + price +
                "\nQuantity: " + quantity +
                "\nBrand: " + brand +
                "\nRating: " + String.format("%.1f", averageRating) +
                " (" + ratingCount + " ratings)";
    }
}
