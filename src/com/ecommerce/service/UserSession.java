package com.ecommerce.service;

import com.ecommerce.model.User;

public class UserSession {

    private User loggedInUser;

    public UserSession() {
        loggedInUser = null;
    }

    // =========================================================
    // LOGIN
    // =========================================================

    public void login(User user) {

        if (user == null) {

            System.out.println(
                    "Cannot create session."
            );

            return;
        }

        loggedInUser = user;

        System.out.println(
                "Session started for: "
                        + user.getName()
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    public void logout() {

        if (loggedInUser == null) {

            System.out.println(
                    "No active session."
            );

            return;
        }

        System.out.println(
                "Session ended for: "
                        + loggedInUser.getName()
        );

        loggedInUser = null;
    }

    // =========================================================
    // GET LOGGED-IN USER
    // =========================================================

    public User getLoggedInUser() {

        return loggedInUser;
    }

    // =========================================================
    // SESSION STATUS
    // =========================================================

    public boolean isLoggedIn() {

        return loggedInUser != null;
    }

    // =========================================================
    // CHECK CURRENT USER
    // =========================================================

    public boolean isCurrentUser(
            int userId) {

        return loggedInUser != null
                && loggedInUser.getUserId()
                == userId;
    }
}