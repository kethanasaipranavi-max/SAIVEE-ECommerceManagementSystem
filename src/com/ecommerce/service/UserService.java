package com.ecommerce.service;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Customer;
import com.ecommerce.model.User;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

public class UserService {

    private final List<User> users;

    private int nextCustomerId = 1001;

    private static final Path CUSTOMER_FILE =
            Paths.get("data", "customers.dat");

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public UserService() {

        users = new ArrayList<>();

        loadCustomers();
    }

    // =========================================================
    // ADD USER
    // =========================================================

    public synchronized boolean addUser(
            User user) {

        if (user == null) {

            System.out.println(
                    "Invalid user."
            );

            return false;
        }

        if (findUserById(
                user.getUserId()
        ) != null) {

            System.out.println(
                    "User ID already exists."
            );

            return false;
        }

        if (user.getEmail() != null
                && !user.getEmail().isBlank()
                && findUserByEmail(
                user.getEmail()
        ) != null) {

            System.out.println(
                    "Email is already registered."
            );

            return false;
        }

        users.add(user);

        if (user instanceof Customer) {

            Customer customer =
                    (Customer) user;

            if (customer.getUserId()
                    >= nextCustomerId) {

                nextCustomerId =
                        customer.getUserId() + 1;
            }

            saveCustomers();
        }

        System.out.println(
                "User added successfully."
        );

        return true;
    }

    // =========================================================
    // CUSTOMER REGISTRATION
    // =========================================================

