package com.ecommerce.service;

import com.ecommerce.model.Notification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationRepository {

    private final Map<Integer, Notification> notifications;

    public NotificationRepository() {
        notifications = new HashMap<>();
    }

    public boolean save(Notification notification) {

        if (notification == null) {
            return false;
        }

        notifications.put(
                notification.getNotificationId(),
                notification
        );

        return true;
    }

    public Notification findById(int notificationId) {

        return notifications.get(notificationId);
    }

    public boolean exists(int notificationId) {

        return notifications.containsKey(notificationId);
    }

    public Notification removeById(int notificationId) {

        return notifications.remove(notificationId);
    }

    public List<Notification> findAll() {

        return new ArrayList<>(notifications.values());
    }

    public List<Notification> findByCustomerId(int customerId) {

        List<Notification> customerNotifications =
                new ArrayList<>();

        for (Notification notification : notifications.values()) {

            if (notification.getCustomer().getUserId()
                    == customerId) {

                customerNotifications.add(notification);
            }
        }

        return customerNotifications;
    }

    public int count() {

        return notifications.size();
    }

    public void clear() {

        notifications.clear();
    }
}