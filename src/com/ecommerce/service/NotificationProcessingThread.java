package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.NotificationType;

public class NotificationProcessingThread extends Thread {

    private final NotificationProcessingService notificationService;
    private final int notificationId;
    private final Customer customer;
    private final NotificationType type;
    private final String message;

    public NotificationProcessingThread(
            NotificationProcessingService notificationService,
            int notificationId,
            Customer customer,
            NotificationType type,
            String message) {

        this.notificationService = notificationService;
        this.notificationId = notificationId;
        this.customer = customer;
        this.type = type;
        this.message = message;
    }

    @Override
    public void run() {

        System.out.println(
                "Sending notification in thread: "
                + Thread.currentThread().getName()
        );

        notificationService.send(
                notificationId,
                customer,
                type,
                message
        );

        System.out.println(
                "Notification processing completed."
        );
    }
}