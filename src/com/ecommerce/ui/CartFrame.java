package com.ecommerce.ui;

import com.ecommerce.exception.InsufficientStockException;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Customer;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.Product;
import com.ecommerce.service.CartService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderProcessingService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.CouponService;
import com.ecommerce.service.InvoiceService;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;

public class CartFrame extends JFrame {

    private final ProductService productService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final Customer customer;
    private final CartService cartService;

    private final InventoryService inventoryService;
    private final OrderProcessingService orderProcessingService;
    private final PaymentService paymentService;
    private final CouponService couponService;
    private final InvoiceService invoiceService;

    private JPanel cartPanel;

    private JLabel subtotalLabel;
    private JLabel discountLabel;
    private JLabel deliveryLabel;
    private JLabel totalLabel;
    private JLabel itemCountLabel;

    private static final double DELIVERY_CHARGE_PER_ITEM = 20.0;

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

    private static final Color LIGHT_BLUE =
            new Color(220, 240, 255);

    private static final Color PALE_BLUE =
            new Color(240, 248, 255);

    public CartFrame(
            ProductService productService,
            OrderService orderService,
            NotificationService notificationService,
            Customer customer,
            CartService cartService,
            InventoryService inventoryService,
            OrderProcessingService orderProcessingService) {

        this.productService = productService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.customer = customer;
        this.cartService = cartService;

        this.inventoryService = inventoryService;
        this.orderProcessingService =
                orderProcessingService;

        this.paymentService =
                new PaymentService();
        this.couponService =
                new CouponService();
        this.invoiceService = new InvoiceService();

        setTitle("SAIVEE - My Cart");

        setSize(900, 700);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(0, 0)
        );

        createHeader();
        createCartArea();
        createBottomArea();

        loadCart();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel header =
                new JPanel(
                        new BorderLayout()
                );

        header.setBackground(NAVY);

        header.setBorder(
                new EmptyBorder(
                        15,
                        25,
                        15,
                        25
                )
        );

        JLabel title =
                new JLabel("SAIVEE");

        title.setForeground(Color.WHITE);

