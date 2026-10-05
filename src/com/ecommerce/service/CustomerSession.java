package com.ecommerce.service;

import com.ecommerce.model.Customer;

public class CustomerSession {

    private Customer loggedInCustomer;

    public CustomerSession() {
        loggedInCustomer = null;
    }

    public void login(Customer customer) {

        if (customer == null) {
            System.out.println("Cannot create customer session.");
            return;
        }

        loggedInCustomer = customer;

        System.out.println(
                "Customer session started for: "
                + customer.getName()
        );
    }

    public void logout() {

        if (loggedInCustomer == null) {
            System.out.println("No active customer session.");
            return;
        }

        System.out.println(
                "Customer session ended for: "
                + loggedInCustomer.getName()
        );

        loggedInCustomer = null;
    }

    public Customer getLoggedInCustomer() {

        return loggedInCustomer;
    }

    public boolean isLoggedIn() {

        return loggedInCustomer != null;
    }
}