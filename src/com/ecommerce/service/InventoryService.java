package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.Product;
import com.ecommerce.model.ProductVariant;
import com.ecommerce.util.AppConstants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class InventoryService {

    private final List<Product> products;
    private final java.util.Map<Integer, Integer> reservedStock;
    private final java.util.Map<String, Integer> reservedVariantStock;

    public InventoryService() {
        products = new ArrayList<>();
        reservedStock = new java.util.HashMap<>();
        reservedVariantStock = new java.util.HashMap<>();
    }

    // =========================================================
    // BASIC INVENTORY OPERATIONS
    // =========================================================

    public synchronized void addProduct(
            Product product) {

        if (product == null) {
            System.out.println("Invalid product.");
            return;
        }

        Product existingProduct =
                findProductById(product.getProductId());

        if (existingProduct == null) {

            products.add(product);

            System.out.println(
                    "Product added to inventory successfully."
            );

        } else {

            System.out.println(
                    "Product already exists in inventory."
            );
        }
    }

    public synchronized void addInventory(
            Product product,
            int stock) {

        if (product == null || stock < 0) {

            System.out.println(
                    "Invalid inventory details."
            );

            return;
        }

        Product existingProduct =
                findProductById(product.getProductId());

        if (existingProduct == null) {

            product.setQuantity(stock);
            products.add(product);

        } else {

            existingProduct.setQuantity(existingProduct.getQuantity() + stock);
        }

        System.out.println(
                "Inventory added successfully."
        );
    }

    // =========================================================
    // PRODUCT SEARCH
    // =========================================================

    public synchronized Product findProductById(
            int productId) {

        for (Product product : products) {

            if (product != null
                    && product.getProductId() == productId) {

                return product;
            }
        }

        return null;
    }

    public synchronized Product findInventoryByProductId(
            int productId) {

        return findProductById(productId);
    }

    // =========================================================
    // STOCK OPERATIONS
    // =========================================================

    public synchronized boolean addStock(
            int productId,
            int quantity) {

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        Product product =
                findProductById(productId);

        if (product == null) {

            System.out.println(
                    "Product not found."
            );

            return false;
        }

        product.setQuantity(
                product.getQuantity() + quantity
        );

        System.out.println(
                "Stock added successfully."
        );

        return true;
    }

    // =========================================================
    // REMOVE STOCK
    // =========================================================

    public synchronized boolean removeStock(
            int productId,
            int quantity) {

        if (quantity <= 0) {

            System.out.println(
                    "Stock quantity must be greater than zero."
            );

            return false;
        }

        Product product =
                findProductById(productId);

        if (product == null) {

            System.out.println(
                    "Product not found."
            );

            return false;
        }

        if (quantity > product.getQuantity()) {

            System.out.println(
                    "Insufficient stock."
            );

            return false;
        }

        product.setQuantity(
                product.getQuantity() - quantity
        );

        System.out.println(
                "Stock removed successfully."
        );

        return true;
    }

    // =========================================================
    // UPDATE STOCK
    // =========================================================

    public synchronized boolean updateStock(
            int productId,
            int newQuantity) {

        if (newQuantity < 0) {

            System.out.println(
                    "Stock quantity cannot be negative."
            );

            return false;
        }

        Product product =
                findProductById(productId);

        if (product == null) {

            System.out.println(
                    "Product not found."
            );

            return false;
        }

        product.setQuantity(newQuantity);

        System.out.println(
                "Stock updated successfully."
        );

        return true;
    }

    // =========================================================
    // RESERVE STOCK
    // =========================================================

    public synchronized boolean reserveStock(
            Product product,
            int quantity)
            throws InsufficientStockException {

        if (product == null || quantity <= 0) {
            return false;
        }

        Product inventoryProduct =
                findProductById(product.getProductId());

        if (inventoryProduct == null) {

            System.out.println(
                    "Product not found in inventory."
            );

            return false;
        }

        if (getAvailableStock(inventoryProduct.getProductId()) < quantity) {

            throw new InsufficientStockException(
                    "Insufficient stock for "
                            + inventoryProduct.getProductName()
                            + ". Available: "
                            + inventoryProduct.getQuantity()
                            + ", Requested: "
                            + quantity
            );
        }

        reservedStock.merge(inventoryProduct.getProductId(), quantity, Integer::sum);

        System.out.println(
                "Stock reserved: "
                        + inventoryProduct.getProductName()
                        + " x"
                        + quantity
        );

        return true;
    }

    public synchronized boolean reserveStock(Product product, int quantity, String variantKey) throws InsufficientStockException {
        if (variantKey == null || variantKey.isBlank()) return reserveStock(product, quantity);
        if (product == null || quantity <= 0) return false;
        Product inventoryProduct = findProductById(product.getProductId());
        if (inventoryProduct == null) return false;
        ProductVariant variant = inventoryProduct.findVariant(variantKey);
        if (variant == null) throw new InsufficientStockException("Variant is not available: " + variantKey);
        String key = inventoryProduct.getProductId() + "|" + variantKey;
        int reserved = reservedVariantStock.getOrDefault(key, 0);
        if (variant.getStock() - reserved < quantity) throw new InsufficientStockException("Insufficient stock for variant " + variantKey);
        reservedVariantStock.put(key, reserved + quantity);
        return true;
    }

    public synchronized int getAvailableVariantStock(int productId, String variantKey) {
        Product p = findProductById(productId); if (p == null) return 0;
        ProductVariant v = p.findVariant(variantKey); if (v == null) return 0;
        String key = productId + "|" + variantKey;
        return Math.max(0, v.getStock() - reservedVariantStock.getOrDefault(key, 0));
    }

    public synchronized boolean reserveStock(
            int productId,
            int quantity)
            throws InsufficientStockException {

        Product product =
                findInventoryByProductId(productId);

        if (product == null) {

            System.out.println(
                    "Product not found in inventory."
            );

            return false;
        }

        return reserveStock(product, quantity);
    }

    // =========================================================
    // RELEASE RESERVED STOCK
    // =========================================================

    public synchronized boolean releaseReservedStock(int productId, int quantity, String variantKey) {
        if (variantKey == null || variantKey.isBlank()) return releaseReservedStock(productId, quantity);
        String key = productId + "|" + variantKey;
        int reserved = reservedVariantStock.getOrDefault(key, 0);
        if (reserved <= 0 || quantity <= 0) return false;
        int released = Math.min(quantity, reserved);
        if (released == reserved) reservedVariantStock.remove(key); else reservedVariantStock.put(key, reserved - released);
        return true;
    }

    public synchronized boolean releaseReservedStock(
            int productId,
            int quantity) {

        if (quantity <= 0) {
            return false;
        }

        Product product =
                findInventoryByProductId(productId);

        if (product == null) {

            System.out.println(
                    "Product not found in inventory."
            );

            return false;
        }

        int reserved = reservedStock.getOrDefault(productId, 0);
        int released = Math.min(quantity, reserved);
        if (released <= 0) return false;
        if (reserved == released) reservedStock.remove(productId);
        else reservedStock.put(productId, reserved - released);

        System.out.println("Reserved stock released: " + product.getProductName() + " x" + released);
        return true;
    }

    // =========================================================
    // COMMIT RESERVED STOCK
    // =========================================================

    public synchronized boolean commitReservedStock(int productId, int quantity, String variantKey) {
        if (variantKey == null || variantKey.isBlank()) return commitReservedStock(productId, quantity);
        Product product = findProductById(productId); if (product == null || quantity <= 0) return false;
        ProductVariant variant = product.findVariant(variantKey); if (variant == null) return false;
        String key = productId + "|" + variantKey;
        int reserved = reservedVariantStock.getOrDefault(key, 0);
        if (quantity > reserved || !variant.removeStock(quantity)) return false;
        reservedVariantStock.remove(key);
        if (reserved > quantity) reservedVariantStock.put(key, reserved - quantity);
        product.setQuantity(Math.max(0, product.getQuantity() - quantity));
        return true;
    }

    public synchronized boolean commitReservedStock(int productId, int quantity) {
        if (quantity <= 0) return false;
        Product product = findProductById(productId);
        if (product == null) return false;
        int reserved = reservedStock.getOrDefault(productId, 0);
        if (quantity > reserved) return false;
        product.setQuantity(product.getQuantity() - quantity);
        if (reserved == quantity) reservedStock.remove(productId);
        else reservedStock.put(productId, reserved - quantity);
        return true;
    }

    public synchronized int getReservedStock(int productId) { return reservedStock.getOrDefault(productId, 0); }
    public synchronized int getAvailableStock(int productId) {
        Product p = findProductById(productId);
        return p == null ? 0 : Math.max(0, p.getQuantity() - reservedStock.getOrDefault(productId, 0));
    }

    public synchronized boolean restoreVariantStock(Product product, int quantity, String variantKey) {
        if (product == null || quantity <= 0 || variantKey == null || variantKey.isBlank()) return false;
        Product p = findProductById(product.getProductId()); if (p == null) return false;
        ProductVariant v = p.findVariant(variantKey); if (v == null) return false;
        v.addStock(quantity);
        p.setQuantity(p.getQuantity() + quantity);
        return true;
    }

    // =========================================================
    // RESTORE STOCK
    // =========================================================

    public synchronized boolean restoreStock(
            Product product,
            int quantity) {

        if (product == null || quantity <= 0) {
            return false;
        }

        Product inventoryProduct =
                findProductById(product.getProductId());

        if (inventoryProduct == null) {

            System.out.println(
                    "Product not found in inventory."
            );

            return false;
        }

        inventoryProduct.setQuantity(
                inventoryProduct.getQuantity() + quantity
        );

        return true;
    }

    // =========================================================
    // STOCK CHECK - PRODUCT
    // =========================================================

    public synchronized boolean hasEnoughStock(
            Product product,
            int quantity) {

        if (product == null || quantity <= 0) {
            return false;
        }

        Product inventoryProduct =
                findProductById(product.getProductId());

        if (inventoryProduct == null) {
            return false;
        }

        return getAvailableStock(product.getProductId()) >= quantity;
    }

    // =========================================================
    // STOCK CHECK - PRODUCT ID
    // =========================================================

    public synchronized boolean hasEnoughStock(
            int productId,
            int quantity) {

        if (quantity <= 0) {
            return false;
        }

        Product product =
                findProductById(productId);

        if (product == null) {
            return false;
        }

        return getAvailableStock(productId) >= quantity;
    }

    // =========================================================
    // LOW STOCK CHECK
    // =========================================================

    public synchronized boolean isLowStock(
            int productId) {

        Product product =
                findProductById(productId);

        if (product == null) {
            return false;
        }

        return getAvailableStock(productId)
                <= AppConstants.LOW_STOCK_THRESHOLD;
    }

    // =========================================================
    // LOW STOCK PRODUCTS
    // =========================================================

    public synchronized List<Product> getLowStockProducts() {

        List<Product> lowStockProducts =
                new ArrayList<>();

        for (Product product : products) {

            if (product != null
                    && getAvailableStock(product.getProductId())
                    <= AppConstants.LOW_STOCK_THRESHOLD) {

                lowStockProducts.add(product);
            }
        }

        return Collections.unmodifiableList(
                lowStockProducts
        );
    }

    public synchronized void displayLowStock(
            int threshold) {

        if (threshold < 0) {
            threshold = AppConstants.LOW_STOCK_THRESHOLD;
        }

        boolean found = false;

        System.out.println(
                "===== SAIVEE LOW STOCK PRODUCTS ====="
        );

        for (Product product : products) {

            if (product != null
                    && product.getQuantity() <= threshold) {

                found = true;

                System.out.println(
                        "----------------------------"
                );

                System.out.println(
                        "Product ID: "
                                + product.getProductId()
                );

                System.out.println(
                        "Product Name: "
                                + product.getProductName()
                );

                System.out.println(
                        "Current Stock: "
                                + product.getQuantity()
                );
            }
        }

        if (!found) {

            System.out.println(
                    "No low-stock products found."
            );
        }
    }

    public synchronized void displayLowStockProducts() {

        displayLowStock(
                AppConstants.LOW_STOCK_THRESHOLD
        );
    }

    // =========================================================
    // DISPLAY INVENTORY
    // =========================================================

    public synchronized void displayAllInventory() {

        if (products.isEmpty()) {

            System.out.println(
                    "Inventory is empty."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE INVENTORY ====="
        );

        for (Product product : products) {

            if (product == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            System.out.println(
                    "Product ID: "
                            + product.getProductId()
            );

            System.out.println(
                    "Product Name: "
                            + product.getProductName()
            );

            System.out.println(
                    "Category: "
                            + product.getCategory()
            );

            System.out.println(
                    "Brand: "
                            + product.getBrand()
            );

            System.out.println(
                    "Price: ₹"
                            + product.getPrice()
            );

            System.out.println(
                    "Stock: "
                            + product.getQuantity()
            );
        }

        System.out.println(
                "----------------------------"
        );
    }

    public synchronized void displayInventory() {

        displayAllInventory();
    }

    // =========================================================
    // GETTERS
    // =========================================================

    public synchronized int getProductCount() {

        return products.size();
    }

    public synchronized int getInventoryCount() {

        return products.size();
    }

    public synchronized List<Product> getProducts() {

        return Collections.unmodifiableList(
                new ArrayList<>(products)
        );
    }

    public static int getLowStockLimit() {

        return AppConstants.LOW_STOCK_THRESHOLD;
    }
}