        title.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        30
                )
        );

        JLabel subtitle =
                new JLabel(
                        "MY SHOPPING CART"
                );

        subtitle.setForeground(
                LIGHT_BLUE
        );

        subtitle.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        JPanel left =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        left.setOpaque(false);

        left.add(title);
        left.add(subtitle);

        JLabel customerLabel =
                new JLabel(
                        "Customer: "
                                + customer.getName()
                );

        customerLabel.setForeground(
                Color.WHITE
        );

        customerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        15
                )
        );

        header.add(
                left,
                BorderLayout.WEST
        );

        header.add(
                customerLabel,
                BorderLayout.EAST
        );

        add(
                header,
                BorderLayout.NORTH
        );
    }

    // =========================================================
    // CART AREA
    // =========================================================

    private void createCartArea() {

        cartPanel =
                new JPanel();

        cartPanel.setLayout(
                new BoxLayout(
                        cartPanel,
                        BoxLayout.Y_AXIS
                )
        );

        cartPanel.setBackground(
                PALE_BLUE
        );

        cartPanel.setBorder(
                new EmptyBorder(
                        15,
                        20,
                        15,
                        20
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        cartPanel
                );

        scrollPane.setBorder(null);

        scrollPane.getVerticalScrollBar()
                .setUnitIncrement(16);

        add(
                scrollPane,
                BorderLayout.CENTER
        );
    }

    // =========================================================
    // BOTTOM AREA
    // =========================================================

    private void createBottomArea() {

        JPanel bottom =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        bottom.setBackground(
                LIGHT_BLUE
        );

        bottom.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        15,
                        20
                )
        );

        JPanel summary =
                new JPanel(
                        new GridLayout(
                                5,
                                1,
                                5,
                                3
                        )
                );

        summary.setOpaque(false);

        itemCountLabel =
                createSummaryLabel(
                        "Items: 0"
                );

        subtotalLabel =
                createSummaryLabel(
                        "Subtotal: ₹0.00"
                );

        discountLabel =
                createSummaryLabel(
                        "Discount: ₹0.00"
                );

        deliveryLabel =
                createSummaryLabel(
                        "Delivery: ₹0.00"
                );

        totalLabel =
                createSummaryLabel(
                        "Final Total: ₹0.00"
                );

        totalLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        19
                )
        );

        summary.add(itemCountLabel);
        summary.add(subtotalLabel);
        summary.add(discountLabel);
        summary.add(deliveryLabel);
        summary.add(totalLabel);

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.RIGHT,
                                8,
                                5
                        )
                );

        buttons.setOpaque(false);

        JButton refreshButton =
                createButton(
                        "REFRESH",
                        SKY_BLUE
                );

        JButton clearButton =
                createButton(
                        "CLEAR CART",
                        DARK_BLUE
                );

        JButton checkoutButton =
                createButton(
                        "CHECKOUT",
                        ROYAL_BLUE
                );

        JButton closeButton =
                createButton(
                        "CLOSE",
                        NAVY
                );

        refreshButton.addActionListener(
                e -> loadCart()
        );

        clearButton.addActionListener(
                e -> clearCart()
        );

        checkoutButton.addActionListener(
                e -> checkout()
        );

        closeButton.addActionListener(
                e -> dispose()
        );

        buttons.add(refreshButton);
        buttons.add(clearButton);
        buttons.add(checkoutButton);
        buttons.add(closeButton);

        bottom.add(
                summary,
                BorderLayout.CENTER
        );

        bottom.add(
                buttons,
                BorderLayout.SOUTH
        );

        add(
                bottom,
                BorderLayout.SOUTH
        );
    }

    // =========================================================
    // LOAD CART
    // =========================================================

    private void loadCart() {

        cartPanel.removeAll();

        Cart cart =
                cartService.getCart(
                        customer
                );

        if (cart == null
                || cart.isEmpty()) {

            showEmptyCart();

            updateSummary(
                    0,
                    0,
                    0,
                    0
            );

            cartPanel.revalidate();
            cartPanel.repaint();

            return;
        }

        int totalItems =
                cart.getTotalItems();

        double subtotal = 0;

        for (OrderItem item :
                cart.getItems()) {

            subtotal +=
                    item.calculateSubtotal();

            cartPanel.add(
                    createCartItem(
                            item
                    )
            );

            cartPanel.add(
                    Box.createVerticalStrut(
                            10
                    )
            );
        }

        double discount =
                calculateDiscount(
                        subtotal
                );

        double deliveryCharge =
                calculateDeliveryCharge(
                        totalItems
                );

        updateSummary(
                totalItems,
                subtotal,
                discount,
                deliveryCharge
        );

        cartPanel.revalidate();
        cartPanel.repaint();
    }

    // =========================================================
    // CART ITEM CARD
    // =========================================================

    private JPanel createCartItem(
            OrderItem item) {

        Product product =
                item.getProduct();

        JPanel card =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        card.setBackground(Color.WHITE);

        card.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                SKY_BLUE,
                                2
                        ),
                        new EmptyBorder(
                                12,
                                15,
                                12,
                                15
                        )
                )
        );

        JPanel info =
                new JPanel(
                        new GridLayout(
                                4,
                                1
                        )
                );

        info.setOpaque(false);

        JLabel name =
                new JLabel(
                        product.getProductName()
                );

        name.setForeground(NAVY);

        name.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        17
                )
        );

        JLabel brand =
                new JLabel(
                        "Brand: "
                                + product.getBrand()
                );

        JLabel price =
                new JLabel(
                        String.format(
                                "Price: ₹%.2f",
                                product.getPrice()
                        )
                );

        JLabel subtotal =
                new JLabel(
                        String.format(
                                "Item Total: ₹%.2f",
                                item.calculateSubtotal()
                        )
                );

        subtotal.setForeground(
                ROYAL_BLUE
        );

        subtotal.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        info.add(name);
        info.add(brand);
        info.add(price);
        info.add(subtotal);

        JPanel quantityPanel =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                5,
                                5
                        )
                );

        quantityPanel.setOpaque(false);

        JButton minusButton =
                createSmallButton(
                        "−",
                        DARK_BLUE
                );

        JLabel quantityLabel =
                new JLabel(
                        String.valueOf(
                                item.getQuantity()
                        ),
                        SwingConstants.CENTER
                );

        quantityLabel.setPreferredSize(
                new Dimension(
                        45,
                        30
                )
        );

        quantityLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        16
                )
        );

        quantityLabel.setForeground(
                NAVY
        );

        JButton plusButton =
                createSmallButton(
                        "+",
                        BLUE
                );

        JButton removeButton =
                createSmallButton(
                        "REMOVE",
                        ROYAL_BLUE
                );

        minusButton.addActionListener(
                e -> {

                    int quantity =
                            item.getQuantity();

                    if (quantity > 1) {

                        cartService.updateQuantity(
                                customer,
                                product,
                                quantity - 1
                        );

                    } else {

                        cartService.removeFromCart(
                                customer,
                                product
                        );
                    }

                    loadCart();
                }
        );

        plusButton.addActionListener(
                e -> {

                    int quantity =
                            item.getQuantity();

                    if (quantity
                            >= product.getQuantity()) {

                        JOptionPane.showMessageDialog(
                                this,
                                "You cannot add more than the available stock.",
                                "SAIVEE",
                                JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }

                    cartService.updateQuantity(
                            customer,
                            product,
                            quantity + 1
                    );

                    loadCart();
                }
        );

        removeButton.addActionListener(
                e -> {

                    cartService.removeFromCart(
                            customer,
                            product
                    );

                    loadCart();
                }
        );

        quantityPanel.add(
                new JLabel("Qty:")
        );

        quantityPanel.add(
                minusButton
        );

        quantityPanel.add(
                quantityLabel
        );

        quantityPanel.add(
                plusButton
        );

        quantityPanel.add(
                removeButton
        );

        card.add(
                info,
                BorderLayout.CENTER
        );

        card.add(
                quantityPanel,
                BorderLayout.EAST
        );

        return card;
    }

    // =========================================================
    // DISCOUNT
    // =========================================================

    private double calculateDiscount(
            double subtotal) {

        if (subtotal < 2000) {
            return 0;
        }

        int blocks =
                (int) (
                        subtotal / 1000
                );

        return blocks * 100;
    }

    // =========================================================
    // DELIVERY
    // =========================================================

    private double calculateDeliveryCharge(
            int totalItems) {

        if (totalItems <= 0) {
            return 0;
        }

        return totalItems
                * DELIVERY_CHARGE_PER_ITEM;
    }

    // =========================================================
    // SUMMARY
    // =========================================================

    private void updateSummary(
            int itemCount,
            double subtotal,
            double discount,
            double delivery) {

        double taxableAmount = Math.max(0.0, subtotal - discount);
        double gst = taxableAmount * 0.18;
        double finalTotal = taxableAmount + gst + delivery;

        itemCountLabel.setText(
                "Items: "
                        + itemCount
        );

        subtotalLabel.setText(
                String.format(
                        "Subtotal: ₹%.2f",
                        subtotal
                )
        );

        discountLabel.setText(
                String.format(
                        "Discount: -₹%.2f",
                        discount
                )
        );

        deliveryLabel.setText(
                String.format(
                        "Delivery: ₹%.2f (₹20 per item)",
                        delivery
                )
        );

        totalLabel.setText(
                String.format(
                        "Final Total: ₹%.2f",
                        finalTotal
                )
        );
    }

    // =========================================================
    // CLEAR CART
    // =========================================================

    private void clearCart() {

        Cart cart =
                cartService.getCart(
                        customer
                );

        if (cart == null
                || cart.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your cart is already empty.",
                    "SAIVEE",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return;
        }

        int choice =
                JOptionPane.showConfirmDialog(
                        this,
                        "Are you sure you want to clear your cart?",
                        "SAIVEE - Clear Cart",
                        JOptionPane.YES_NO_OPTION
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        cartService.clearCart(
                customer
        );

        loadCart();

        JOptionPane.showMessageDialog(
                this,
                "Cart cleared successfully.",
                "SAIVEE",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // CHECKOUT
    // =========================================================

    private void checkout() {

        Cart cart =
                cartService.getCart(
                        customer
                );

        if (cart == null
                || cart.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "Your cart is empty.",
                    "SAIVEE Checkout",
                    JOptionPane.WARNING_MESSAGE
            );

            return;
        }

        double subtotal = 0.0;

        for (OrderItem item :
                cart.getItems()) {

            subtotal +=
                    item.calculateSubtotal();
        }

        double discount =
                calculateDiscount(
                        subtotal
                );

        String couponCode = JOptionPane.showInputDialog(
                this,
                "Enter coupon code (optional):\n"
                        + "SAVE10 | SAVE20 | FLAT500 | WELCOME100",
                "SAIVEE - Coupon",
                JOptionPane.QUESTION_MESSAGE);

        if (couponCode != null && !couponCode.trim().isEmpty()) {
            String validation = couponService.validate(couponCode, subtotal);
            if (!"VALID".equals(validation)) {
                JOptionPane.showMessageDialog(
                        this, validation, "Invalid Coupon",
                        JOptionPane.WARNING_MESSAGE);
            } else {
                double couponDiscount =
                        couponService.calculateDiscount(couponCode, subtotal);
                discount = Math.max(discount, couponDiscount);
                JOptionPane.showMessageDialog(
                        this,
                        String.format("Coupon %s applied. Discount: ₹%.2f",
                                couponCode.trim().toUpperCase(), couponDiscount),
                        "SAIVEE - Coupon Applied",
                        JOptionPane.INFORMATION_MESSAGE);
            }
        }

        int totalItems =
                cart.getTotalItems();

        double delivery =
                calculateDeliveryCharge(
                        totalItems
                );

        double taxableAmount = Math.max(0.0, subtotal - discount);
        double gst = taxableAmount * 0.18;
        double finalTotal = taxableAmount + gst + delivery;

        // =====================================================
        // RESERVE STOCK
        // =====================================================

        try {

            orderProcessingService
                    .reserveCartStock(
                            cart.getCartId()
                    );

        } catch (InsufficientStockException e) {

            JOptionPane.showMessageDialog(
                    this,
                    "Checkout could not continue.\n\n"
                            + e.getMessage(),
                    "SAIVEE - Insufficient Stock",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        if (!orderProcessingService
                .hasActiveReservation(
                        cart.getCartId()
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "Unable to reserve stock for checkout.",
                    "SAIVEE - Checkout Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        // =====================================================
        // DISABLE CART
        // =====================================================

        setEnabled(false);

        final double checkoutDiscount = discount;

        // =====================================================
        // OPEN PAYMENT
        // =====================================================

        PaymentFrame paymentFrame =
                new PaymentFrame(
                        finalTotal,
                        customer,
                        paymentService,
                        notificationService,

                        // PAYMENT SUCCESS
                        () -> {

                            try {

                                placeOrderAfterReservation(
                                        finalTotal,
                                        delivery,
                                        checkoutDiscount
                                );

                            } finally {

                                setEnabled(true);
                            }
                        },

                        // PAYMENT FAILED / CANCELLED
                        () -> {

                            orderProcessingService
                                    .releaseCartReservation(
                                            cart.getCartId()
                                    );

                            setEnabled(true);
                        }
                );

        paymentFrame.setVisible(true);
    }

    // =========================================================
    // PLACE ORDER AFTER PAYMENT
    // =========================================================

    private void placeOrderAfterReservation(
            double finalTotal,
            double deliveryCharge,
            double discount) {

        Cart cart =
                cartService.getCart(
                        customer
                );

        if (cart == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Cart could not be found.",
                    "SAIVEE - Order Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int cartId =
                cart.getCartId();

        if (!orderProcessingService
                .hasActiveReservation(
                        cartId
                )) {

            JOptionPane.showMessageDialog(
                    this,
                    "The stock reservation is no longer active.\n"
                            + "Order was not created.",
                    "SAIVEE - Order Error",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        int orderId =
                orderService.getNextOrderId();

        try {

            Order order =
                    orderProcessingService
                            .placeReservedOrder(
                                    orderId,
                                    cartId,
                                    deliveryCharge,
                                    discount
                            );

            if (order == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Unable to create the order.",
                        "SAIVEE - Order Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            com.ecommerce.service.PaymentHistoryService.getInstance().linkLatestToOrder(
                    customer.getEmail(), finalTotal, order.getOrderId());

            JOptionPane.showMessageDialog(
                    this,
                    String.format(
                            "ORDER PLACED SUCCESSFULLY!%n%n"
                                    + "Order ID: #%d%n"
                                    + "Subtotal: ₹%.2f%n"
                                    + "Discount: -₹%.2f%n"
                                    + "GST (18%%): ₹%.2f%n"
                                    + "Delivery: ₹%.2f%n"
                                    + "Amount Paid: ₹%.2f%n"
                                    + "Status: %s",
                            order.getOrderId(),
                            order.getSubtotal(),
                            order.getDiscount(),
                            order.getTax(),
                            order.getDeliveryCharge(),
                            finalTotal,
                            order.getStatus()
                    ),
                    "SAIVEE - Order Confirmed",
                    JOptionPane.INFORMATION_MESSAGE
            );

            String invoice = invoiceService.generateInvoice(order);
            if (!invoice.isBlank()) {
                JTextArea invoiceArea = new JTextArea(invoice);
                invoiceArea.setEditable(false);
                invoiceArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
                JScrollPane invoiceScroll = new JScrollPane(invoiceArea);
                invoiceScroll.setPreferredSize(new Dimension(650, 480));
                JOptionPane.showMessageDialog(this, invoiceScroll,
                        "SAIVEE - Invoice", JOptionPane.INFORMATION_MESSAGE);
            }

            loadCart();

        } catch (Exception e) {

            orderProcessingService
                    .releaseCartReservation(
                            cartId
                    );

            JOptionPane.showMessageDialog(
                    this,
                    "Order could not be completed.\n\n"
                            + e.getMessage(),
                    "SAIVEE - Order Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // EMPTY CART
    // =========================================================

    private void showEmptyCart() {

        JPanel empty =
                new JPanel(
                        new GridLayout(
                                3,
                                1,
                                5,
                                10
                        )
                );

        empty.setBackground(
                Color.WHITE
        );

        empty.setBorder(
                new EmptyBorder(
                        40,
                        20,
                        40,
                        20
                )
        );

        JLabel icon =
                new JLabel(
                        "YOUR CART IS EMPTY",
                        SwingConstants.CENTER
                );

        icon.setForeground(NAVY);

        icon.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        22
                )
        );

        JLabel message =
                new JLabel(
                        "Add products from the SAIVEE catalogue.",
                        SwingConstants.CENTER
                );

        message.setForeground(
                DARK_BLUE
        );

        JButton shoppingButton =
                createButton(
                        "CONTINUE SHOPPING",
                        BLUE
                );

        shoppingButton.addActionListener(
                e -> dispose()
        );

        empty.add(icon);
        empty.add(message);
        empty.add(shoppingButton);

        cartPanel.add(empty);
    }

    // =========================================================
    // LABEL STYLE
    // =========================================================

    private JLabel createSummaryLabel(
            String text) {

        JLabel label =
                new JLabel(text);

        label.setForeground(NAVY);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        return label;
    }

    // =========================================================
    // BUTTON STYLE
    // =========================================================

    private JButton createButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text);

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE,
                                1
                        ),
                        new EmptyBorder(
                                8,
                                12,
                                8,
                                12
                        )
                )
        );

        return button;
    }

    private JButton createSmallButton(
            String text,
            Color color) {

        JButton button =
                new JButton(text);

        button.setBackground(color);

        button.setForeground(Color.WHITE);

        button.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        button.setFocusPainted(false);

        button.setCursor(
                new Cursor(
                        Cursor.HAND_CURSOR
                )
        );

        return button;
    }
}
