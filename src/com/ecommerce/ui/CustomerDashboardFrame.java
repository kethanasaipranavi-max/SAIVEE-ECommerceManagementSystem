package com.ecommerce.ui;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderProcessingService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.ReturnService;
import com.ecommerce.service.AnalyticsService;
import com.ecommerce.service.WishlistService;
import com.ecommerce.service.RecentlyViewedService;
import com.ecommerce.service.RecommendationService;
import com.ecommerce.service.CouponService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class CustomerDashboardFrame extends JFrame {

    private final Customer customer;
    private final UserService userService;
    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final CartService cartService;
    private final InventoryService inventoryService;
    private final OrderProcessingService orderProcessingService;
    private final ReturnService returnService;
    private final AnalyticsService analyticsService;
    private final WishlistService wishlistService;
    private final RecentlyViewedService recentlyViewedService;
    private final RecommendationService recommendationService;
    private final CouponService couponService;

    private JLabel totalOrdersValue;
    private JLabel totalSpentValue;
    private JLabel activeOrdersValue;
    private JLabel productsValue;

    private static final Color NAVY =
            new Color(13, 45, 82);

    private static final Color DARK_BLUE =
            new Color(25, 70, 125);

    private static final Color ROYAL_BLUE =
            new Color(40, 90, 180);

    private static final Color BLUE =
            new Color(45, 120, 210);

    private static final Color SKY_BLUE =
            new Color(80, 160, 230);

    private static final Color PALE_BLUE =
            new Color(240, 248, 255);

    public CustomerDashboardFrame(
            Customer customer,
            UserService userService,
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            CartService cartService,
            InventoryService inventoryService,
            OrderProcessingService orderProcessingService) {

        this.customer = customer;
        this.userService = userService;
        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.cartService = cartService;
        this.inventoryService = inventoryService;
        this.orderProcessingService = orderProcessingService;
        this.returnService = new ReturnService(orderService, inventoryService);
        this.analyticsService = new AnalyticsService(orderService, userService, productService);
        this.wishlistService = WishlistService.getInstance();
        this.recentlyViewedService = RecentlyViewedService.getInstance();
        this.recommendationService = new RecommendationService(productService, orderService);
        this.couponService = new CouponService();

        setTitle(
                "SAIVEE - Customer Dashboard");

        setSize(950, 700);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE);

        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { returnToMain(); }
        });

        setLayout(
                new BorderLayout());

        createHeader();
        createDashboard();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout());

        headerPanel.setBackground(NAVY);

        headerPanel.setBorder(
                new EmptyBorder(
                        20, 30, 20, 30));

        JLabel logoLabel =
                new JLabel("SAIVEE");

        logoLabel.setForeground(Color.WHITE);

        logoLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        32));

        JLabel subtitleLabel =
                new JLabel(
                        "Your Shopping World");

        subtitleLabel.setForeground(Color.WHITE);

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        15));

        JPanel logoPanel =
                new JPanel(
                        new GridLayout(2, 1));

        logoPanel.setOpaque(false);

        logoPanel.add(logoLabel);
        logoPanel.add(subtitleLabel);

        JLabel welcomeLabel =
                new JLabel(
                        "Welcome, "
                                + customer.getName()
                                + "!");

        welcomeLabel.setForeground(Color.WHITE);

        welcomeLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18));

        headerPanel.add(
                logoPanel,
                BorderLayout.WEST);

        headerPanel.add(
                welcomeLabel,
                BorderLayout.EAST);

        add(
                headerPanel,
                BorderLayout.NORTH);
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void createDashboard() {

        JPanel mainPanel =
                new JPanel(
                        new BorderLayout(
                                15,
                                15));

        mainPanel.setBackground(
                PALE_BLUE);

        mainPanel.setBorder(
                new EmptyBorder(
                        20, 25, 20, 25));

        // ================= SUMMARY =================

        JPanel summaryPanel =
                new JPanel(
                        new GridLayout(
                                1,
                                4,
                                15,
                                15));

        summaryPanel.setOpaque(false);

        totalOrdersValue =
                createValueLabel(
                        getTotalOrders());

        totalSpentValue =
                createMoneyValueLabel(
                        getTotalSpent());

        activeOrdersValue =
                createValueLabel(
                        getActiveOrders());

        productsValue =
                createValueLabel(
                        productService.getProductCount());

        summaryPanel.add(
                createSummaryCard(
                        "TOTAL ORDERS",
                        totalOrdersValue,
                        DARK_BLUE));

        summaryPanel.add(
                createSummaryCard(
                        "TOTAL SPENT",
                        totalSpentValue,
                        ROYAL_BLUE));

        summaryPanel.add(
                createSummaryCard(
                        "ACTIVE ORDERS",
                        activeOrdersValue,
                        BLUE));

        summaryPanel.add(
                createSummaryCard(
                        "PRODUCTS",
                        productsValue,
                        SKY_BLUE));

        mainPanel.add(
                summaryPanel,
                BorderLayout.NORTH);

        // ================= BUTTONS =================

        JPanel buttonPanel =
                new JPanel(
                        new GridLayout(
                                4,
                                3,
                                15,
                                15));

        buttonPanel.setOpaque(false);

        JButton shoppingButton =
                createButton(
                        "SHOPPING CATALOGUE",
                        DARK_BLUE);

        JButton cartButton =
                createButton(
                        "MY CART",
                        ROYAL_BLUE);

        JButton ordersButton =
                createButton(
                        "MY ORDERS",
                        BLUE);

        JButton notificationButton =
                createButton(
                        "NOTIFICATIONS",
                        SKY_BLUE);

        JButton profileButton =
                createButton(
                        "MY PROFILE",
                        DARK_BLUE);

        JButton returnsButton =
                createButton(
                        "RETURNS / CANCEL",
                        new Color(170, 95, 55));

        JButton logoutButton =
                createButton(
                        "LOGOUT",
                        new Color(
                                190,
                                55,
                                65));

        JButton recentButton = createButton("RECENTLY VIEWED", DARK_BLUE);
        JButton recommendationButton = createButton("RECOMMENDATIONS", ROYAL_BLUE);
        JButton wishlistButton = createButton("MY WISHLIST", BLUE);
        JButton couponButton = createButton("COUPONS", SKY_BLUE);
        JButton analyticsButton = createButton("MY ANALYTICS", DARK_BLUE);

        shoppingButton.addActionListener(
                e -> openProductCatalogue());

        cartButton.addActionListener(
                e -> openCart());

        ordersButton.addActionListener(
                e -> {
                    refreshDashboard();
                    showOrderHistory();
                });

        notificationButton.addActionListener(
                e -> openNotifications());

        profileButton.addActionListener(
                e -> showProfile());

        returnsButton.addActionListener(
                e -> manageReturnsAndCancellation());

        logoutButton.addActionListener(
                e -> logout());
        recentButton.addActionListener(e -> showRecentlyViewed());
        recommendationButton.addActionListener(e -> showRecommendations());
        wishlistButton.addActionListener(e -> showWishlist());
        couponButton.addActionListener(e -> showCoupons());
        analyticsButton.addActionListener(e -> showCustomerAnalytics());

        buttonPanel.add(shoppingButton);
        buttonPanel.add(cartButton);
        buttonPanel.add(ordersButton);
        buttonPanel.add(notificationButton);
        buttonPanel.add(profileButton);
        buttonPanel.add(returnsButton);
        buttonPanel.add(logoutButton);
        buttonPanel.add(recentButton);
        buttonPanel.add(recommendationButton);
        buttonPanel.add(wishlistButton);
        buttonPanel.add(couponButton);
        buttonPanel.add(analyticsButton);

        mainPanel.add(
                buttonPanel,
                BorderLayout.CENTER);

        // ================= FOOTER =================

        JLabel bottomLabel =
                new JLabel(
                        "SAIVEE • Shop Smart • Shop Simple • Shop Better",
                        SwingConstants.CENTER);

        bottomLabel.setForeground(DARK_BLUE);

        bottomLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14));

        mainPanel.add(
                bottomLabel,
                BorderLayout.SOUTH);

        add(
                mainPanel,
                BorderLayout.CENTER);
    }

    // =========================================================
    // SUMMARY CARD
    // =========================================================

    private JPanel createSummaryCard(
            String title,
            JLabel valueLabel,
            Color color) {

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                5,
                                5));

        card.setBackground(color);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                2),
                        new EmptyBorder(
                                15,
                                10,
                                15,
                                10)));

        JLabel titleLabel =
                new JLabel(
                        title,
                        SwingConstants.CENTER);

        titleLabel.setForeground(Color.WHITE);

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12));

        card.add(
                titleLabel,
                BorderLayout.NORTH);

        card.add(
                valueLabel,
                BorderLayout.CENTER);

        return card;
    }

    private JLabel createValueLabel(
            int value) {

        JLabel label =
                new JLabel(
                        String.valueOf(value),
                        SwingConstants.CENTER);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23));

        return label;
    }

    private JLabel createMoneyValueLabel(
            double value) {

        JLabel label =
                new JLabel(
                        String.format(
                                "₹%.2f",
                                value),
                        SwingConstants.CENTER);

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        23));

        return label;
    }

    // =========================================================
    // BUTTON
    // =========================================================

    private JButton createButton(
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
                        16));

        button.setFocusPainted(false);

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                2),
                        new EmptyBorder(
                                15,
                                10,
                                15,
                                10)));

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR));

        return button;
    }

    // =========================================================
    // REFRESH DASHBOARD
    // =========================================================

    public void refreshDashboard() {

        /*
         * Always read the CURRENT shared OrderService.
         *
         * This means:
         *
         * Customer logs out
         *       ↓
         * Customer logs in again
         *       ↓
         * Same OrderService
         *       ↓
         * Previous orders are still available
         */

        if (totalOrdersValue != null) {

            totalOrdersValue.setText(
                    String.valueOf(
                            getTotalOrders()));
        }

        if (totalSpentValue != null) {

            totalSpentValue.setText(
                    String.format(
                            "₹%.2f",
                            getTotalSpent()));
        }

        if (activeOrdersValue != null) {

            activeOrdersValue.setText(
                    String.valueOf(
                            getActiveOrders()));
        }

        if (productsValue != null) {

            productsValue.setText(
                    String.valueOf(
                            productService.getProductCount()));
        }

        revalidate();
        repaint();
    }

    // =========================================================
    // ORDER CALCULATIONS
    // =========================================================

    private List<Order> getCustomerOrders() {

        /*
         * Centralized order lookup.
         *
         * All customer order screens use this method.
         */
        List<Order> orders =
                orderService.getCustomerOrders(
                        customer);

        return orders == null
                ? java.util.Collections.emptyList()
                : orders;
    }

    private int getTotalOrders() {

        return getCustomerOrders().size();
    }

    private double getTotalSpent() {

        List<Order> orders =
                getCustomerOrders();

        double total = 0;

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            if (order.getStatus()
                    != OrderStatus.CANCELLED
                    && order.getStatus()
                    != OrderStatus.FAILED) {

                total +=
                        order.getTotalAmount();
            }
        }

        return total;
    }

    private int getActiveOrders() {

        List<Order> orders =
                getCustomerOrders();

        int count = 0;

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            OrderStatus status =
                    order.getStatus();

            if (status != OrderStatus.DELIVERED
                    && status != OrderStatus.CANCELLED
                    && status != OrderStatus.FAILED) {

                count++;
            }
        }

        return count;
    }

    // =========================================================
    // SHOPPING CATALOGUE
    // =========================================================

    private void openProductCatalogue() {

        ProductFrame productFrame =
                new ProductFrame(
                        productService.getProducts(),
                        productService,
                        orderService,
                        notificationService,
                        customer,
                        cartService,
                        inventoryService,
                        orderProcessingService);

        productFrame.setVisible(true);
    }

    // =========================================================
    // CART
    // =========================================================

    private void openCart() {

        CartFrame cartFrame =
                new CartFrame(
                        productService,
                        orderService,
                        notificationService,
                        customer,
                        cartService,
                        inventoryService,
                        orderProcessingService);

        cartFrame.setVisible(true);
    }

    // =========================================================
    // MY ORDERS
    // =========================================================

    private void showOrderHistory() {

        /*
         * Get the latest orders every time the customer
         * clicks MY ORDERS.
         *
         * Therefore admin status changes are immediately
         * reflected here.
         */
        List<Order> orders =
                getCustomerOrders();

        if (orders.isEmpty()) {

            JTextArea emptyArea =
                    new JTextArea(
                            "No orders found.\n\n"
                                    + "Your placed orders "
                                    + "will appear here.");

            emptyArea.setEditable(false);

            emptyArea.setFont(
                    new Font(
                            "Arial",
                            Font.PLAIN,
                            16));

            emptyArea.setBorder(
                    new EmptyBorder(
                            20,
                            20,
                            20,
                            20));

            JOptionPane.showMessageDialog(
                    this,
                    emptyArea,
                    "SAIVEE - My Orders",
                    JOptionPane.INFORMATION_MESSAGE);

            return;
        }

        StringBuilder output =
                new StringBuilder();

        output.append(
                "========================================\n");

        output.append(
                "             SAIVEE MY ORDERS\n");

        output.append(
                "========================================\n\n");

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            output.append(
                    "Order ID: ")
                    .append(
                            order.getOrderId())
                    .append("\n");

            output.append(
                    "Status: ")
                    .append(
                            order.getStatus())
                    .append("\n");

            output.append(
                    "Total Amount: ₹")
                    .append(
                            String.format(
                                    "%.2f",
                                    order.getTotalAmount()))
                    .append("\n");

            output.append(
                    "Items:\n");

            if (order.getOrderItems() != null) {

                for (var item :
                        order.getOrderItems()) {

                    if (item == null
                            || item.getProduct() == null) {

                        continue;
                    }

                    output.append(
                            "  • ")
                            .append(
                                    item.getProduct()
                                            .getProductName())
                            .append(
                                    " x ")
                            .append(
                                    item.getQuantity())
                            .append(
                                    " = ₹")
                            .append(
                                    String.format(
                                            "%.2f",
                                            item.calculateSubtotal()))
                            .append("\n");
                }
            }

            output.append(
                    "\n----------------------------------------\n\n");
        }

        JTextArea textArea =
                new JTextArea(
                        output.toString());

        textArea.setEditable(false);

        textArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14));

        textArea.setBackground(
                PALE_BLUE);

        textArea.setForeground(
                NAVY);

        textArea.setCaretPosition(0);

        JScrollPane scrollPane =
                new JScrollPane(textArea);

        scrollPane.setPreferredSize(
                new Dimension(
                        750,
                        500));

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "SAIVEE - My Orders",
                JOptionPane.INFORMATION_MESSAGE);

        refreshDashboard();
    }

    private void showRecentlyViewed() {
        showProductSummary("RECENTLY VIEWED", recentlyViewedService.getRecentlyViewed(customer));
    }

    private void showRecommendations() {
        showProductSummary("RECOMMENDED FOR YOU", recommendationService.recommend(customer, null, 10));
    }

    private void showProductSummary(String title, List<Product> products) {
        if (products == null || products.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No products to display yet.", "SAIVEE", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        StringBuilder b = new StringBuilder(title).append("\n\n");
        for (Product p : products) b.append("• ").append(p.getProductName()).append(" | ")
                .append(p.getBrand()).append(" | ₹").append(String.format("%.2f", p.getPrice()))
                .append(" | Stock: ").append(p.getQuantity()).append("\n");
        JTextArea area = new JTextArea(b.toString()); area.setEditable(false);
        JOptionPane.showMessageDialog(this, new JScrollPane(area), "SAIVEE - " + title, JOptionPane.INFORMATION_MESSAGE);
    }

    private void showWishlist() {
        List<Product> list = wishlistService.getWishlist(customer);
        if (list.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Your wishlist is empty.", "SAIVEE Wishlist", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] choices = list.stream().map(p -> p.getProductName() + " | ₹" + String.format("%.2f", p.getPrice()) + " | Stock: " + p.getQuantity()).toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(this, "Select a wishlist product to move to cart:",
                "SAIVEE Wishlist", JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (selected == null) return;
        int index = java.util.Arrays.asList(choices).indexOf(selected);
        Product product = list.get(index);
        if (product.getQuantity() <= 0) { JOptionPane.showMessageDialog(this, "Product is currently out of stock.", "Wishlist", JOptionPane.WARNING_MESSAGE); return; }
        String qty = JOptionPane.showInputDialog(this, "Quantity:", "Move to Cart", JOptionPane.QUESTION_MESSAGE);
        if (qty == null) return;
        try {
            int q = Integer.parseInt(qty.trim());
            if (q <= 0 || !cartService.addToCart(customer, product, q)) throw new NumberFormatException();
            wishlistService.removeFromWishlist(customer, product);
            JOptionPane.showMessageDialog(this, "Product moved to cart successfully.", "Wishlist", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) { JOptionPane.showMessageDialog(this, "Invalid quantity or insufficient stock.", "Wishlist", JOptionPane.ERROR_MESSAGE); }
    }

    private void showCoupons() {
        JOptionPane.showMessageDialog(this,
                "Available Coupons\n\nSAVE10  - 10% off (min ₹500)\nSAVE20  - 20% off (min ₹1500)\nFLAT500 - ₹500 off (min ₹2500)\nWELCOME100 - ₹100 off (min ₹999)",
                "SAIVEE - Coupons", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showCustomerAnalytics() {
        String text = String.format(
                "CUSTOMER ANALYTICS\n\nCustomer: %s\nEmail: %s\n\nTotal Spending: ₹%.2f\nNumber of Orders: %d\nFavourite Category: %s\nMost Purchased Brand: %s\nAverage Order Value: ₹%.2f",
                customer.getName(), customer.getEmail(), analyticsService.customerSpending(customer),
                analyticsService.customerOrderCount(customer), analyticsService.favouriteCategory(customer),
                analyticsService.mostPurchasedBrand(customer), analyticsService.averageOrderValue(customer));
        JOptionPane.showMessageDialog(this, text, "SAIVEE - My Analytics", JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================
    // NOTIFICATIONS
    // =========================================================

    private void openNotifications() {

        NotificationFrame notificationFrame =
                new NotificationFrame(
                        customer,
                        notificationService);

        notificationFrame.setVisible(true);
    }

    // =========================================================
    // PROFILE
    // =========================================================

    private void manageReturnsAndCancellation() {
        List<Order> orders = getCustomerOrders();
        if (orders.isEmpty()) {
            JOptionPane.showMessageDialog(this, "You have no orders.",
                    "SAIVEE - Returns / Cancellation", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        String[] choices = orders.stream()
                .map(o -> "#" + o.getOrderId() + " - " + o.getStatus()
                        + " - ₹" + String.format("%.2f", o.getTotalAmount()))
                .toArray(String[]::new);
        String selected = (String) JOptionPane.showInputDialog(
                this, "Select an order. Return is available only for DELIVERED orders; cancellation is available before shipping:", "SAIVEE - Returns / Cancellation",
                JOptionPane.QUESTION_MESSAGE, null, choices, choices[0]);
        if (selected == null) return;

        int orderId = Integer.parseInt(selected.substring(1, selected.indexOf(" ")));
        Order order = orderService.findOrderById(orderId);
        if (order == null) return;

        boolean canReturn = order.getStatus() == OrderStatus.DELIVERED;
        boolean canCancel = order.getStatus() == OrderStatus.PLACED
                || order.getStatus() == OrderStatus.CONFIRMED
                || order.getStatus() == OrderStatus.PROCESSING;

        if (canReturn && canCancel) { // defensive; normally statuses are mutually exclusive
            JOptionPane.showMessageDialog(this, "Please refresh this order and try again.",
                    "SAIVEE - Order Actions", JOptionPane.INFORMATION_MESSAGE);
            return;
        }

        if (canReturn) {
            String[] actions = {"Request Return", "Close"};
            int action = JOptionPane.showOptionDialog(this,
                    "Order #" + orderId + " has been delivered. You can request a return.",
                    "SAIVEE - Return Options", JOptionPane.DEFAULT_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, actions, actions[0]);
            if (action == 0) {
                String reason = JOptionPane.showInputDialog(this,
                        "Please enter the reason for your return request:");
                if (reason == null) return;
                boolean ok = returnService.requestReturn(orderId, reason);
                if (ok) {
                    String date = JOptionPane.showInputDialog(this,
                            "Optional: enter preferred pickup date/time (yyyy-MM-dd HH:mm), or Cancel to schedule later:",
                            "SAIVEE - Pickup Scheduling", JOptionPane.QUESTION_MESSAGE);
                    if (date != null && !date.isBlank()) {
                        try {
                            returnService.schedulePickup(orderId, java.time.LocalDateTime.parse(date.trim(), java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));
                        } catch (Exception ex) {
                            JOptionPane.showMessageDialog(this, "Pickup request was saved, but the date format was invalid. Admin can schedule pickup later.", "SAIVEE", JOptionPane.WARNING_MESSAGE);
                        }
                    }
                }
                JOptionPane.showMessageDialog(this,
                        ok ? "Return request submitted. You can track pickup, inspection, approval/rejection and refund status from Returns / Cancel."
                                : "The return request could not be submitted. It may already be under review.",
                        "SAIVEE - Return Request",
                        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                refreshDashboard();
            }
            return;
        }

        if (canCancel) {
            int choice = JOptionPane.showConfirmDialog(this,
                    "Cancel order #" + orderId + "? Reserved stock will be restored if cancellation succeeds.",
                    "SAIVEE - Cancel Order", JOptionPane.YES_NO_OPTION);
            if (choice == JOptionPane.YES_OPTION) {
                boolean ok = orderService.cancelOrder(orderId);
                JOptionPane.showMessageDialog(this,
                        ok ? "Order cancelled and stock restored. If you paid online, the refund will be initiated to your registered refund destination. Cash on Delivery orders are not charged before delivery. Details will be sent by email."
                                : "Order could not be cancelled.",
                        "SAIVEE - Cancellation",
                        ok ? JOptionPane.INFORMATION_MESSAGE : JOptionPane.WARNING_MESSAGE);
                refreshDashboard();
            }
        } else {
            com.ecommerce.model.ReturnRecord rr = returnService.getRecord(orderId);
            String detail;
            if (rr != null) {
                detail = "RETURN STATUS\n\nOrder: #" + orderId + "\nReason: " + rr.getReason() + "\nStatus: " + rr.getStatus()
                        + "\nPickup: " + (rr.getPickupDate() == null ? "Not scheduled yet" : rr.getPickupDate())
                        + "\nInspection: " + (rr.getInspectionNotes() == null || rr.getInspectionNotes().isBlank() ? "Pending / not recorded" : rr.getInspectionNotes())
                        + "\nRefund reference: " + (rr.getRefundReference() == null || rr.getRefundReference().isBlank() ? "Not issued yet" : rr.getRefundReference());
            } else if (order.getStatus() == OrderStatus.CANCELLED) {
                detail = "This order is already cancelled.";
            } else {
                detail = "RETURN: available after delivery.\nCANCELLATION: available before shipping.\nCurrent order status: " + order.getStatus();
            }
            JOptionPane.showMessageDialog(this, detail, "SAIVEE - Order Actions", JOptionPane.INFORMATION_MESSAGE);
        }
    }

    // =========================================================
    // PROFILE
    // =========================================================

    private void showProfile() {

        String profile =
                "SAIVEE CUSTOMER PROFILE\n\n"
                        + "Customer ID: "
                        + customer.getUserId()
                        + "\n\n"
                        + "Name: "
                        + customer.getName()
                        + "\n"
                        + "Email: "
                        + customer.getEmail()
                        + "\n"
                        + "Phone: "
                        + customer.getPhone();

        JTextArea textArea =
                new JTextArea(profile);

        textArea.setEditable(false);

        textArea.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        16));

        textArea.setBackground(
                PALE_BLUE);

        textArea.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15));

        JOptionPane.showMessageDialog(
                this,
                textArea,
                "SAIVEE - My Profile",
                JOptionPane.INFORMATION_MESSAGE);
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void returnToMain() {
        MainFrame main = MainFrame.getInstance();
        if (main != null) main.returnToMainPage();
        dispose();
    }

    private void logout() {

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to logout?",
                        "SAIVEE Logout",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        /*
         * IMPORTANT:
         *
         * DO NOT create a new MainFrame.
         *
         * The original MainFrame is still alive but hidden.
         * It contains the ORIGINAL shared services:
         *
         * UserService
         * ProductService
         * OrderService
         * NotificationService
         * CartService
         * InventoryService
         * OrderProcessingService
         * ReportService
         *
         * Therefore the OrderService containing previous
         * orders is NOT destroyed during logout.
         */

        MainFrame mainFrame =
                findMainFrame();

        if (mainFrame != null) {

            /*
             * Close only the customer dashboard.
             */
            dispose();

            /*
             * Restore the ORIGINAL main page.
             */
            mainFrame.returnToMainPage();

            return;
        }

        /*
         * This should normally never happen because
         * MainFrame is created first by Main.java.
         */
        JOptionPane.showMessageDialog(
                this,
                "SAIVEE main page could not be restored.",
                "Logout",
                JOptionPane.WARNING_MESSAGE);

        dispose();
    }

    // =========================================================
    // FIND ORIGINAL MAIN FRAME
    // =========================================================

    private MainFrame findMainFrame() {

        /*
         * Search all currently existing Swing windows.
         *
         * We deliberately do NOT create another MainFrame.
         */
        for (Window window :
                Window.getWindows()) {

            if (window instanceof MainFrame) {

                return (MainFrame) window;
            }
        }

        return null;
    }
}
