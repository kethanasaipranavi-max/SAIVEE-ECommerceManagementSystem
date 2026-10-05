package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.NotificationType;

public class NotificationProcessingService {

    private final NotificationService notificationService;

    public NotificationProcessingService(
            NotificationService notificationService) {

        this.notificationService = notificationService;
    }

    public void notifyOrderPlaced(int notificationId,
                                  Customer customer,
                                  int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.ORDER_PLACED,
                "Order #" + orderId + " has been placed successfully."
        );
    }

    public void notifyPaymentSuccess(int notificationId,
                                     Customer customer,
                                     int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.PAYMENT_SUCCESS,
                "Payment for order #" + orderId
                        + " was successful."
        );
    }

    public void notifyPaymentFailed(int notificationId,
                                    Customer customer,
                                    int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.PAYMENT_FAILED,
                "Payment for order #" + orderId
                        + " has failed."
        );
    }

    public void notifyOrderConfirmed(int notificationId,
                                     Customer customer,
                                     int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.ORDER_CONFIRMED,
                "Order #" + orderId + " has been confirmed."
        );
    }

    public void notifyOrderShipped(int notificationId,
                                   Customer customer,
                                   int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.ORDER_SHIPPED,
                "Order #" + orderId + " has been shipped."
        );
    }

    public void notifyOrderDelivered(int notificationId,
                                     Customer customer,
                                     int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.ORDER_DELIVERED,
                "Order #" + orderId + " has been delivered."
        );
    }

    public void notifyOrderCancelled(int notificationId,
                                     Customer customer,
                                     int orderId) {

        send(
                notificationId,
                customer,
                NotificationType.ORDER_CANCELLED,
                "Order #" + orderId + " has been cancelled."
        );
    }

    public void notifyLowStock(int notificationId,
                               Customer customer,
                               String productName) {

        send(
                notificationId,
                customer,
                NotificationType.LOW_STOCK,
                "Low stock alert for product: " + productName
        );
    }

    public void send(int notificationId,
                     Customer customer,
                     NotificationType type,
                     String message) {

        if (customer == null) {

            System.out.println(
                    "Cannot send notification: customer not found."
            );

            return;
        }

        notificationService.sendNotification(
                notificationId,
                customer,
                type,
                message
        );
    }
}