package com.ecommerce.model;

public class Admin extends User {

    private String role;

    public Admin() {
        super();
    }

    public Admin(int adminId, String name,
                 String email, String role) {

        super(adminId, name, email);
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    @Override
    public void displayUserInfo() {

        super.displayUserInfo();
        System.out.println("Role: " + role);
    }

    @Override
    public String toString() {

        return "Admin ID: " + userId +
                "\nName: " + name +
                "\nEmail: " + email +
                "\nRole: " + role;
    }
}