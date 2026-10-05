package com.ecommerce.ui;

import com.ecommerce.service.CartService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderProcessingService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReportService;
import com.ecommerce.service.UserService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class MainFrame extends JFrame {

    private static MainFrame instance;
    public static MainFrame getInstance() { return instance; }

    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderProcessingService orderProcessingService;
    private final ReportService reportService;

    private static final Color NAVY = new Color(13, 45, 82);
    private static final Color DARK_BLUE = new Color(25, 70, 125);
    private static final Color ROYAL_BLUE = new Color(40, 90, 180);
    private static final Color BLUE = new Color(45, 120, 210);
    private static final Color SKY_BLUE = new Color(80, 160, 230);
    private static final Color EXIT_RED = new Color(190, 55, 65);
    private static final Color PALE_BLUE = new Color(240, 248, 255);

    public MainFrame(
            UserService userService,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            CartService cartService,
            InventoryService inventoryService,
            OrderProcessingService orderProcessingService,
            ReportService reportService) {

        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.orderProcessingService = orderProcessingService;
        this.reportService = reportService;
        instance = this;

        setTitle(
                "SAIVEE - E-Commerce Order & Inventory Management System");

        setSize(1150, 780);

        /*
         * IMPORTANT:
         * Only the MAIN SAIVEE window exits the complete application.
         * Other windows use DISPOSE_ON_CLOSE.
         */
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLocationRelativeTo(null);
        setResizable(false);

        createMainPage();
    }

    private void createMainPage() {

        JPanel rootPanel =
                new JPanel(new BorderLayout());

        rootPanel.setBackground(PALE_BLUE);

        // ================= HEADER =================

        JPanel headerPanel =
                new JPanel(new BorderLayout());

        headerPanel.setBackground(NAVY);

        headerPanel.setBorder(
                new EmptyBorder(
                        25, 40, 25, 40));

        JPanel logoPanel =
                new JPanel(new GridLayout(2, 1));

        logoPanel.setOpaque(false);

        JLabel logoLabel =
                new JLabel("SAIVEE");

        logoLabel.setForeground(Color.WHITE);

        logoLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        40));

        JLabel taglineLabel =
                new JLabel(
                        "Smart E-Commerce • Simple Management");

        taglineLabel.setForeground(
                new Color(210, 230, 250));

        taglineLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15));

        logoPanel.add(logoLabel);
        logoPanel.add(taglineLabel);

        JLabel systemLabel =
                new JLabel(
                        "<html><div style='text-align:right;'>"
                                + "<b>ORDER & INVENTORY</b><br>"
                                + "MANAGEMENT SYSTEM"
                                + "</div></html>");

        systemLabel.setForeground(Color.WHITE);

        systemLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16));

        headerPanel.add(
                logoPanel,
                BorderLayout.WEST);

        headerPanel.add(
                systemLabel,
                BorderLayout.EAST);

        rootPanel.add(
                headerPanel,
                BorderLayout.NORTH);

        // ================= CENTER =================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout(20, 20));

        centerPanel.setBackground(PALE_BLUE);

        centerPanel.setBorder(
                new EmptyBorder(
                        30, 45, 25, 45));

        JLabel welcomeLabel =
                new JLabel(
                        "<html><div style='text-align:center;'>"
                                + "<font size='6'>Welcome to SAIVEE</font><br>"
                                + "<font size='4'>"
                                + "Your Complete E-Commerce Management Platform"
                                + "</font>"
                                + "</div></html>",
                        SwingConstants.CENTER);

        welcomeLabel.setForeground(NAVY);

        centerPanel.add(
                welcomeLabel,
                BorderLayout.NORTH);

        // ================= INFO CARDS =================

        JPanel infoPanel =
                new JPanel(
                        new GridLayout(
                                1, 3, 20, 20));

        infoPanel.setOpaque(false);

        infoPanel.add(
                createInfoCard(
                        "SHOP",
                        "Browse products,\n"
                                + "manage your cart\n"
                                + "and place orders.",
                        DARK_BLUE));

        infoPanel.add(
                createInfoCard(
                        "MANAGE",
                        "Manage products,\n"
                                + "inventory, orders\n"
                                + "and customers.",
                        ROYAL_BLUE));

        infoPanel.add(
                createInfoCard(
                        "TRACK",
                        "Track orders,\n"
                                + "inventory levels\n"
                                + "and reports.",
                        BLUE));

        centerPanel.add(
                infoPanel,
                BorderLayout.CENTER);

        // ================= ACTION BUTTONS =================

        JPanel actionPanel =
                new JPanel(
                        new GridLayout(
                                1, 5, 12, 12));

        actionPanel.setOpaque(false);

        JButton customerButton =
                createMainButton(
                        "CUSTOMER LOGIN",
                        DARK_BLUE);

        JButton registerButton =
                createMainButton(
                        "REGISTER",
                        ROYAL_BLUE);

        JButton adminButton =
                createMainButton(
                        "ADMIN LOGIN",
                        BLUE);

        JButton aboutButton =
                createMainButton(
                        "ABOUT",
                        SKY_BLUE);

        JButton exitButton =
                createMainButton(
                        "EXIT APPLICATION",
                        EXIT_RED);

        // ================= ACTIONS =================

        customerButton.addActionListener(
                e -> openCustomerLogin());

        registerButton.addActionListener(
                e -> openRegistration());

        adminButton.addActionListener(
                e -> openAdminLogin());

        aboutButton.addActionListener(
                e -> openAbout());

        exitButton.addActionListener(
                e -> exitApplication());

        actionPanel.add(customerButton);
        actionPanel.add(registerButton);
        actionPanel.add(adminButton);
        actionPanel.add(aboutButton);
        actionPanel.add(exitButton);

        centerPanel.add(
                actionPanel,
                BorderLayout.SOUTH);

        rootPanel.add(
                centerPanel,
                BorderLayout.CENTER);

        // ================= FOOTER =================

        JPanel footerPanel =
                new JPanel(new BorderLayout());

        footerPanel.setBackground(NAVY);

        footerPanel.setBorder(
                new EmptyBorder(
                        15, 30, 15, 30));

        JLabel footerLabel =
                new JLabel(
                        "SAIVEE • Shop Smart • Shop Simple • Shop Better",
                        SwingConstants.CENTER);

        footerLabel.setForeground(Color.WHITE);

        footerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        footerPanel.add(
                footerLabel,
                BorderLayout.CENTER);

        rootPanel.add(
                footerPanel,
                BorderLayout.SOUTH);

        add(rootPanel);
    }

    // ================= INFO CARD =================

    private JPanel createInfoCard(
            String title,
            String description,
            Color color) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10, 10));

        card.setBackground(color);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                2),
                        new EmptyBorder(
                                20, 20, 20, 20)));

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER);

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22));

        JTextArea descriptionArea =
                new JTextArea(description);

        descriptionArea.setEditable(false);
        descriptionArea.setFocusable(false);
        descriptionArea.setOpaque(false);
        descriptionArea.setForeground(Color.WHITE);

        descriptionArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15));

        descriptionArea.setLineWrap(true);
        descriptionArea.setWrapStyleWord(true);

        card.add(
                titleLabel,
                BorderLayout.NORTH);

        card.add(
                descriptionArea,
                BorderLayout.CENTER);

        return card;
    }

    // ================= MAIN BUTTON =================

    private JButton createMainButton(
            String text,
            Color background) {

        JButton button =
                new JButton(text);

        button.setBackground(background);
        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                2),
                        new EmptyBorder(
                                16, 8, 16, 8)));

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));

        return button;
    }

    // ================= CUSTOMER LOGIN =================

    private void openCustomerLogin() {

        LoginFrame loginFrame =
                new LoginFrame(
                        userService,
                        productService,
                        orderService,
                        notificationService,
                        cartService,
                        inventoryService,
                        orderProcessingService);

        loginFrame.setVisible(true);

        /*
         * Keep this SAME MainFrame alive.
         *
         * We hide it instead of disposing it.
         * Therefore the shared services remain alive.
         */
        setVisible(false);
    }

    // ================= REGISTER =================

    private void openRegistration() {

        RegisterFrame registerFrame =
                new RegisterFrame(
                        userService,
                        productService,
                        orderService,
                        notificationService,
                        cartService,
                        inventoryService,
                        orderProcessingService);

        registerFrame.setVisible(true);

        /*
         * Keep the original MainFrame alive.
         */
        setVisible(false);
    }

    // ================= ADMIN LOGIN =================

    private void openAdminLogin() {

        AdminLoginFrame adminLoginFrame =
                new AdminLoginFrame(
                        userService,
                        productService,
                        orderService,
                        notificationService,
                        inventoryService,
                        reportService);

        adminLoginFrame.setVisible(true);

        /*
         * Keep the original MainFrame alive.
         */
        setVisible(false);
    }

    // ================= ABOUT =================

    private void openAbout() {

        AboutFrame aboutFrame =
                new AboutFrame();

        aboutFrame.setVisible(true);
    }

    // ================= EXIT APPLICATION =================

    private void exitApplication() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to exit SAIVEE?",
                        "Exit Application",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        /*
         * This is the ONLY main-page action that
         * completely closes the application.
         */
        dispose();
        System.exit(0);
    }

    // ================= RETURN TO MAIN PAGE =================

    public void returnToMainPage() {

        setVisible(true);

        toFront();

        requestFocus();

        /*
         * Make sure the main window is restored
         * if it was minimized.
         */
        setState(JFrame.NORMAL);
    }
}