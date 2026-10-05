package com.ecommerce.model;

public class Delivery {

    private int deliveryId;
    private Order order;
    private String deliveryAddress;
    private String deliveryPerson;
    private DeliveryStatus status;

    public Delivery() {

        status = DeliveryStatus.NOT_ASSIGNED;
    }

    public Delivery(int deliveryId, Order order,
                    String deliveryAddress) {

        this.deliveryId = deliveryId;
        this.order = order;
        this.deliveryAddress = deliveryAddress;
        this.status = DeliveryStatus.NOT_ASSIGNED;
    }

    public int getDeliveryId() {
        return deliveryId;
    }

    public Order getOrder() {
        return order;
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public String getDeliveryPerson() {
        return deliveryPerson;
    }

    public DeliveryStatus getStatus() {
        return status;
    }

    public void assignDeliveryPerson(String deliveryPerson) {

        this.deliveryPerson = deliveryPerson;
        this.status = DeliveryStatus.ASSIGNED;

        System.out.println(
                "Delivery person assigned successfully."
        );
    }

    public void updateStatus(DeliveryStatus status) {

        this.status = status;

        System.out.println(
                "Delivery status updated to: " + status
        );
    }

    public void displayDeliveryInfo() {

        System.out.println("----------------------------");
        System.out.println("Delivery ID: " + deliveryId);
        System.out.println("Order ID: " + order.getOrderId());
        System.out.println("Delivery Address: " + deliveryAddress);
        System.out.println("Delivery Person: " + deliveryPerson);
        System.out.println("Delivery Status: " + status);
    }

    @Override
    public String toString() {

        return "Delivery ID: " + deliveryId +
                "\nOrder ID: " + order.getOrderId() +
                "\nDelivery Address: " + deliveryAddress +
                "\nDelivery Person: " + deliveryPerson +
                "\nStatus: " + status;
    }
}