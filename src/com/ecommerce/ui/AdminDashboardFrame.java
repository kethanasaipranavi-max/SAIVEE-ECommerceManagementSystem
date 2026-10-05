package com.ecommerce.ui;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;
import com.ecommerce.service.AdminService;
import com.ecommerce.service.InventoryService;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReportService;
import com.ecommerce.service.ReturnService;
import com.ecommerce.service.AuditLogService;
import com.ecommerce.service.AnalyticsService;
import com.ecommerce.service.ReviewService;
import com.ecommerce.model.Review;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

public class AdminDashboardFrame extends JFrame {

    private final Admin admin;
    private final com.ecommerce.service.UserService userService;
    private final AdminService adminService;
    private final ProductService productService;
    private final InventoryService inventoryService;
    private final OrderService orderService;
    private final NotificationService notificationService;
    private final ReportService reportService;
    private final AnalyticsService analyticsService;

    private static final Color NAVY = new Color(13, 45, 82);
    private static final Color BLUE = new Color(40, 105, 190);
    private static final Color SKY = new Color(70, 150, 220);
    private static final Color GREEN = new Color(35, 150, 100);
    private static final Color ORANGE = new Color(225, 135, 45);
    private static final Color PURPLE = new Color(125, 80, 175);
    private static final Color RED = new Color(190, 55, 65);
    private static final Color TEAL = new Color(30, 145, 145);
    private static final Color LIGHT = new Color(242, 248, 255);

    public AdminDashboardFrame(
            Admin admin,
            com.ecommerce.service.UserService userService,
            AdminService adminService,
            ProductService productService,
            InventoryService inventoryService,
            OrderService orderService,
            NotificationService notificationService,
            ReportService reportService) {

        this.admin = admin;
        this.userService = userService;
        this.adminService = adminService;
        this.productService = productService;
        this.inventoryService = inventoryService;
        this.orderService = orderService;
        this.notificationService = notificationService;
        this.reportService = reportService;
        this.analyticsService = new AnalyticsService(orderService, userService, productService);

        setTitle("SAIVEE - Admin Control Center");
        setSize(1100, 760);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        addWindowListener(new java.awt.event.WindowAdapter() {
            @Override public void windowClosing(java.awt.event.WindowEvent e) { returnToMain(); }
        });
        setResizable(false);
        setLayout(new BorderLayout());

        createHeader();
        createDashboard();
        createFooter();
    }

    // =========================================================
    // HEADER
    // =========================================================

    private void createHeader() {

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(NAVY);
        header.setBorder(
                new EmptyBorder(18, 30, 18, 30)
        );

        JPanel logoPanel = new JPanel(new GridLayout(2, 1));
        logoPanel.setOpaque(false);

        JLabel logo = new JLabel("SAIVEE");
        logo.setForeground(Color.WHITE);
        logo.setFont(new Font("Arial", Font.BOLD, 34));

        JLabel subtitle = new JLabel(
                "ADMIN CONTROL CENTER"
        );
        subtitle.setForeground(new Color(190, 220, 250));
        subtitle.setFont(new Font("Arial", Font.BOLD, 13));

        logoPanel.add(logo);
        logoPanel.add(subtitle);

        JLabel welcome = new JLabel(
                "Welcome Admin, " + admin.getName() + "!"
        );
        welcome.setForeground(Color.WHITE);
        welcome.setFont(new Font("Arial", Font.BOLD, 19));

        header.add(logoPanel, BorderLayout.WEST);
        header.add(welcome, BorderLayout.EAST);

        add(header, BorderLayout.NORTH);
    }

    // =========================================================
    // DASHBOARD
    // =========================================================

