package com.ecommerce.model;

public class Customer extends User {

    private String phone;
    private String password;
    private boolean emailVerified;

    public Customer() {
        super();
    }

    public Customer(
            int customerId,
            String name,
            String email,
            String phone,
            String password) {

        super(
                customerId,
                name,
                email
        );

        this.phone = phone;
        this.password = password;
        this.emailVerified = false;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public boolean isEmailVerified() { return emailVerified; }

    public void setEmailVerified(boolean emailVerified) { this.emailVerified = emailVerified; }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public void displayUserInfo() {

        super.displayUserInfo();

        System.out.println(
                "Phone: " + phone
        );
    }

    @Override
    public String toString() {

        return "Customer ID: " + userId
                + "\nName: " + name
                + "\nEmail: " + email
                + "\nPhone: " + phone;
    }
}