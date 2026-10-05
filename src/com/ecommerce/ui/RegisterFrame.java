package com.ecommerce.ui;

import com.ecommerce.model.Customer;
import com.ecommerce.service.CartService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderProcessingService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.EmailVerificationService;
import com.ecommerce.util.AppConstants;
import com.ecommerce.util.PasswordUtil;

import javax.swing.*;
import java.awt.*;

public class RegisterFrame extends JFrame {

    private final UserService userService;

    private final JTextField nameField;
    private final JTextField emailField;
    private final JTextField phoneField;

    private final JPasswordField passwordField;
    private final JPasswordField confirmPasswordField;

    private final JButton registerButton;
    private final JButton closeButton;

    public RegisterFrame(
            UserService userService,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            CartService cartService,
            InventoryService inventoryService,
            OrderProcessingService orderProcessingService) {

        this.userService = userService;

        setTitle("SAIVEE - Customer Registration");
        setSize(520, 420);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        20, 25, 20, 25
                )
        );

        JLabel titleLabel = new JLabel(
                "Create Your SAIVEE Account",
                SwingConstants.CENTER
        );

        titleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitleLabel = new JLabel(
                "Register to start shopping with SAIVEE",
                SwingConstants.CENTER
        );

        subtitleLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        13
                )
        );

        JPanel headerPanel = new JPanel(
                new GridLayout(2, 1, 0, 4)
        );

        headerPanel.add(titleLabel);
        headerPanel.add(subtitleLabel);

        mainPanel.add(
                headerPanel,
                BorderLayout.NORTH
        );

        nameField = new JTextField();
        emailField = new JTextField();
        phoneField = new JTextField();

        passwordField = new JPasswordField();
        confirmPasswordField = new JPasswordField();

        registerButton = new JButton("Create Account");
        closeButton = new JButton("Close");

        registerButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.BOLD,
                        14
                )
        );

        closeButton.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        14
                )
        );

        JPanel formPanel = new JPanel(
                new GridLayout(
                        6,
                        2,
                        12,
                        12
                )
        );

        formPanel.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createTitledBorder(
                                "Account Details"
                        ),
                        BorderFactory.createEmptyBorder(
                                15,
                                15,
                                15,
                                15
                        )
                )
        );

        formPanel.add(
                new JLabel("Full Name:")
        );
        formPanel.add(nameField);

        formPanel.add(
                new JLabel("Email:")
        );
        formPanel.add(emailField);

        formPanel.add(
                new JLabel("Phone:")
        );
        formPanel.add(phoneField);

        formPanel.add(
                new JLabel("Password:")
        );
        formPanel.add(passwordField);

        formPanel.add(
                new JLabel("Confirm Password:")
        );
        formPanel.add(confirmPasswordField);

        formPanel.add(registerButton);
        formPanel.add(closeButton);

        mainPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        JLabel footerLabel = new JLabel(
                "Password: minimum length + at least one letter and one digit",
                SwingConstants.CENTER
        );

        footerLabel.setFont(
                new Font(
                        "Segoe UI",
                        Font.PLAIN,
                        11
                )
        );

        mainPanel.add(
                footerLabel,
                BorderLayout.SOUTH
        );

        add(mainPanel);

        registerButton.addActionListener(
                e -> register()
        );

        closeButton.addActionListener(
                e -> returnToMain()
        );

        getRootPane().setDefaultButton(
                registerButton
        );
    }

    private void register() {

        String name =
                nameField.getText().trim();

        String email =
                emailField.getText().trim();

        String phone =
                phoneField.getText().trim();

        String password =
                new String(
                        passwordField.getPassword()
                );

        String confirmPassword =
                new String(
                        confirmPasswordField.getPassword()
                );

        // =========================
        // VALIDATION
        // =========================

        if (name.isEmpty()
                || email.isEmpty()
                || phone.isEmpty()
                || password.isEmpty()
                || confirmPassword.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please fill all fields.",
                    "Missing Information",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (name.length() < 2) {

            JOptionPane.showMessageDialog(
                    this,
                    "Name must contain at least 2 characters.",
                    "Invalid Name",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!email.matches(
                "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid email address.",
                    "Invalid Email",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!phone.matches("\\d{10}")) {

            JOptionPane.showMessageDialog(
                    this,
                    "Phone number must contain exactly 10 digits.",
                    "Invalid Phone",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!PasswordUtil.isValidPassword(password)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Password must contain at least "
                            + "one letter and one digit "
                            + "and be at least "
                            + AppConstants.MIN_PASSWORD_LENGTH
                            + " characters long.",
                    "Invalid Password",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!PasswordUtil.passwordsMatch(
                password,
                confirmPassword)) {

            JOptionPane.showMessageDialog(
                    this,
                    "Passwords do not match.",
                    "Password Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =========================
        // CHECK DUPLICATE EMAIL
        // =========================

        if (userService.findUserByEmail(email) != null) {

            JOptionPane.showMessageDialog(
                    this,
                    "This email is already registered.",
                    "Registration Failed",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =========================
        // EMAIL OTP VERIFICATION
        // =========================
        EmailVerificationService verificationService = EmailVerificationService.getInstance();
        if (!verificationService.sendOtp(email, name)) {
            JOptionPane.showMessageDialog(this,
                    "We could not send the verification code. Check your email address and SAIVEE email settings.",
                    "Email Verification", JOptionPane.ERROR_MESSAGE);
            return;
        }
        String otp = JOptionPane.showInputDialog(this,
                "A 6-digit verification code was sent to:\n" + email + "\n\nEnter the code:",
                "Verify Email", JOptionPane.QUESTION_MESSAGE);
        if (otp == null || !verificationService.verify(email, otp)) {
            JOptionPane.showMessageDialog(this,
                    "Email verification failed or the code expired. Please register again and request a new code.",
                    "Verification Failed", JOptionPane.ERROR_MESSAGE);
            return;
        }

        // =========================
        // CREATE CUSTOMER
        // =========================

        Customer customer =
                userService.registerCustomer(
                        name,
                        email,
                        phone,
                        password
                );

        if (customer == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Registration failed.",
                    "Registration Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        customer.setEmailVerified(true);
        userService.saveCustomerVerification(customer);

        JOptionPane.showMessageDialog(
                this,
                "Registration successful!\n\n"
                        + "Welcome, "
                        + customer.getName()
                        + "!\n\n"
                        + "You can now login using:\n"
                        + "Email: "
                        + customer.getEmail(),
                "Registration Successful",
                JOptionPane.INFORMATION_MESSAGE
        );

        clearFields();
        returnToMain();
    }

    private void returnToMain() {
        MainFrame main = MainFrame.getInstance();
        if (main != null) main.returnToMainPage();
        dispose();
    }

    private void clearFields() {

        nameField.setText("");
        emailField.setText("");
        phoneField.setText("");
        passwordField.setText("");
        confirmPasswordField.setText("");
    }
}