    public synchronized Customer registerCustomer(
            String name,
            String email,
            String phone,
            String password) {

        if (name == null
                || name.trim().isEmpty()) {

            System.out.println(
                    "Name cannot be empty."
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

        if (phone == null
                || phone.trim().isEmpty()) {

            System.out.println(
                    "Phone cannot be empty."
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

        name = name.trim();
        email = email.trim();
        phone = phone.trim();

        if (findUserByEmail(email) != null) {

            System.out.println(
                    "Email is already registered."
            );

            return null;
        }

        Customer customer =
                new Customer(
                        nextCustomerId,
                        name,
                        email,
                        phone,
                        password
                );

        nextCustomerId++;

        users.add(customer);

        saveCustomers();

        System.out.println(
                "Customer registered successfully."
        );

        System.out.println(
                "Generated Customer ID: "
                        + customer.getUserId()
        );

        System.out.println(
                "Customer information saved permanently."
        );

        return customer;
    }

    // =========================================================
    // FIND USER BY ID
    // =========================================================

    public synchronized User findUserById(
            int userId) {

        for (User user :
                users) {

            if (user != null
                    && user.getUserId()
                    == userId) {

                return user;
            }
        }

        return null;
    }

    // =========================================================
    // FIND USER BY EMAIL
    // =========================================================

    public synchronized User findUserByEmail(
            String email) {

        if (email == null
                || email.trim().isEmpty()) {

            return null;
        }

        String searchEmail =
                email.trim();

        for (User user :
                users) {

            if (user != null
                    && user.getEmail() != null
                    && user.getEmail()
                    .equalsIgnoreCase(
                            searchEmail
                    )) {

                return user;
            }
        }

        return null;
    }

    // =========================================================
    // FIND CUSTOMER BY EMAIL
    // =========================================================

    public synchronized Customer findCustomerByEmail(
            String email) {

        User user =
                findUserByEmail(email);

        if (user instanceof Customer) {

            return (Customer) user;
        }

        return null;
    }

    // =========================================================
    // FIND CUSTOMER BY ID
    // =========================================================

    public synchronized Customer findCustomerById(
            int customerId) {

        User user =
                findUserById(customerId);

        if (user instanceof Customer) {

            return (Customer) user;
        }

        return null;
    }

    // =========================================================
    // FIND ADMIN
    // =========================================================

    public synchronized Admin findAdminById(
            int adminId) {

        User user =
                findUserById(adminId);

        if (user instanceof Admin) {

            return (Admin) user;
        }

        return null;
    }

    // =========================================================
    // DISPLAY USERS
    // =========================================================

    public synchronized void displayAllUsers() {

        if (users.isEmpty()) {

            System.out.println(
                    "No users available."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE USERS ====="
        );

        for (User user :
                users) {

            if (user == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            user.displayUserInfo();
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // USER COUNT
    // =========================================================

    public synchronized int getUserCount() {

        return users.size();
    }

    // =========================================================
    // GET ALL USERS
    // =========================================================

    public synchronized List<User> getUsers() {

        return Collections.unmodifiableList(
                new ArrayList<>(
                        users
                )
        );
    }

    public synchronized void saveCustomerVerification(Customer customer) {
        if (customer == null || findCustomerById(customer.getUserId()) == null) return;
        saveCustomers();
    }

    // =========================================================
    // SAVE CUSTOMERS
    // =========================================================

    private synchronized void saveCustomers() {

        try {

            Path parent =
                    CUSTOMER_FILE.getParent();

            if (parent != null) {

                Files.createDirectories(
                        parent
                );
            }

            try (BufferedWriter writer =
                         Files.newBufferedWriter(
                                 CUSTOMER_FILE,
                                 StandardCharsets.UTF_8
                         )) {

                for (User user :
                        users) {

                    if (!(user instanceof Customer)) {
                        continue;
                    }

                    Customer customer =
                            (Customer) user;

                    String line =
                            customer.getUserId()
                                    + "|"
                                    + encode(
                                    customer.getName()
                            )
                                    + "|"
                                    + encode(
                                    customer.getEmail()
                            )
                                    + "|"
                                    + encode(
                                    customer.getPhone()
                            )
                                    + "|"
                                    + encode(
                                    customer.getPassword()
                            )
                                    + "|"
                                    + customer.isEmailVerified();

                    writer.write(line);
                    writer.newLine();
                }
            }

        } catch (IOException e) {

            System.out.println(
                    "Unable to save customer data."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // LOAD CUSTOMERS
    // =========================================================

    private void loadCustomers() {

        if (!Files.exists(
                CUSTOMER_FILE
        )) {

            System.out.println(
                    "No previous customer data found."
            );

            return;
        }

        try (BufferedReader reader =
                     Files.newBufferedReader(
                             CUSTOMER_FILE,
                             StandardCharsets.UTF_8
                     )) {

            String line;

            while ((line = reader.readLine())
                    != null) {

                if (line.trim().isEmpty()) {
                    continue;
                }

                String[] parts =
                        line.split(
                                "\\|",
                                -1
                        );

                if (parts.length != 5 && parts.length != 6) {

                    System.out.println(
                            "Skipped invalid customer record."
                    );

                    continue;
                }

                try {

                    int customerId =
                            Integer.parseInt(
                                    parts[0]
                            );

                    String name =
                            decode(parts[1]);

                    String email =
                            decode(parts[2]);

                    String phone =
                            decode(parts[3]);

                    String password =
                            decode(parts[4]);

                    if (findUserById(
                            customerId
                    ) != null) {

                        continue;
                    }

                    Customer customer =
                            new Customer(
                                    customerId,
                                    name,
                                    email,
                                    phone,
                                    password
                            );
                    if (parts.length == 6) customer.setEmailVerified(Boolean.parseBoolean(parts[5]));

                    users.add(customer);

                    if (customerId
                            >= nextCustomerId) {

                        nextCustomerId =
                                customerId + 1;
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Skipped invalid customer record."
                    );
                }
            }

            System.out.println(
                    "Saved customers loaded: "
                            + users.size()
            );

        } catch (IOException e) {

            System.out.println(
                    "Unable to load customer data."
            );

            e.printStackTrace();
        }
    }

    // =========================================================
    // ENCODING
    // =========================================================

    private String encode(
            String value) {

        if (value == null) {
            return "";
        }

        return Base64.getEncoder()
                .encodeToString(
                        value.getBytes(
                                StandardCharsets.UTF_8
                        )
                );
    }

    // =========================================================
    // DECODING
    // =========================================================

    private String decode(
            String value) {

        if (value == null
                || value.isEmpty()) {

            return "";
        }

        return new String(
                Base64.getDecoder().decode(value),
                StandardCharsets.UTF_8
        );
    }
}