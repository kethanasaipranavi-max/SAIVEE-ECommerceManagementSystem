package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;

public class CustomerService {

    private final UserService userService;
    private final OrderService orderService;

    public CustomerService(
            UserService userService,
            OrderService orderService) {

        this.userService = userService;
        this.orderService = orderService;
    }

    // =========================================================
    // FIND CUSTOMER
    // =========================================================

    public Customer findCustomer(
            int customerId) {

        if (userService == null) {
            return null;
        }

        return userService.findCustomerById(
                customerId
        );
    }

    // =========================================================
    // DISPLAY CUSTOMER PROFILE
    // =========================================================

    public void displayCustomerProfile(
            int customerId) {

        Customer customer =
                findCustomer(customerId);

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        System.out.println(
                "===== CUSTOMER PROFILE ====="
        );

        customer.displayUserInfo();
    }

    // =========================================================
    // DISPLAY ORDER HISTORY
    // =========================================================

    public void displayOrderHistory(
            int customerId) {

        Customer customer =
                findCustomer(customerId);

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return;
        }

        if (orderService == null) {

            System.out.println(
                    "Order service not available."
            );

            return;
        }

        orderService.displayCustomerOrders(
                customer
        );
    }

    // =========================================================
    // CHECK ORDER ACCESS
    // =========================================================

    public boolean canAccessOrder(
            int customerId,
            Order order) {

        Customer customer =
                findCustomer(customerId);

        if (customer == null
                || order == null
                || order.getCustomer() == null) {

            return false;
        }

        return order.getCustomer()
                .getUserId()
                == customer.getUserId();
    }

    // =========================================================
    // CHECK CUSTOMER EXISTS
    // =========================================================

    public boolean customerExists(
            int customerId) {

        return findCustomer(
                customerId
        ) != null;
    }
}