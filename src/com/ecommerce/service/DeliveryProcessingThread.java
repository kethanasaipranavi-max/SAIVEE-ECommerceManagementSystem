package com.ecommerce.service;

import com.ecommerce.model.DeliveryStatus;

public class DeliveryProcessingThread extends Thread {

    private final DeliveryProcessingService deliveryProcessingService;
    private final int deliveryId;
    private final DeliveryStatus status;

    public DeliveryProcessingThread(
            DeliveryProcessingService deliveryProcessingService,
            int deliveryId,
            DeliveryStatus status) {

        this.deliveryProcessingService = deliveryProcessingService;
        this.deliveryId = deliveryId;
        this.status = status;
    }

    @Override
    public void run() {

        System.out.println(
                "Updating delivery in thread: "
                + Thread.currentThread().getName()
        );

        deliveryProcessingService.updateDeliveryStatus(
                deliveryId,
                status
        );

        System.out.println(
                "Delivery processing completed."
        );
    }
}