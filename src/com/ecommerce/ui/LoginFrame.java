package com.ecommerce.ui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import com.ecommerce.model.Customer;
import com.ecommerce.service.AuthenticationService;
import com.ecommerce.service.CartService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderProcessingService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;

public class LoginFrame extends JFrame {

    private final JTextField emailField;
    private final JPasswordField passwordField;
    private final JButton loginButton;
    private final JButton closeButton;

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;

    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderProcessingService orderProcessingService;

    public LoginFrame(
            UserService userService,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            CartService cartService,
            InventoryService inventoryService,
            OrderProcessingService orderProcessingService) {

        this.userService = userService;

        this.authenticationService =
                new AuthenticationService(userService);

        this.productService =
                productService;

        this.orderService =
                orderService;

        this.notificationService =
                notificationService;

        this.cartService =
                cartService;

        this.inventoryService =
                inventoryService;

        this.orderProcessingService =
                orderProcessingService;

        setTitle("Customer Login");

        setSize(400, 250);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { returnToMain(); }
        });

        setLayout(new GridLayout(4, 2, 10, 10));

        emailField =
                new JTextField();

        passwordField =
                new JPasswordField();

        loginButton = new JButton("Login");
        closeButton = new JButton("Close");

        add(
                new JLabel("Email:")
        );

        add(emailField);

        add(
                new JLabel("Password:")
        );

        add(passwordField);

        add(loginButton);
        add(closeButton);

        loginButton.addActionListener(e -> loginCustomer());
        closeButton.addActionListener(e -> returnToMain());
    }

    private void returnToMain() {
        MainFrame main = MainFrame.getInstance();
        if (main != null) main.returnToMainPage();
        dispose();
    }

    private void loginCustomer() {

        try {

            String email =
                    emailField
                            .getText()
                            .trim();

            String password =
                    new String(
                            passwordField.getPassword()
                    );

            if (email.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your email."
                );

                return;
            }

            if (password.isEmpty()) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter your password."
                );

                return;
            }

            Customer customer =
                    authenticationService.loginCustomer(
                            email,
                            password
                    );

            if (customer == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Invalid email or password.",
                        "Login Failed",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            CustomerDashboardFrame dashboard =
                    new CustomerDashboardFrame(
                            customer,
                            userService,
                            productService,
                            orderService,
                            notificationService,
                            cartService,
                            inventoryService,
                            orderProcessingService
                    );

            dashboard.setVisible(true);

            dispose();

        } catch (Exception ex) {

            ex.printStackTrace();

            JOptionPane.showMessageDialog(
                    this,
                    "Login could not be completed.\n\n"
                            + ex.getMessage(),
                    "Application Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }
}