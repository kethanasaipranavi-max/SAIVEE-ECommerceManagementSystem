package com.ecommerce.ui;

import com.ecommerce.model.Admin;
import com.ecommerce.service.AdminService;
import com.ecommerce.service.AuthenticationService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReportService;
import com.ecommerce.service.UserService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class AdminLoginFrame extends JFrame {

    private final JTextField adminIdField;
    private final JTextField emailField;

    private final JButton loginButton;
    private final JButton closeButton;

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final InventoryService inventoryService;
    private final ReportService reportService;

    public AdminLoginFrame(
            UserService userService,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            InventoryService inventoryService,
            ReportService reportService) {

        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.inventoryService = inventoryService;
        this.reportService = reportService;

        setTitle("SAIVEE - Admin Login");
        setSize(450, 250);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);
        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { returnToMain(); }
        });

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "SAIVEE ADMIN LOGIN",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        add(
                titleLabel,
                BorderLayout.NORTH
        );

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                3,
                                2,
                                10,
                                10
                        )
                );

        panel.setBorder(
                BorderFactory.createEmptyBorder(
                        20,
                        30,
                        20,
                        30
                )
        );

        adminIdField = new JTextField();
        emailField = new JTextField();

        loginButton =
                new JButton("Login");

        closeButton =
                new JButton("Close");

        panel.add(
                new JLabel("Admin ID:")
        );

        panel.add(
                adminIdField
        );

        panel.add(
                new JLabel("Email:")
        );

        panel.add(
                emailField
        );

        panel.add(
                loginButton
        );

        panel.add(
                closeButton
        );

        add(
                panel,
                BorderLayout.CENTER
        );

        loginButton.addActionListener(
                e -> login()
        );

        closeButton.addActionListener(
                e -> returnToMain()
        );
    }

    private void returnToMain() {
        MainFrame main = MainFrame.getInstance();
        if (main != null) main.returnToMainPage();
        dispose();
    }

    private void login() {

        try {

            String adminIdText =
                    adminIdField
                            .getText()
                            .trim();

            String email =
                    emailField
                            .getText()
                            .trim();

            if (adminIdText.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter Admin ID."
                );

                return;
            }

            if (email.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter admin email."
                );

                return;
            }

            int adminId;

            try {

                adminId =
                        Integer.parseInt(
                                adminIdText
                        );

            } catch (NumberFormatException e) {

                JOptionPane.showMessageDialog(
                        this,
                        "Admin ID must be a number.",
                        "Invalid Admin ID",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            AuthenticationService
                    authenticationService =
                    new AuthenticationService(
                            userService
                    );

            Admin admin =
                    authenticationService.loginAdmin(
                            adminId,
                            email
                    );

            if (admin == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid admin ID or email.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            AdminService adminService =
                    new AdminService(
                            userService,
                            productService,
                            inventoryService,
                            orderService
                    );

            AdminDashboardFrame dashboard =
                    new AdminDashboardFrame(
                            admin,
                            userService,
                            adminService,
                            productService,
                            inventoryService,
                            orderService,
                            notificationService,
                            reportService
                    );

            dashboard.setVisible(true);

            dispose();

        } catch (Exception e) {

            e.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Admin login could not be completed.\n\n"
                            + e.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}