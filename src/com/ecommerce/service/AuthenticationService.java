package com.ecommerce.service;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Customer;
import com.ecommerce.model.User;

public class AuthenticationService {

    private final UserService userService;

    public AuthenticationService(
            UserService userService) {

        this.userService = userService;
    }

    // =========================================================
    // CUSTOMER LOGIN
    // =========================================================

    public Customer loginCustomer(
            String email,
            String password) {

        if (userService == null) {

            System.out.println(
                    "User service not available."
            );

            return null;
        }

        if (email == null
                || email.trim().isEmpty()) {

            System.out.println(
                    "Email cannot be empty."
            );

            return null;
        }

        if (password == null
                || password.isEmpty()) {

            System.out.println(
                    "Password cannot be empty."
            );

            return null;
        }

        Customer customer =
                userService.findCustomerByEmail(
                        email.trim()
                );

        if (customer == null) {

            System.out.println(
                    "Customer not found."
            );

            return null;
        }

        if (customer.getPassword() == null
                || !customer.getPassword()
                .equals(password)) {

            System.out.println(
                    "Invalid password."
            );

            return null;
        }

        System.out.println(
                "Login successful. Welcome, "
                        + customer.getName()
                        + "!"
        );

        return customer;
    }

    // =========================================================
    // ADMIN LOGIN
    // =========================================================

    public Admin loginAdmin(
            int adminId,
            String email) {

        if (userService == null) {

            System.out.println(
                    "User service not available."
            );

            return null;
        }

        if (email == null
                || email.trim().isEmpty()) {

            System.out.println(
                    "Email cannot be empty."
            );

            return null;
        }

        User user =
                userService.findUserById(
                        adminId
                );

        if (!(user instanceof Admin)) {

            System.out.println(
                    "Admin not found."
            );

            return null;
        }

        if (user.getEmail() == null
                || !user.getEmail()
                .equalsIgnoreCase(
                        email.trim()
                )) {

            System.out.println(
                    "Invalid email."
            );

            return null;
        }

        System.out.println(
                "Admin login successful. Welcome, "
                        + user.getName()
                        + "!"
        );

        return (Admin) user;
    }

    // =========================================================
    // USER LOGIN CHECK
    // =========================================================

    public boolean isLoggedIn(
            User user) {

        return user != null;
    }

    // =========================================================
    // USER TYPE CHECK
    // =========================================================

    public boolean isCustomer(
            User user) {

        return user instanceof Customer;
    }

    public boolean isAdmin(
            User user) {

        return user instanceof Admin;
    }
}