package com.ecommerce.model;

import java.util.ArrayList;
import java.util.List;
import java.time.LocalDateTime;

public class Order {

    private int orderId;
    private Customer customer;
    private List<OrderItem> orderItems;
    private OrderStatus status;

    private double subtotal;
    private double discount;
    private double deliveryCharge;
    private double tax;
    private double totalAmount;
    private final LocalDateTime createdAt;

    public Order() {

        orderItems = new ArrayList<>();
        status = OrderStatus.PLACED;

        subtotal = 0.0;
        discount = 0.0;
        deliveryCharge = 0.0;
        tax = 0.0;
        totalAmount = 0.0;
        createdAt = LocalDateTime.now();
    }

    public Order(
            int orderId,
            Customer customer) {

        this.orderId = orderId;
        this.customer = customer;
        this.orderItems = new ArrayList<>();
        this.status = OrderStatus.PLACED;

        this.subtotal = 0.0;
        this.discount = 0.0;
        this.deliveryCharge = 0.0;
        this.tax = 0.0;
        this.totalAmount = 0.0;
        this.createdAt = LocalDateTime.now();
    }

    // ==================================================
    // GETTERS
    // ==================================================

    public int getOrderId() {
        return orderId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public List<OrderItem> getOrderItems() {
        return orderItems;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public double getSubtotal() {
        return subtotal;
    }

    public double getDiscount() {
        return discount;
    }

    public double getDeliveryCharge() {
        return deliveryCharge;
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public double getTax() { return tax; }

    public LocalDateTime getCreatedAt() { return createdAt; }

    // ==================================================
    // ADD ITEM
    // ==================================================

    public void addItem(OrderItem item) {

        if (item == null) {
            return;
        }

        orderItems.add(item);

        calculateTotal();
    }

    // ==================================================
    // REMOVE ITEM
    // ==================================================

    public void removeItem(OrderItem item) {

        if (item == null) {
            return;
        }

        orderItems.remove(item);

        calculateTotal();
    }

    // ==================================================
    // CALCULATE SUBTOTAL
    // ==================================================

    public void calculateSubtotal() {

        subtotal = 0.0;

        for (OrderItem item : orderItems) {

            subtotal +=
                    item.calculateSubtotal();
        }
    }

    // ==================================================
    // CALCULATE FINAL TOTAL
    // ==================================================

    public void calculateTotal() {

        calculateSubtotal();

        tax = Math.max(0.0, subtotal - discount) * 0.18;
        totalAmount = subtotal - discount + tax + deliveryCharge;

        if (totalAmount < 0) {
            totalAmount = 0.0;
        }
    }

    // ==================================================
    // SET DISCOUNT
    // ==================================================

    public void setDiscount(
            double discount) {

        if (discount < 0) {
            discount = 0.0;
        }

        this.discount = discount;

        calculateTotal();
    }

    // ==================================================
    // SET DELIVERY CHARGE
    // ==================================================

    public void setDeliveryCharge(
            double deliveryCharge) {

        if (deliveryCharge < 0) {
            deliveryCharge = 0.0;
        }

        this.deliveryCharge =
                deliveryCharge;

        calculateTotal();
    }

    // ==================================================
    // SET BOTH CHARGES
    // ==================================================

    public void setCharges(
            double discount,
            double deliveryCharge) {

        if (discount < 0) {
            discount = 0.0;
        }

        if (deliveryCharge < 0) {
            deliveryCharge = 0.0;
        }

        this.discount = discount;

        this.deliveryCharge =
                deliveryCharge;

        calculateTotal();
    }

    // ==================================================
    // UPDATE STATUS
    // ==================================================

    public void updateStatus(
            OrderStatus status) {

        if (status == null) {
            return;
        }

        this.status = status;
    }

    // ==================================================
    // DISPLAY ORDER
    // ==================================================

    public void displayOrder() {

        System.out.println(
                "----------------------------"
        );

        System.out.println(
                "Order ID: " + orderId
        );

        if (customer != null) {

            System.out.println(
                    "Customer: "
                            + customer.getName()
            );
        }

        System.out.println(
                "Order Status: " + status
        );

        System.out.println(
                "\nOrder Items:"
        );

        for (OrderItem item :
                orderItems) {

            System.out.println(item);

            System.out.println(
                    "----------------------------"
            );
        }

        System.out.println(
                String.format(
                        "Subtotal: ₹%.2f",
                        subtotal
                )
        );

        System.out.println(
                String.format(
                        "Discount: -₹%.2f",
                        discount
                )
        );

        System.out.println(
                String.format(
                        "Delivery Charge: ₹%.2f",
                        deliveryCharge
                )
        );

        System.out.println(
                String.format(
                        "Total Amount: ₹%.2f",
                        totalAmount
                )
        );
    }

    // ==================================================
    // TO STRING
    // ==================================================

    @Override
    public String toString() {

        String customerName =
                customer != null
                        ? customer.getName()
                        : "Unknown";

        return String.format(
                "Order ID: %d"
                        + "\nCustomer: %s"
                        + "\nStatus: %s"
                        + "\nSubtotal: ₹%.2f"
                        + "\nDiscount: -₹%.2f"
                        + "\nDelivery Charge: ₹%.2f"
                        + "\nTotal Amount: ₹%.2f",

                orderId,
                customerName,
                status,
                subtotal,
                discount,
                deliveryCharge,
                totalAmount
        );
    }
}