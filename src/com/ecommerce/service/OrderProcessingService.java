package com.ecommerce.service;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OrderProcessingService {

    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderService orderService;

    private final Map<Integer, List<OrderItem>> activeReservations;

    private static final double DELIVERY_CHARGE_PER_ITEM = 20.0;

    public OrderProcessingService(
            CartService cartService,
            InventoryService inventoryService,
            OrderService orderService) {

        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;

        this.activeReservations = new HashMap<>();
    }

    // =========================================================
    // VALIDATE CART
    // =========================================================

    public synchronized boolean validateCart(
            int cartId)
            throws InsufficientStockException {

        if (cartService == null
                || inventoryService == null) {

            System.out.println(
                    "Required services are not available."
            );

            return false;
        }

        Cart cart =
                cartService.findCartById(cartId);

        if (cart == null) {

            System.out.println(
                    "Cart not found."
            );

            return false;
        }

        if (cart.getItems() == null
                || cart.getItems().isEmpty()) {

            System.out.println(
                    "Cannot process an empty cart."
            );

            return false;
        }

        for (OrderItem item : cart.getItems()) {

            if (item == null
                    || item.getProduct() == null
                    || item.getQuantity() <= 0) {

                System.out.println(
                        "Invalid item found in cart."
                );

                return false;
            }

            int productId =
                    item.getProduct()
                            .getProductId();

            int quantity =
                    item.getQuantity();

            if (inventoryService
                    .findInventoryByProductId(productId)
                    == null) {

                System.out.println(
                        "Inventory not found for product: "
                                + item.getProduct()
                                .getProductName()
                );

                return false;
            }

            int availableStock = item.getVariantKey() == null || item.getVariantKey().isBlank()
                    ? inventoryService.getAvailableStock(productId)
                    : inventoryService.getAvailableVariantStock(productId, item.getVariantKey());

            if (quantity > availableStock) {

                throw new InsufficientStockException(
                        "Insufficient stock for: "
                                + item.getProduct()
                                .getProductName()
                                + ". Available: "
                                + availableStock
                                + ", Requested: "
                                + quantity
                );
            }
        }

        return true;
    }

    // =========================================================
    // RESERVE CART STOCK
    //
    // Reservation stays active until:
    // 1. Payment succeeds -> order consumes reservation
    // 2. Payment fails -> reservation released
    // 3. Customer cancels -> reservation released
    // =========================================================

    public synchronized List<OrderItem> reserveCartStock(
            int cartId)
            throws InsufficientStockException {

        if (cartService == null
                || inventoryService == null) {

            System.out.println(
                    "Required services are not available."
            );

            return null;
        }

        if (activeReservations.containsKey(cartId)) {

            System.out.println(
                    "Stock is already reserved for this cart."
            );

            return new ArrayList<>(
                    activeReservations.get(cartId)
            );
        }

        Cart cart =
                cartService.findCartById(cartId);

        if (cart == null) {

            System.out.println(
                    "Cart not found."
            );

            return null;
        }

        if (cart.getItems() == null
                || cart.getItems().isEmpty()) {

            System.out.println(
                    "Cannot reserve stock for an empty cart."
            );

            return null;
        }

        validateCart(cartId);

        List<OrderItem> reservedItems =
                new ArrayList<>();

        try {

            for (OrderItem item : cart.getItems()) {

                if (item == null
                        || item.getProduct() == null
                        || item.getQuantity() <= 0) {

                    rollbackReservations(
                            reservedItems
                    );

                    System.out.println(
                            "Invalid item found in cart."
                    );

                    return null;
                }

                int productId =
                        item.getProduct()
                                .getProductId();

                int quantity =
                        item.getQuantity();

                boolean reserved = item.getVariantKey() == null || item.getVariantKey().isBlank()
                        ? inventoryService.reserveStock(productId, quantity)
                        : inventoryService.reserveStock(item.getProduct(), quantity, item.getVariantKey());

                if (!reserved) {

                    rollbackReservations(
                            reservedItems
                    );

                    System.out.println(
                            "Unable to reserve stock for product: "
                                    + item.getProduct()
                                    .getProductName()
                    );

                    return null;
                }

                OrderItem reservedCopy = new OrderItem(item.getProduct(), quantity);
                reservedCopy.setVariantKey(item.getVariantKey());
                reservedItems.add(reservedCopy);
            }

            activeReservations.put(
                    cartId,
                    new ArrayList<>(
                            reservedItems
                    )
            );

            System.out.println(
                    "Cart stock reserved successfully."
            );

            return new ArrayList<>(
                    reservedItems
            );

        } catch (InsufficientStockException e) {

            rollbackReservations(
                    reservedItems
            );

            throw e;

        } catch (RuntimeException e) {

            rollbackReservations(
                    reservedItems
            );

            System.out.println(
                    "Stock reservation failed: "
                            + e.getMessage()
            );

            return null;
        }
    }

    // =========================================================
    // CHECK ACTIVE RESERVATION
    // =========================================================

    public synchronized boolean hasActiveReservation(
            int cartId) {

        return activeReservations.containsKey(
                cartId
        );
    }

    // =========================================================
    // RELEASE RESERVATION BY CART ID
    // =========================================================

    public synchronized void releaseCartReservation(
            int cartId) {

        List<OrderItem> reservedItems =
                activeReservations.remove(
                        cartId
                );

        if (reservedItems == null
                || reservedItems.isEmpty()) {

            return;
        }

        rollbackReservations(
                reservedItems
        );

        System.out.println(
                "Cart stock reservation released."
        );
    }

    // =========================================================
    // RELEASE RESERVATION BY LIST
    //
    // Kept for compatibility.
    // =========================================================

    public synchronized void releaseCartReservation(
            List<OrderItem> reservedItems) {

        if (reservedItems == null
                || reservedItems.isEmpty()) {

            return;
        }

        rollbackReservations(
                reservedItems
        );

        System.out.println(
                "Cart stock reservation released."
        );
    }

    // =========================================================
    // PLACE ORDER AFTER RESERVATION
    //
    // IMPORTANT:
    // This method DOES NOT reserve stock again.
    // It consumes the existing reservation.
    // =========================================================

    public synchronized Order placeReservedOrder(
            int orderId,
            int cartId,
            double deliveryCharge,
            double discount) {

        if (orderService == null) {

            System.out.println(
                    "Order service is not available."
            );

            return null;
        }

        List<OrderItem> reservedItems =
                activeReservations.get(cartId);

        if (reservedItems == null
                || reservedItems.isEmpty()) {

            System.out.println(
                    "No active stock reservation found."
            );

            return null;
        }

        Cart cart =
                cartService.findCartById(cartId);

        if (cart == null) {

            releaseCartReservation(cartId);

            System.out.println(
                    "Cart not found."
            );

            return null;
        }

        if (cart.getCustomer() == null) {

            releaseCartReservation(cartId);

            System.out.println(
                    "Customer not found."
            );

            return null;
        }

        try {

            Order order =
                    new Order(
                            orderId,
                            cart.getCustomer()
                    );

            /*
             * Build order from the reserved snapshot.
             * This prevents cart changes from affecting
             * the already reserved stock.
             */
            for (OrderItem reservedItem :
                    reservedItems) {

                if (reservedItem == null
                        || reservedItem.getProduct() == null
                        || reservedItem.getQuantity() <= 0) {

                    releaseCartReservation(cartId);

                    System.out.println(
                            "Invalid reserved item."
                    );

                    return null;
                }

                OrderItem orderItem = new OrderItem(reservedItem.getProduct(), reservedItem.getQuantity());
                orderItem.setVariantKey(reservedItem.getVariantKey());
                order.addItem(orderItem);
            }

            if (order.getOrderItems().isEmpty()) {

                releaseCartReservation(cartId);

                System.out.println(
                        "Cannot create order: no valid items."
                );

                return null;
            }

            if (discount < 0) {
                discount = 0.0;
            }

            if (deliveryCharge < 0) {
                deliveryCharge = 0.0;
            }

            order.setCharges(
                    discount,
                    deliveryCharge
            );

            /*
             * Store order.
             *
             * IMPORTANT:
             * Check the result before consuming the
             * reservation or clearing the cart.
             */
            boolean orderAdded =
                    orderService.addOrder(order);

            if (!orderAdded) {

                System.out.println(
                        "Order could not be added. "
                                + "Stock reservation has been released."
                );

                releaseCartReservation(cartId);

                return null;
            }

            /* Commit the reservation atomically: reserved stock becomes sold stock. */
            for (OrderItem reservedItem : reservedItems) {
                boolean committed = reservedItem.getVariantKey() == null || reservedItem.getVariantKey().isBlank()
                        ? inventoryService.commitReservedStock(reservedItem.getProduct().getProductId(), reservedItem.getQuantity())
                        : inventoryService.commitReservedStock(reservedItem.getProduct().getProductId(), reservedItem.getQuantity(), reservedItem.getVariantKey());
                if (!committed) {
                    throw new IllegalStateException("Unable to commit reserved stock for product #" + reservedItem.getProduct().getProductId());
                }
            }
            activeReservations.remove(cartId);

            /*
             * Clear the customer's cart only after
             * successful order creation.
             */
            cart.clearCart();

            System.out.println(
                    "Reserved stock successfully converted into order."
            );

            System.out.println(
                    "Order placed successfully."
            );

            System.out.println(
                    "Order ID: " + orderId
            );

            System.out.println(
                    String.format(
                            "Subtotal: ₹%.2f",
                            order.getSubtotal()
                    )
            );

            System.out.println(
                    String.format(
                            "Discount: -₹%.2f",
                            order.getDiscount()
                    )
            );

            System.out.println(
                    String.format(
                            "Delivery Charge: ₹%.2f",
                            order.getDeliveryCharge()
                    )
            );

            System.out.println(
                    String.format(
                            "Total Amount: ₹%.2f",
                            order.getTotalAmount()
                    )
            );

            return order;

        } catch (RuntimeException e) {

            /*
             * Order creation failed.
             * Return reserved stock.
             */
            releaseCartReservation(cartId);

            System.out.println(
                    "Order processing failed: "
                            + e.getMessage()
            );

            return null;
        }
    }

    // =========================================================
    // PLACE ORDER - AUTOMATIC CHARGES
    //
    // Direct/non-payment-flow usage.
    // =========================================================

    public synchronized Order placeOrder(
            int orderId,
            int cartId)
            throws InsufficientStockException {

        Cart cart =
                cartService.findCartById(cartId);

        if (cart == null
                || cart.getItems() == null
                || cart.getItems().isEmpty()) {

            return null;
        }

        double subtotal = 0.0;

        for (OrderItem item : cart.getItems()) {

            if (item != null) {

                subtotal +=
                        item.calculateSubtotal();
            }
        }

        double discount =
                calculateDiscount(subtotal);

        double deliveryCharge =
                calculateDeliveryCharge(
                        cart.getTotalItems()
                );

        return placeOrder(
                orderId,
                cartId,
                deliveryCharge,
                discount
        );
    }

    // =========================================================
    // PLACE ORDER - WITH CHARGES
    //
    // Direct usage safely reserves first.
    // =========================================================

    public synchronized Order placeOrder(
            int orderId,
            int cartId,
            double deliveryCharge,
            double discount)
            throws InsufficientStockException {

        /*
         * If reservation already exists,
         * consume that reservation.
         */
        if (hasActiveReservation(cartId)) {

            return placeReservedOrder(
                    orderId,
                    cartId,
                    deliveryCharge,
                    discount
            );
        }

        List<OrderItem> reservedItems =
                reserveCartStock(cartId);

        if (reservedItems == null
                || reservedItems.isEmpty()) {

            return null;
        }

        return placeReservedOrder(
                orderId,
                cartId,
                deliveryCharge,
                discount
        );
    }

    // =========================================================
    // ROLLBACK STOCK
    // =========================================================

    private void rollbackReservations(
            List<OrderItem> reservedItems) {

        if (reservedItems == null
                || inventoryService == null) {

            return;
        }

        for (OrderItem item : reservedItems) {

            if (item == null
                    || item.getProduct() == null
                    || item.getQuantity() <= 0) {

                continue;
            }

            if (item.getVariantKey() == null || item.getVariantKey().isBlank())
                inventoryService.releaseReservedStock(item.getProduct().getProductId(), item.getQuantity());
            else
                inventoryService.releaseReservedStock(item.getProduct().getProductId(), item.getQuantity(), item.getVariantKey());
        }
    }

    // =========================================================
    // DISCOUNT
    // =========================================================

    private double calculateDiscount(
            double subtotal) {

        if (subtotal < 2000) {
            return 0.0;
        }

        int blocks =
                (int) (
                        subtotal / 1000
                );

        return blocks * 100.0;
    }

    // =========================================================
    // DELIVERY CHARGE
    // =========================================================

    private double calculateDeliveryCharge(
            int totalItems) {

        if (totalItems <= 0) {
            return 0.0;
        }

        return totalItems
                * DELIVERY_CHARGE_PER_ITEM;
    }

    // =========================================================
    // PUBLIC DISCOUNT
    // =========================================================

    public double getDiscount(
            double subtotal) {

        if (subtotal < 0) {
            return 0.0;
        }

        return calculateDiscount(
                subtotal
        );
    }

    // =========================================================
    // PUBLIC DELIVERY CHARGE
    // =========================================================

    public double getDeliveryCharge(
            int totalItems) {

        return calculateDeliveryCharge(
                totalItems
        );
    }
}