    private void createDashboard() {

        JPanel main = new JPanel(new BorderLayout(15, 15));
        main.setBackground(LIGHT);
        main.setBorder(
                new EmptyBorder(18, 30, 18, 30)
        );

        JLabel title = new JLabel(
                "SAIVEE ADMIN MANAGEMENT",
                SwingConstants.CENTER
        );

        title.setForeground(NAVY);
        title.setFont(new Font("Arial", Font.BOLD, 25));

        main.add(title, BorderLayout.NORTH);

        JPanel cards = new JPanel(
                new GridLayout(5, 3, 15, 15)
        );
        cards.setOpaque(false);

        // -----------------------------------------------------
        // PRODUCT MANAGEMENT
        // -----------------------------------------------------

        cards.add(createButton(
                "ADD PRODUCT",
                "Add a new product",
                BLUE,
                e -> addProduct()
        ));

        cards.add(createButton(
                "UPDATE PRODUCT",
                "Edit product details",
                SKY,
                e -> updateProduct()
        ));

        cards.add(createButton(
                "REMOVE PRODUCT",
                "Delete a product",
                RED,
                e -> removeProduct()
        ));

        cards.add(createButton(
                "VIEW PRODUCTS",
                "View complete catalogue",
                NAVY,
                e -> viewProducts()
        ));

        cards.add(createButton(
                "ADD VARIANT",
                "Add size/color specific stock",
                TEAL,
                e -> addVariant()
        ));

        // -----------------------------------------------------
        // INVENTORY MANAGEMENT
        // -----------------------------------------------------

        cards.add(createButton(
                "ADD INVENTORY",
                "Add stock for a product",
                GREEN,
                e -> addInventory()
        ));

        cards.add(createButton(
                "UPDATE STOCK",
                "Change available stock",
                TEAL,
                e -> updateStock()
        ));

        cards.add(createButton(
                "REMOVE STOCK",
                "Reduce available stock",
                ORANGE,
                e -> removeStock()
        ));

        cards.add(createButton(
                "VIEW INVENTORY",
                "View all stock",
                PURPLE,
                e -> viewInventory()
        ));

        // -----------------------------------------------------
        // MONITORING
        // -----------------------------------------------------

        cards.add(createButton(
                "LOW STOCK PRODUCTS",
                "Find products needing stock",
                ORANGE,
                e -> viewLowStock()
        ));

        cards.add(createButton(
                "TRACK ALL ORDERS",
                "Track every customer order",
                BLUE,
                e -> trackOrders()
        ));

        cards.add(createButton(
                "REPORTS",
                "View business summary",
                GREEN,
                e -> openReports()
        ));

        cards.add(createButton(
                "RETURNS & REFUNDS",
                "Manage customer returns",
                TEAL,
                e -> manageReturns()
        ));

        cards.add(createButton(
                "CUSTOMER REVIEWS",
                "Read and reply to ratings",
                PURPLE,
                e -> manageReviews()
        ));

        cards.add(createButton(
                "AUDIT LOG",
                "View administrative activity",
                PURPLE,
                e -> showAuditLog()
        ));

        cards.add(createButton(
                "LOGOUT",
                "Return to SAIVEE home",
                RED,
                e -> logout()
        ));

        main.add(cards, BorderLayout.CENTER);

        add(main, BorderLayout.CENTER);
    }

    // =========================================================
    // DASHBOARD BUTTON
    // =========================================================

    private JButton createButton(
            String title,
            String description,
            Color background,
            java.awt.event.ActionListener action) {

        JButton button = new JButton();

        button.setBackground(background);
        button.setForeground(Color.WHITE);
        button.setFocusPainted(false);
        button.setCursor(
                new Cursor(Cursor.HAND_CURSOR)
        );

        button.setBorder(
                BorderFactory.createCompoundBorder(
                        BorderFactory.createLineBorder(
                                Color.WHITE, 2
                        ),
                        new EmptyBorder(
                                10, 10, 10, 10
                        )
                )
        );

        button.setLayout(
                new BorderLayout(5, 5)
        );

        JLabel titleLabel = new JLabel(
                title,
                SwingConstants.CENTER
        );

        titleLabel.setForeground(Color.WHITE);
        titleLabel.setFont(
                new Font("Arial", Font.BOLD, 15)
        );

        JLabel descriptionLabel = new JLabel(
                "<html><center>"
                        + description
                        + "</center></html>",
                SwingConstants.CENTER
        );

        descriptionLabel.setForeground(Color.WHITE);
        descriptionLabel.setFont(
                new Font("Arial", Font.PLAIN, 11)
        );

        button.add(
                titleLabel,
                BorderLayout.CENTER
        );

        button.add(
                descriptionLabel,
                BorderLayout.SOUTH
        );

        button.addActionListener(action);

        return button;
    }

    // =========================================================
    // ADD PRODUCT
    // =========================================================

