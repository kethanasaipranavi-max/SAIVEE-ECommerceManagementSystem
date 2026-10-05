package com.ecommerce.service;

import com.ecommerce.model.Delivery;
import com.ecommerce.model.DeliveryStatus;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;

public class DeliveryProcessingService {

    private final DeliveryService deliveryService;
    private final OrderService orderService;

    public DeliveryProcessingService(
            DeliveryService deliveryService,
            OrderService orderService) {

        this.deliveryService = deliveryService;
        this.orderService = orderService;
    }

    public void createDelivery(int deliveryId,
                               int orderId,
                               String deliveryAddress) {

        Order order = orderService.findOrderById(orderId);

        if (order == null) {

            System.out.println("Order not found.");
            return;
        }

        if (order.getStatus() != OrderStatus.CONFIRMED
                && order.getStatus() != OrderStatus.PROCESSING) {

            System.out.println(
                    "Delivery can only be created for a confirmed "
                    + "or processing order."
            );

            return;
        }

        deliveryService.createDelivery(
                deliveryId,
                order,
                deliveryAddress
        );
    }

    public void assignDeliveryPerson(int deliveryId,
                                     String deliveryPerson) {

        if (deliveryPerson == null
                || deliveryPerson.trim().isEmpty()) {

            System.out.println(
                    "Delivery person name cannot be empty."
            );

            return;
        }

        deliveryService.assignDeliveryPerson(
                deliveryId,
                deliveryPerson
        );
    }

    public void updateDeliveryStatus(int deliveryId,
                                     DeliveryStatus status) {

        Delivery delivery =
                deliveryService.findDeliveryById(deliveryId);

        if (delivery == null) {

            System.out.println("Delivery not found.");
            return;
        }

        if (status == null) {

            System.out.println("Invalid delivery status.");
            return;
        }

        deliveryService.updateDeliveryStatus(
                deliveryId,
                status
        );
    }

    public void displayDelivery(int deliveryId) {

        Delivery delivery =
                deliveryService.findDeliveryById(deliveryId);

        if (delivery == null) {

            System.out.println("Delivery not found.");
            return;
        }

        delivery.displayDeliveryInfo();
    }
}