package com.ecommerce.service;

import com.ecommerce.model.Delivery;
import com.ecommerce.model.DeliveryStatus;
import com.ecommerce.model.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class DeliveryService {

    private final List<Delivery> deliveries;

    public DeliveryService() {
        deliveries = new ArrayList<>();
    }

    // =========================================================
    // CREATE DELIVERY
    // =========================================================

    public synchronized boolean createDelivery(
            int deliveryId,
            Order order,
            String deliveryAddress) {

        if (order == null) {

            System.out.println(
                    "Cannot create delivery: order not found."
            );

            return false;
        }

        if (deliveryAddress == null
                || deliveryAddress.isBlank()) {

            System.out.println(
                    "Cannot create delivery: delivery address is required."
            );

            return false;
        }

        if (findDeliveryById(deliveryId) != null) {

            System.out.println(
                    "Delivery ID already exists."
            );

            return false;
        }

        Delivery delivery =
                new Delivery(
                        deliveryId,
                        order,
                        deliveryAddress
                );

        deliveries.add(delivery);

        System.out.println(
                "Delivery created successfully."
        );

        return true;
    }

    // =========================================================
    // FIND DELIVERY
    // =========================================================

    public synchronized Delivery findDeliveryById(
            int deliveryId) {

        for (Delivery delivery :
                deliveries) {

            if (delivery != null
                    && delivery.getDeliveryId()
                    == deliveryId) {

                return delivery;
            }
        }

        return null;
    }

    // =========================================================
    // ASSIGN DELIVERY PERSON
    // =========================================================

    public synchronized boolean assignDeliveryPerson(
            int deliveryId,
            String deliveryPerson) {

        Delivery delivery =
                findDeliveryById(deliveryId);

        if (delivery == null) {

            System.out.println(
                    "Delivery not found."
            );

            return false;
        }

        if (deliveryPerson == null
                || deliveryPerson.isBlank()) {

            System.out.println(
                    "Delivery person name is required."
            );

            return false;
        }

        delivery.assignDeliveryPerson(
                deliveryPerson
        );

        return true;
    }

    // =========================================================
    // UPDATE DELIVERY STATUS
    // =========================================================

    public synchronized boolean updateDeliveryStatus(
            int deliveryId,
            DeliveryStatus status) {

        Delivery delivery =
                findDeliveryById(deliveryId);

        if (delivery == null) {

            System.out.println(
                    "Delivery not found."
            );

            return false;
        }

        if (status == null) {

            System.out.println(
                    "Delivery status cannot be null."
            );

            return false;
        }

        delivery.updateStatus(status);

        return true;
    }

    // =========================================================
    // DISPLAY ALL DELIVERIES
    // =========================================================

    public synchronized void displayAllDeliveries() {

        if (deliveries.isEmpty()) {

            System.out.println(
                    "No deliveries available."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE DELIVERIES ====="
        );

        for (Delivery delivery :
                deliveries) {

            if (delivery == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            delivery.displayDeliveryInfo();
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // GET DELIVERY COUNT
    // =========================================================

    public synchronized int getDeliveryCount() {

        return deliveries.size();
    }

    // =========================================================
    // GET ALL DELIVERIES
    //
    // Returns a read-only snapshot.
    // =========================================================

    public synchronized List<Delivery>
    getDeliveries() {

        return Collections.unmodifiableList(
                new ArrayList<>(
                        deliveries
                )
        );
    }

    // =========================================================
    // CHECK DELIVERY
    // =========================================================

    public synchronized boolean deliveryExists(
            int deliveryId) {

        return findDeliveryById(
                deliveryId
        ) != null;
    }

    // =========================================================
    // FIND DELIVERY BY ORDER
    // =========================================================

    public synchronized Delivery findDeliveryByOrderId(
            int orderId) {

        for (Delivery delivery :
                deliveries) {

            if (delivery == null
                    || delivery.getOrder() == null) {
                continue;
            }

            if (delivery.getOrder()
                    .getOrderId()
                    == orderId) {

                return delivery;
            }
        }

        return null;
    }
}