    private void addProduct() {

        try {

            JTextField idField =
                    new JTextField();

            JTextField nameField =
                    new JTextField();

            JTextField categoryField =
                    new JTextField();

            JTextField priceField =
                    new JTextField();

            JTextField quantityField =
                    new JTextField();

            JTextField brandField =
                    new JTextField();

            JPanel panel =
                    createFormPanel(
                            new String[]{
                                    "Product ID:",
                                    "Product Name:",
                                    "Category:",
                                    "Price:",
                                    "Quantity:",
                                    "Brand:"
                            },
                            new JComponent[]{
                                    idField,
                                    nameField,
                                    categoryField,
                                    priceField,
                                    quantityField,
                                    brandField
                            }
                    );

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "SAIVEE - Add Product",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            int id =
                    Integer.parseInt(
                            idField.getText().trim()
                    );

            String name =
                    nameField.getText().trim();

            String category =
                    categoryField.getText().trim();

            double price =
                    Double.parseDouble(
                            priceField.getText().trim()
                    );

            int quantity =
                    Integer.parseInt(
                            quantityField.getText().trim()
                    );

            String brand =
                    brandField.getText().trim();

            if (name.isEmpty()
                    || category.isEmpty()
                    || brand.isEmpty()
                    || price < 0
                    || quantity < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Please enter valid product details.",
                        "Invalid Details",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            Product product =
                    new Product(
                            id,
                            name,
                            category,
                            price,
                            quantity,
                            brand
                    );

            boolean success =
                    adminService.addProduct(
                            admin.getUserId(),
                            product
                    );

            if (success) {

                // Also add the product to inventory
                // so the newly-created product is available
                // to the inventory system.
                adminService.addInventory(
                        admin.getUserId(),
                        product,
                        quantity
                );

                JOptionPane.showMessageDialog(
                        this,
                        "Product added successfully!",
                        "SAIVEE",
                        JOptionPane.INFORMATION_MESSAGE
                );

            } else {

                JOptionPane.showMessageDialog(
                        this,
                        "Product could not be added.",
                        "SAIVEE",
                        JOptionPane.ERROR_MESSAGE
                );
            }

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {

            showError(ex);
        }
    }

    private void addVariant() {
        Product product = chooseProduct("Select Product for Variant");
        if (product == null) return;
        JTextField size = new JTextField();
        JTextField color = new JTextField();
        JTextField stock = new JTextField();
        JPanel panel = createFormPanel(
                new String[]{"Size:", "Color:", "Variant Stock:"},
                new JComponent[]{size, color, stock});
        int result = JOptionPane.showConfirmDialog(this, panel, "SAIVEE - Add Product Variant",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        if (result != JOptionPane.OK_OPTION) return;
        try {
            int qty = Integer.parseInt(stock.getText().trim());
            if (qty < 0 || (size.getText().trim().isEmpty() && color.getText().trim().isEmpty())) throw new NumberFormatException();
            product.addVariant(new com.ecommerce.model.ProductVariant(size.getText(), color.getText(), qty));
            inventoryService.updateStock(product.getProductId(), product.getQuantity());
            JOptionPane.showMessageDialog(this, "Variant added. Total product stock is now " + product.getQuantity() + ".", "SAIVEE", JOptionPane.INFORMATION_MESSAGE);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Enter a valid size/color and non-negative stock.", "Invalid Variant", JOptionPane.ERROR_MESSAGE);
        }
    }

    // =========================================================
    // UPDATE PRODUCT
    // =========================================================

    private void updateProduct() {

        try {

            Product product =
                    chooseProduct(
                            "Select Product to Update"
                    );

            if (product == null) {
                return;
            }

            JTextField nameField =
                    new JTextField(
                            product.getProductName()
                    );

            JTextField categoryField =
                    new JTextField(
                            product.getCategory()
                    );

            JTextField priceField =
                    new JTextField(
                            String.valueOf(
                                    product.getPrice()
                            )
                    );

            JTextField quantityField =
                    new JTextField(
                            String.valueOf(
                                    product.getQuantity()
                            )
                    );

            JTextField brandField =
                    new JTextField(
                            product.getBrand()
                    );

            JPanel panel =
                    createFormPanel(
                            new String[]{
                                    "Product ID:",
                                    "Product Name:",
                                    "Category:",
                                    "Price:",
                                    "Quantity:",
                                    "Brand:"
                            },
                            new JComponent[]{
                                    new JLabel(
                                            String.valueOf(
                                                    product.getProductId()
                                            )
                                    ),
                                    nameField,
                                    categoryField,
                                    priceField,
                                    quantityField,
                                    brandField
                            }
                    );

            int result =
                    JOptionPane.showConfirmDialog(
                            this,
                            panel,
                            "SAIVEE - Update Product",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.PLAIN_MESSAGE
                    );

            if (result != JOptionPane.OK_OPTION) {
                return;
            }

            product.setProductName(
                    nameField.getText().trim()
            );

            product.setCategory(
                    categoryField.getText().trim()
            );

            product.setPrice(
                    Double.parseDouble(
                            priceField.getText().trim()
                    )
            );

            product.setQuantity(
                    Integer.parseInt(
                            quantityField.getText().trim()
                    )
            );

            product.setBrand(
                    brandField.getText().trim()
            );

            boolean success =
                    adminService.updateProduct(
                            admin.getUserId(),
                            product
                    );

            JOptionPane.showMessageDialog(
                    this,
                    success
                            ? "Product updated successfully!"
                            : "Product could not be updated.",
                    "SAIVEE",
                    success
                            ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter valid numeric values.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );

        } catch (Exception ex) {

            showError(ex);
        }
    }

    // =========================================================
    // REMOVE PRODUCT
    // =========================================================

    private void removeProduct() {

        Product product =
                chooseProduct(
                        "Select Product to Remove"
                );

        if (product == null) {
            return;
        }

        int confirm =
                JOptionPane.showConfirmDialog(
                        this,
                        "Remove this product?\n\n"
                                + product.getProductName()
                                + "\nProduct ID: "
                                + product.getProductId(),
                        "Confirm Product Removal",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.WARNING_MESSAGE
                );

        if (confirm != JOptionPane.YES_OPTION) {
            return;
        }

        boolean success =
                adminService.removeProduct(
                        admin.getUserId(),
                        product.getProductId()
                );

        JOptionPane.showMessageDialog(
                this,
                success
                        ? "Product removed successfully!"
                        : "Product could not be removed.",
                "SAIVEE",
                success
                        ? JOptionPane.INFORMATION_MESSAGE
                        : JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // VIEW PRODUCTS
    // =========================================================

    private void viewProducts() {

        List<Product> products =
                productService.getProducts();

        String[] columns = {
                "ID",
                "Product",
                "Category",
                "Brand",
                "Price",
                "Quantity"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        for (Product product : products) {

            model.addRow(
                    new Object[]{
                            product.getProductId(),
                            product.getProductName(),
                            product.getCategory(),
                            product.getBrand(),
                            "₹" + String.format(
                                    "%.2f",
                                    product.getPrice()
                            ),
                            product.getQuantity()
                    }
            );
        }

        showTableWindow(
                "SAIVEE - Product Catalogue",
                model,
                850,
                500
        );
    }

    // =========================================================
    // ADD INVENTORY
    // =========================================================

    private void addInventory() {

        Product product =
                chooseProduct(
                        "Select Product for Inventory"
                );

        if (product == null) {
            return;
        }

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter stock quantity to add:",
                        "SAIVEE - Add Inventory",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (input == null) {
            return;
        }

        try {

            int stock =
                    Integer.parseInt(
                            input.trim()
                    );

            if (stock < 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Stock cannot be negative.",
                        "Invalid Stock",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            boolean success =
                    adminService.addInventory(
                            admin.getUserId(),
                            product,
                            stock
                    );

            JOptionPane.showMessageDialog(
                    this,
                    success
                            ? "Inventory added successfully!"
                            : "Inventory could not be added.",
                    "SAIVEE",
                    success
                            ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid stock quantity.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // UPDATE STOCK
    // =========================================================

    private void updateStock() {

        Product product =
                chooseProduct(
                        "Select Product to Update Stock"
                );

        if (product == null) {
            return;
        }

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter NEW stock quantity:",
                        String.valueOf(
                                product.getQuantity()
                        )
                );

        if (input == null) {
            return;
        }

        try {

            int quantity =
                    Integer.parseInt(
                            input.trim()
                    );

            boolean success =
                    adminService.updateStock(
                            admin.getUserId(),
                            product.getProductId(),
                            quantity
                    );

            // Keep product quantity synchronized
            // with the inventory quantity.
            if (success) {
                product.setQuantity(quantity);
            }

            JOptionPane.showMessageDialog(
                    this,
                    success
                            ? "Stock updated successfully!"
                            : "Stock could not be updated.",
                    "SAIVEE",
                    success
                            ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid quantity.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // REMOVE STOCK
    // =========================================================

    private void removeStock() {

        Product product =
                chooseProduct(
                        "Select Product to Remove Stock"
                );

        if (product == null) {
            return;
        }

        String input =
                JOptionPane.showInputDialog(
                        this,
                        "Enter quantity to remove:",
                        "SAIVEE - Remove Stock",
                        JOptionPane.QUESTION_MESSAGE
                );

        if (input == null) {
            return;
        }

        try {

            int quantity =
                    Integer.parseInt(
                            input.trim()
                    );

            if (quantity <= 0) {

                JOptionPane.showMessageDialog(
                        this,
                        "Quantity must be greater than zero.",
                        "Invalid Quantity",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            boolean success =
                    adminService.removeStock(
                            admin.getUserId(),
                            product.getProductId(),
                            quantity
                    );

            JOptionPane.showMessageDialog(
                    this,
                    success
                            ? "Stock removed successfully!"
                            : "Stock could not be removed.",
                    "SAIVEE",
                    success
                            ? JOptionPane.INFORMATION_MESSAGE
                            : JOptionPane.ERROR_MESSAGE
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Please enter a valid quantity.",
                    "Invalid Input",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // VIEW INVENTORY
    // =========================================================

    private void viewInventory() {

        String output =
                captureOutput(
                        () -> adminService.displayInventory(
                                admin.getUserId()
                        )
                );

        showTextWindow(
                "SAIVEE - Inventory Details",
                output
        );
    }

    // =========================================================
    // LOW STOCK
    // =========================================================

    private void viewLowStock() {

        String output =
                captureOutput(
                        () -> adminService.displayLowStockProducts(
                                admin.getUserId()
                        )
                );

        showTextWindow(
                "SAIVEE - Low Stock Products",
                output
        );
    }

    // =========================================================
    // TRACK ALL ORDERS
    // =========================================================

    private void trackOrders() {

        List<Order> orders =
                orderService.getOrders();

        String[] columns = {
                "Order ID",
                "Customer",
                "Total",
                "Status"
        };

        DefaultTableModel model =
                new DefaultTableModel(columns, 0) {

                    @Override
                    public boolean isCellEditable(
                            int row,
                            int column) {
                        return false;
                    }
                };

        for (Order order : orders) {

            String customerName = "Unknown";

            if (order.getCustomer() != null) {
                customerName =
                        order.getCustomer().getName();
            }

            model.addRow(
                    new Object[]{
                            order.getOrderId(),
                            customerName,
                            "₹" + String.format(
                                    "%.2f",
                                    order.getTotalAmount()
                            ),
                            order.getStatus()
                    }
            );
        }

        JTable table =
                new JTable(model);

        table.setRowHeight(30);
        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        table.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(table);

        scrollPane.setPreferredSize(
                new Dimension(
                        800,
                        400
                )
        );

        JButton updateButton =
                new JButton(
                        "UPDATE SELECTED ORDER STATUS"
                );

        updateButton.setBackground(BLUE);
        updateButton.setForeground(Color.WHITE);
        updateButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        updateButton.setFocusPainted(false);

        JButton detailsButton =
                new JButton(
                        "VIEW ORDER DETAILS"
                );

        detailsButton.setBackground(PURPLE);
        detailsButton.setForeground(Color.WHITE);
        detailsButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        detailsButton.setFocusPainted(false);

        JButton refreshButton =
                new JButton("REFRESH");

        refreshButton.setBackground(GREEN);
        refreshButton.setForeground(Color.WHITE);
        refreshButton.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        14
                )
        );

        refreshButton.setFocusPainted(false);

        JPanel buttons =
                new JPanel(
                        new FlowLayout(
                                FlowLayout.CENTER,
                                10,
                                10
                        )
                );

        buttons.add(updateButton);
        buttons.add(detailsButton);
        buttons.add(refreshButton);

        JPanel panel =
                new JPanel(
                        new BorderLayout()
                );

        panel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        panel.add(
                buttons,
                BorderLayout.SOUTH
        );

        updateButton.addActionListener(
                e -> {

                    int row =
                            table.getSelectedRow();

                    if (row < 0) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Please select an order first.",
                                "SAIVEE",
                                JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }

                    int orderId =
                            (Integer) model.getValueAt(
                                    row,
                                    0
                            );

                    updateSelectedOrderStatus(
                            orderId
                    );
                }
        );

        detailsButton.addActionListener(
                e -> {

                    int row =
                            table.getSelectedRow();

                    if (row < 0) {

                        JOptionPane.showMessageDialog(
                                this,
                                "Please select an order first.",
                                "SAIVEE",
                                JOptionPane.WARNING_MESSAGE
                        );

                        return;
                    }

                    int orderId =
                            (Integer) model.getValueAt(
                                    row,
                                    0
                            );

                    Order order =
                            adminService.findOrder(
                                    admin.getUserId(),
                                    orderId
                            );

                    if (order != null) {

                        showTextWindow(
                                "SAIVEE - Order "
                                        + orderId
                                        + " Details",
                                order.toString()
                        );
                    }
                }
        );

        refreshButton.addActionListener(
                e -> {

                    updateButton.setEnabled(false);

                    // Rebuild the table data
                    // from the SAME OrderService.
                    model.setRowCount(0);

                    for (Order order :
                            orderService.getOrders()) {

                        String customerName =
                                "Unknown";

                        if (order.getCustomer() != null) {
                            customerName =
                                    order.getCustomer()
                                            .getName();
                        }

                        model.addRow(
                                new Object[]{
                                        order.getOrderId(),
                                        customerName,
                                        "₹" + String.format(
                                                "%.2f",
                                                order.getTotalAmount()
                                        ),
                                        order.getStatus()
                                }
                        );
                    }

                    updateButton.setEnabled(true);
                }
        );

        JOptionPane.showMessageDialog(
                this,
                panel,
                "SAIVEE - Order Tracking",
                JOptionPane.PLAIN_MESSAGE
        );
    }

    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    private void updateSelectedOrderStatus(
            int orderId) {

        Order order =
                adminService.findOrder(
                        admin.getUserId(),
                        orderId
                );

        if (order == null) {

            JOptionPane.showMessageDialog(
                    this,
                    "Order not found.",
                    "SAIVEE",
                    JOptionPane.ERROR_MESSAGE
            );

            return;
        }

        JComboBox<OrderStatus> statusBox =
                new JComboBox<>(
                        OrderStatus.values()
                );

        statusBox.setSelectedItem(
                order.getStatus()
        );

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                2,
                                2,
                                10,
                                10
                        )
                );

        panel.add(
                new JLabel("Order ID:")
        );

        panel.add(
                new JLabel(
                        String.valueOf(orderId)
                )
        );

        panel.add(
                new JLabel("New Status:")
        );

        panel.add(statusBox);

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        "Update Order Status",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return;
        }

        OrderStatus newStatus =
                (OrderStatus) statusBox.getSelectedItem();

        boolean success =
                adminService.updateOrderStatus(
                        admin.getUserId(),
                        orderId,
                        newStatus
                );

        if (success) {

            JOptionPane.showMessageDialog(
                    this,
                    "Order " + orderId
                            + " status updated to "
                            + newStatus
                            + ".\n\n"
                            + "The customer will see the updated "
                            + "status when viewing My Orders.",
                    "SAIVEE Order Tracking",
                    JOptionPane.INFORMATION_MESSAGE
            );

        } else {

            JOptionPane.showMessageDialog(
                    this,
                    "Order status could not be updated.",
                    "SAIVEE",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // =========================================================
    // REPORTS
    // =========================================================

    private void openReports() {
        JTextArea analyticsArea = new JTextArea(analyticsService.buildAdminReport());
        analyticsArea.setEditable(false);
        analyticsArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        JScrollPane analyticsScroll = new JScrollPane(analyticsArea);
        analyticsScroll.setPreferredSize(new Dimension(760, 600));
        JOptionPane.showMessageDialog(this, analyticsScroll, "SAIVEE - Analytics & Reports", JOptionPane.INFORMATION_MESSAGE);

        StringBuilder report =
                new StringBuilder();

        report.append(
                "================================================\n"
        );

        report.append(
                "             SAIVEE ADMIN REPORT\n"
        );

        report.append(
                "================================================\n\n"
        );

        report.append(
                "ADMIN INFORMATION\n"
        );

        report.append(
                "------------------------------------------------\n"
        );

        report.append(
                "Admin ID: "
        ).append(
                admin.getUserId()
        ).append("\n");

        report.append(
                "Admin Name: "
        ).append(
                admin.getName()
        ).append("\n\n");

        report.append(
                "BUSINESS SUMMARY\n"
        );

        report.append(
                "------------------------------------------------\n"
        );

        report.append(
                "Total Products: "
        ).append(
                productService.getProductCount()
        ).append("\n");

        report.append(
                "Total Orders: "
        ).append(
                orderService.getOrders().size()
        ).append("\n");

        report.append(
                "Total Inventory Records: "
        ).append(
                inventoryService.getInventoryCount()
        ).append("\n\n");

        report.append(
                "ORDER STATUS SUMMARY\n"
        );

        report.append(
                "------------------------------------------------\n"
        );

        for (OrderStatus status :
                OrderStatus.values()) {

            int count = 0;

            for (Order order :
                    orderService.getOrders()) {

                if (order.getStatus() == status) {
                    count++;
                }
            }

            report.append(
                    status
            ).append(
                    ": "
            ).append(
                    count
            ).append("\n");
        }

        report.append(
                "\n================================================\n"
        );

        report.append(
                "              END OF REPORT\n"
        );

        report.append(
                "================================================\n"
        );

        showTextWindow(
                "SAIVEE - Reports",
                report.toString()
        );
    }

    // =========================================================
    // PRODUCT SELECTOR
    // =========================================================

    private Product chooseProduct(
            String title) {

        List<Product> products =
                productService.getProducts();

        if (products == null
                || products.isEmpty()) {

            JOptionPane.showMessageDialog(
                    this,
                    "No products are available.",
                    "SAIVEE",
                    JOptionPane.INFORMATION_MESSAGE
            );

            return null;
        }

        JComboBox<Product> combo =
                new JComboBox<>();

        for (Product product :
                products) {

            combo.addItem(product);
        }

        combo.setRenderer(
                new DefaultListCellRenderer() {

                    @Override
                    public Component getListCellRendererComponent(
                            JList<?> list,
                            Object value,
                            int index,
                            boolean isSelected,
                            boolean cellHasFocus) {

                        super.getListCellRendererComponent(
                                list,
                                value,
                                index,
                                isSelected,
                                cellHasFocus
                        );

                        if (value instanceof Product) {

                            Product product =
                                    (Product) value;

                            setText(
                                    product.getProductId()
                                            + " - "
                                            + product.getProductName()
                                            + " | ₹"
                                            + product.getPrice()
                            );
                        }

                        return this;
                    }
                }
        );

        JPanel panel =
                new JPanel(
                        new BorderLayout(
                                10,
                                10
                        )
                );

        panel.add(
                new JLabel(
                        "Select Product:"
                ),
                BorderLayout.NORTH
        );

        panel.add(
                combo,
                BorderLayout.CENTER
        );

        int result =
                JOptionPane.showConfirmDialog(
                        this,
                        panel,
                        title,
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.PLAIN_MESSAGE
                );

        if (result != JOptionPane.OK_OPTION) {
            return null;
        }

        return (Product) combo.getSelectedItem();
    }

    // =========================================================
    // FORM PANEL
    // =========================================================

    private JPanel createFormPanel(
            String[] labels,
            JComponent[] components) {

        JPanel panel =
                new JPanel(
                        new GridLayout(
                                labels.length,
                                2,
                                10,
                                10
                        )
                );

        for (int i = 0;
             i < labels.length;
             i++) {

            panel.add(
                    new JLabel(
                            labels[i]
                    )
            );

            panel.add(
                    components[i]
            );
        }

        return panel;
    }

    // =========================================================
    // TEXT WINDOW
    // =========================================================

    private void showTextWindow(
            String title,
            String content) {

        JTextArea textArea =
                new JTextArea(content);

        textArea.setEditable(false);
        textArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        textArea.setForeground(NAVY);
        textArea.setBackground(LIGHT);

        textArea.setCaretPosition(0);

        textArea.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JScrollPane scroll =
                new JScrollPane(textArea);

        scroll.setPreferredSize(
                new Dimension(
                        850,
                        520
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // TABLE WINDOW
    // =========================================================

    private void showTableWindow(
            String title,
            DefaultTableModel model,
            int width,
            int height) {

        JTable table =
                new JTable(model);

        table.setRowHeight(28);

        table.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        table.getTableHeader().setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        13
                )
        );

        JScrollPane scroll =
                new JScrollPane(table);

        scroll.setPreferredSize(
                new Dimension(
                        width,
                        height
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scroll,
                title,
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // CAPTURE SERVICE OUTPUT
    // =========================================================

    private String captureOutput(
            Runnable action) {

        java.io.PrintStream original =
                System.out;

        java.io.ByteArrayOutputStream output =
                new java.io.ByteArrayOutputStream();

        java.io.PrintStream temporary =
                new java.io.PrintStream(output);

        try {

            System.setOut(temporary);
            action.run();

        } finally {

            temporary.flush();
            System.setOut(original);
        }

        String result =
                output.toString();

        if (result.trim().isEmpty()) {
            return "No information available.";
        }

        return result;
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            Exception ex) {

        JOptionPane.showMessageDialog(
                this,
                "Operation could not be completed.\n\n"
                        + ex.getMessage(),
                "SAIVEE Error",
                JOptionPane.ERROR_MESSAGE
        );
    }

    // =========================================================
    // LOGOUT
    // =========================================================

    private void manageReturns() {
        ReturnService returns = new ReturnService(orderService, inventoryService);
        List<com.ecommerce.model.ReturnRecord> records = returns.getRecords();
        if (records.isEmpty()) { JOptionPane.showMessageDialog(this,"No return requests found.","SAIVEE - Returns & Refunds",JOptionPane.INFORMATION_MESSAGE); return; }
        String[] choices = records.stream().map(r -> "#"+r.getOrderId()+" - "+r.getStatus()+" - "+(orderService.findOrderById(r.getOrderId())==null?"Customer":orderService.findOrderById(r.getOrderId()).getCustomer().getName())).toArray(String[]::new);
        String selected=(String)JOptionPane.showInputDialog(this,"Select a return record:","SAIVEE - Returns & Refunds",JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]); if(selected==null)return;
        int id=Integer.parseInt(selected.substring(1,selected.indexOf(" "))); com.ecommerce.model.ReturnRecord r=returns.getRecord(id); if(r==null)return;
        String[] actions;
        switch(r.getStatus()){case REQUESTED -> actions=new String[]{"Schedule Pickup","Reject","Close"}; case PICKUP_SCHEDULED -> actions=new String[]{"Mark Picked Up","Reject","Close"}; case PICKED_UP -> actions=new String[]{"Start Inspection","Reject","Close"}; case UNDER_INSPECTION -> actions=new String[]{"Approve Return","Reject","Close"}; case APPROVED -> actions=new String[]{"Process Refund","Close"}; default -> actions=new String[]{"Close"};}
        int a=JOptionPane.showOptionDialog(this,"Order #"+id+"\nReason: "+r.getReason()+"\nStatus: "+r.getStatus()+"\nPickup: "+(r.getPickupDate()==null?"Not scheduled":r.getPickupDate()),"SAIVEE - Return Workflow",JOptionPane.DEFAULT_OPTION,JOptionPane.QUESTION_MESSAGE,null,actions,actions[0]); if(a<0)return; String chosen=actions[a];
        boolean ok=false;
        if(chosen.equals("Schedule Pickup")){String d=JOptionPane.showInputDialog(this,"Pickup date/time (yyyy-MM-dd HH:mm):");if(d!=null)try{ok=returns.schedulePickup(id,java.time.LocalDateTime.parse(d.trim(),java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")));}catch(Exception ignored){}}
        else if(chosen.equals("Mark Picked Up"))ok=returns.markPickedUp(id);
        else if(chosen.equals("Start Inspection"))ok=returns.startInspection(id);
        else if(chosen.equals("Approve Return"))ok=returns.approveReturn(id);
        else if(chosen.equals("Reject")){String note=JOptionPane.showInputDialog(this,"Reason for rejection:");ok=returns.rejectReturn(id,note);}
        else if(chosen.equals("Process Refund"))ok=returns.refund(id);
        if(!chosen.equals("Close"))JOptionPane.showMessageDialog(this,ok?"Return workflow updated and customer notified by email.":"The return action could not be completed.","SAIVEE",ok?JOptionPane.INFORMATION_MESSAGE:JOptionPane.WARNING_MESSAGE);
    }

    private void manageReviews() {
        List<Review> reviews = ReviewService.getInstance().getAll();
        if (reviews.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No customer reviews yet.", "SAIVEE - Customer Reviews", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        String[] choices = reviews.stream().map(r -> "#" + r.getReviewId() + " - " + r.getProductName() + " - " + r.getStars() + "/5 - " + r.getCustomerName()).toArray(String[]::new);
        String selected=(String)JOptionPane.showInputDialog(this,"Select a review to view or reply:","SAIVEE - Customer Reviews",JOptionPane.QUESTION_MESSAGE,null,choices,choices[0]);
        if(selected==null)return; long id=Long.parseLong(selected.substring(1,selected.indexOf(" "))); Review r=reviews.stream().filter(x->x.getReviewId()==id).findFirst().orElse(null); if(r==null)return;
        String details="Customer: "+r.getCustomerName()+" <"+r.getCustomerEmail()+">\nProduct: "+r.getProductName()+"\nRating: "+r.getStars()+"/5\n\nReview:\n"+r.getText()+"\n\nReply: "+(r.hasReply()?r.getAdminReply():"No reply yet");
        int action=JOptionPane.showOptionDialog(this,details,"SAIVEE - Review",JOptionPane.DEFAULT_OPTION,JOptionPane.INFORMATION_MESSAGE,null,new String[]{"Reply by Email","Close"},"Reply by Email");
        if(action==0){String reply=JOptionPane.showInputDialog(this,"Enter your response. It will be emailed to the customer and saved with the review:");if(reply!=null&&!reply.isBlank()){boolean ok=ReviewService.getInstance().reply(id,reply);JOptionPane.showMessageDialog(this,ok?"Reply saved and emailed to the customer.":"Reply could not be saved.");}}
    }

    private void showAuditLog() {
        JTextArea area = new JTextArea(AuditLogService.getInstance().getFormattedLogs());
        area.setEditable(false);
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        JScrollPane pane = new JScrollPane(area);
        pane.setPreferredSize(new Dimension(800, 500));
        JOptionPane.showMessageDialog(this, pane,
                "SAIVEE - Audit Log", JOptionPane.INFORMATION_MESSAGE);
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
                        JOptionPane.QUESTION_MESSAGE
                );

        if (choice != JOptionPane.YES_OPTION) {
            return;
        }

        MainFrame mainFrame = null;

        for (Window window :
                Window.getWindows()) {

            if (window instanceof MainFrame) {

                mainFrame =
                        (MainFrame) window;

                break;
            }
        }

        returnToMain();

        if (mainFrame != null) {

            mainFrame.setVisible(true);
            mainFrame.toFront();
            mainFrame.requestFocus();

        } else {

            JOptionPane.showMessageDialog(
                    null,
                    "SAIVEE main page could not be restored.",
                    "Logout",
                    JOptionPane.WARNING_MESSAGE
            );
        }
    }

    // =========================================================
    // FOOTER
    // =========================================================

    private void createFooter() {

        JPanel footer =
                new JPanel(
                        new BorderLayout()
                );

        footer.setBackground(NAVY);

        footer.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        JLabel label =
                new JLabel(
                        "SAIVEE • ADMIN PORTAL • "
                                + "Secure Management System",
                        SwingConstants.CENTER
                );

        label.setForeground(Color.WHITE);

        label.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        footer.add(
                label,
                BorderLayout.CENTER
        );

        add(
                footer,
                BorderLayout.SOUTH
        );
    }
}
