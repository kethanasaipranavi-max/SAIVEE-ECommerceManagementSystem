package com.ecommerce.service;

import com.ecommerce.model.Admin;

public class AdminSession {

    private Admin loggedInAdmin;

    public AdminSession() {
        loggedInAdmin = null;
    }

    public void login(Admin admin) {

        if (admin == null) {
            System.out.println("Cannot create admin session.");
            return;
        }

        loggedInAdmin = admin;

        System.out.println(
                "Admin session started for: "
                + admin.getName()
        );
    }

    public void logout() {

        if (loggedInAdmin == null) {
            System.out.println("No active admin session.");
            return;
        }

        System.out.println(
                "Admin session ended for: "
                + loggedInAdmin.getName()
        );

        loggedInAdmin = null;
    }

    public Admin getLoggedInAdmin() {

        return loggedInAdmin;
    }

    public boolean isLoggedIn() {

        return loggedInAdmin != null;
    }
}