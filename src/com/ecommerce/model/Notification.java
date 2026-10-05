package com.ecommerce.model;

public class Notification {

    private int notificationId;
    private Customer customer;
    private NotificationType type;
    private String message;
    private boolean read;

    public Notification() {
        read = false;
    }

    public Notification(int notificationId,
                        Customer customer,
                        NotificationType type,
                        String message) {

        this.notificationId = notificationId;
        this.customer = customer;
        this.type = type;
        this.message = message;
        this.read = false;
    }

    public int getNotificationId() {
        return notificationId;
    }

    public Customer getCustomer() {
        return customer;
    }

    public NotificationType getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }

    public boolean isRead() {
        return read;
    }

    public void markAsRead() {

        read = true;
    }

    public void displayNotification() {

        System.out.println("----------------------------");
        System.out.println("Notification ID: " + notificationId);
        System.out.println("Customer: " + customer.getName());
        System.out.println("Type: " + type);
        System.out.println("Message: " + message);
        System.out.println("Read: " + read);
    }

    @Override
    public String toString() {

        return "Notification ID: " + notificationId +
                "\nCustomer: " + customer.getName() +
                "\nType: " + type +
                "\nMessage: " + message +
                "\nRead: " + read;
    }
}