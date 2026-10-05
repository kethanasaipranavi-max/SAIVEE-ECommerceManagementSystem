package com.ecommerce.service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Customer;
import com.ecommerce.model.NotificationType;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.OrderStatus;
import com.ecommerce.model.Product;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.util.Base64;

public class OrderService {

    private final List<Order> orders;
    private final NotificationService notificationService;
    private final InventoryService inventoryService;
    private final InvoiceService invoiceService;

    private int nextOrderId;
    private int nextNotificationId;

    private final Set<Integer> stockRestoredOrders;
    private final Path historyFile = Paths.get("data", "orders.dat");

    // =========================================================
    // DEFAULT CONSTRUCTOR
    // =========================================================

    public OrderService() {
        this(
                new NotificationService(),
                null
        );
    }

    // =========================================================
    // SHARED SERVICES CONSTRUCTOR
    // =========================================================

    public OrderService(
            NotificationService notificationService,
            InventoryService inventoryService) {

        orders = new ArrayList<>();

        this.notificationService =
                notificationService != null
                        ? notificationService
                        : new NotificationService();

        this.inventoryService = inventoryService;
        this.invoiceService = new InvoiceService();

        stockRestoredOrders = new HashSet<>();

        nextOrderId = 10001;
        nextNotificationId = 1;
    }

    // =========================================================
    // CREATE ORDER
    // =========================================================

    public synchronized Order createOrder(
            Customer customer,
            Cart cart) {

        if (customer == null) {
            System.out.println(
                    "Cannot create order: customer is null."
            );
            return null;
        }

        if (cart == null
                || cart.getItems() == null
                || cart.getItems().isEmpty()) {

            System.out.println(
                    "Cannot create order: cart is empty."
            );
            return null;
        }

        int orderId = nextOrderId++;

        Order order =
                new Order(
                        orderId,
                        customer
                );

        for (OrderItem cartItem : cart.getItems()) {

            if (cartItem == null
                    || cartItem.getProduct() == null
                    || cartItem.getQuantity() <= 0) {
                continue;
            }

            OrderItem orderItem =
                    new OrderItem(
                            cartItem.getProduct(),
                            cartItem.getQuantity()
                    );

            order.addItem(orderItem);
        }

        if (order.getOrderItems().isEmpty()) {
            System.out.println(
                    "Cannot create order: no valid items."
            );
            return null;
        }

        order.calculateTotal();
        order.updateStatus(OrderStatus.PLACED);
        orders.add(order);
        saveHistory();
        AuditLogService.getInstance().log(
                "CUSTOMER",
                "Created order #" + orderId
        );

        System.out.println("Order created successfully.");
        System.out.println("Order ID: " + orderId);

        invoiceService.generateInvoice(order);
        notificationService.sendOrderPlacedNotification(
                nextNotificationId++,
                order,
                invoiceService.getInvoicePdfPath(order)
        );

        return order;
    }

    // =========================================================
    // ADD EXISTING ORDER
    // =========================================================

    public synchronized boolean addOrder(Order order) {

        if (order == null) {
            System.out.println("Invalid order.");
            return false;
        }

        if (findOrderById(order.getOrderId()) != null) {
            System.out.println("Order ID already exists.");
            return false;
        }

        orders.add(order);
        saveHistory();

        if (order.getOrderId() >= nextOrderId) {
            nextOrderId = order.getOrderId() + 1;
        }

        System.out.println("Order added successfully.");

        Customer customer = order.getCustomer();

        if (customer != null) {
            invoiceService.generateInvoice(order);
            notificationService.sendOrderPlacedNotification(
                    nextNotificationId++,
                    order,
                    invoiceService.getInvoicePdfPath(order)
            );
        }

        return true;
    }

    /** Load persisted customer order history after users and catalogue are ready. */
    public synchronized void loadPersistedOrders(UserService userService, ProductService productService) {
        if (!orders.isEmpty() || !Files.exists(historyFile) || userService == null || productService == null) return;
        try {
            for (String line : Files.readAllLines(historyFile, StandardCharsets.UTF_8)) {
                String[] x = line.split("\\|", -1);
                if (x.length < 9) continue;
                try {
                    int id=Integer.parseInt(x[0]);
                    Customer c=userService.findCustomerByEmail(dec(x[1]));
                    if(c==null) continue;
                    Order o=new Order(id,c);
                    String[] items=x[8].isBlank()?new String[0]:x[8].split(";",-1);
                    for(String item:items){String[] z=item.split("~",-1); if(z.length<4) continue; Product p=productService.findProductById(Integer.parseInt(z[0])); if(p==null) continue; OrderItem oi=new OrderItem(p,Integer.parseInt(z[1])); oi.setVariantKey(dec(z[2])); o.addItem(oi);}
                    o.setCharges(Double.parseDouble(x[4]),Double.parseDouble(x[5])); o.updateStatus(OrderStatus.valueOf(x[3])); orders.add(o); nextOrderId=Math.max(nextOrderId,id+1);
                } catch(Exception ignored) {}
            }
        } catch(Exception e){System.out.println("Order history load failed: "+e.getMessage());}
    }

    private synchronized void saveHistory(){
        try{Files.createDirectories(historyFile.getParent()); List<String> lines=new ArrayList<>(); for(Order o:orders){StringBuilder items=new StringBuilder(); for(OrderItem i:o.getOrderItems()){if(items.length()>0)items.append(';'); items.append(i.getProduct().getProductId()).append('~').append(i.getQuantity()).append('~').append(enc(i.getVariantKey())).append('~').append(i.getPriceAtPurchase());} lines.add(o.getOrderId()+"|"+enc(o.getCustomer()==null?"":o.getCustomer().getEmail())+"|"+enc(o.getCustomer()==null?"":o.getCustomer().getName())+"|"+o.getStatus()+"|"+o.getDiscount()+"|"+o.getDeliveryCharge()+"|"+o.getSubtotal()+"|"+o.getTotalAmount()+"|"+items); } Files.write(historyFile,lines,StandardCharsets.UTF_8); }catch(Exception e){System.out.println("Order history save failed: "+e.getMessage());}
    }
    private static String enc(String s){return Base64.getEncoder().encodeToString((s==null?"":s).getBytes(StandardCharsets.UTF_8));}
    private static String dec(String s){return s==null||s.isBlank()?"":new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8);}

    // =========================================================
    // FIND ORDER
    // =========================================================

    public synchronized Order findOrderById(int orderId) {

        for (Order order : orders) {

            if (order != null
                    && order.getOrderId() == orderId) {

                return order;
            }
        }

        return null;
    }

    // =========================================================
    // UPDATE ORDER STATUS
    // =========================================================

    public synchronized boolean updateOrderStatus(
            int orderId,
            OrderStatus status) {

        Order order = findOrderById(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }

        if (status == null) {
            System.out.println("Invalid order status.");
            return false;
        }

        OrderStatus currentStatus = order.getStatus();

        if (currentStatus == status) {
            System.out.println(
                    "Order is already in " + status + " status."
            );
            return false;
        }

        // Admin order management intentionally allows selecting any status.
        // This is useful for manual corrections, demonstrations and recovery
        // of orders that may already have advanced outside the normal flow.
        // Customer-facing return/refund actions still use their own validation.
        order.updateStatus(status);
        saveHistory();

        System.out.println(
                "Order status updated successfully."
        );

        sendStatusNotification(order);

        return true;
    }

    // =========================================================
    // STATUS TRANSITION VALIDATION
    // =========================================================

    private boolean isValidStatusTransition(
            OrderStatus currentStatus,
            OrderStatus newStatus) {

        if (currentStatus == null || newStatus == null) {
            return false;
        }

        switch (currentStatus) {

            case PLACED:
                return newStatus == OrderStatus.CONFIRMED
                        || newStatus == OrderStatus.CANCELLED
                        || newStatus == OrderStatus.FAILED;

            case CONFIRMED:
                return newStatus == OrderStatus.PROCESSING
                        || newStatus == OrderStatus.CANCELLED;

            case PROCESSING:
                return newStatus == OrderStatus.SHIPPED
                        || newStatus == OrderStatus.CANCELLED;

            case SHIPPED:
                return newStatus == OrderStatus.OUT_FOR_DELIVERY;

            case OUT_FOR_DELIVERY:
                return newStatus == OrderStatus.DELIVERED;

            case DELIVERED:
                return newStatus == OrderStatus.RETURN_REQUESTED;

            case RETURN_REQUESTED:
                return newStatus == OrderStatus.RETURNED
                        || newStatus == OrderStatus.CANCELLED;

            case RETURNED:
                return newStatus == OrderStatus.REFUNDED;

            case REFUNDED:
                return false;

            case CANCELLED:
                return false;

            case FAILED:
                return false;

            default:
                return false;
        }
    }

    // =========================================================
    // CANCEL ORDER
    // =========================================================

    public synchronized boolean cancelOrder(int orderId) {

        Order order = findOrderById(orderId);

        if (order == null) {
            System.out.println("Order not found.");
            return false;
        }

        if (order.getStatus() == OrderStatus.DELIVERED) {
            System.out.println(
                    "Delivered order cannot be cancelled."
            );
            return false;
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            System.out.println(
                    "Order is already cancelled."
            );
            return false;
        }

        if (order.getStatus() == OrderStatus.SHIPPED
                || order.getStatus()
                == OrderStatus.OUT_FOR_DELIVERY) {

            System.out.println(
                    "Order cannot be cancelled after shipping."
            );
            return false;
        }

        restoreOrderStock(order);

        order.updateStatus(OrderStatus.CANCELLED);
        saveHistory();

        System.out.println(
                "Order cancelled successfully."
        );

        PaymentHistoryService.Record payment = PaymentHistoryService.getInstance().findByOrder(order.getOrderId());
        String refundInfo;
        if (payment != null && !"Cash on Delivery".equals(payment.getMethod())) {
            refundInfo = "Your eligible refund of ₹" + String.format("%.2f", order.getTotalAmount()) + " will be initiated to the original " + payment.getMethod() + " payment channel. The payment provider/bank may take several business days to post the credit. Your transaction ID is " + payment.getPaymentId() + ".";
            PaymentHistoryService.getInstance().markRefunded(order.getOrderId());
        } else {
            refundInfo = "Because this was Cash on Delivery and payment is normally collected only at delivery, no cancellation refund is due. If payment was already collected in an exceptional case, SAIVEE will verify the transaction and arrange an eligible bank refund through a secure process.";
        }
        sendOrderNotification(order.getCustomer(), NotificationType.ORDER_CANCELLED,
                "Your SAIVEE order #" + order.getOrderId() + " has been cancelled. " + refundInfo + " This email confirms the cancellation/refund process; it does not claim a bank transfer has already completed. Never share OTPs, PINs, CVV or passwords.");

        return true;
    }

    // =========================================================
    // RESTORE ORDER STOCK
    // =========================================================

    private void restoreOrderStock(Order order) {

        if (order == null) {
            return;
        }

        if (inventoryService == null) {
            System.out.println(
                    "InventoryService is not connected. "
                            + "Stock could not be restored."
            );
            return;
        }

        int orderId = order.getOrderId();

        if (stockRestoredOrders.contains(orderId)) {
            return;
        }

        for (OrderItem item : order.getOrderItems()) {

            if (item == null
                    || item.getProduct() == null
                    || item.getQuantity() <= 0) {
                continue;
            }

            if (item.getVariantKey() == null || item.getVariantKey().isBlank())
                inventoryService.restoreStock(item.getProduct(), item.getQuantity());
            else
                inventoryService.restoreVariantStock(item.getProduct(), item.getQuantity(), item.getVariantKey());

            System.out.println(
                    "Stock restored for cancelled order: "
                            + item.getProduct().getProductName()
                            + " x"
                            + item.getQuantity()
            );
        }

        stockRestoredOrders.add(orderId);
    }

    // =========================================================
    // DISPLAY ALL ORDERS
    // =========================================================

    public synchronized void displayAllOrders() {

        if (orders.isEmpty()) {
            System.out.println("No orders available.");
            return;
        }

        System.out.println("===== SAIVEE ORDERS =====");

        for (Order order : orders) {

            if (order == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            order.displayOrder();
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // DISPLAY CUSTOMER ORDERS
    // =========================================================

    public synchronized void displayCustomerOrders(
            Customer customer) {

        if (customer == null) {
            System.out.println("Invalid customer.");
            return;
        }

        boolean found = false;

        for (Order order : orders) {

            if (order != null
                    && order.getCustomer() != null
                    && order.getCustomer().getUserId()
                    == customer.getUserId()) {

                System.out.println(
                        "----------------------------"
                );

                order.displayOrder();

                found = true;
            }
        }

        if (!found) {
            System.out.println(
                    "No orders found for this customer."
            );
        }
    }

    // =========================================================
    // GET CUSTOMER ORDERS
    // =========================================================

    public synchronized List<Order> getCustomerOrders(
            Customer customer) {

        List<Order> customerOrders =
                new ArrayList<>();

        if (customer == null) {
            return customerOrders;
        }

        for (Order order : orders) {

            if (order != null
                    && order.getCustomer() != null
                    && order.getCustomer().getUserId()
                    == customer.getUserId()) {

                customerOrders.add(order);
            }
        }

        return customerOrders;
    }

    // =========================================================
    // GET ORDER COUNT
    // =========================================================

    public synchronized int getOrderCount() {
        return orders.size();
    }

    // =========================================================
    // GET ALL ORDERS
    // =========================================================

    public synchronized List<Order> getOrders() {

        return Collections.unmodifiableList(
                new ArrayList<>(orders)
        );
    }

    // =========================================================
    // GET NEXT ORDER ID
    // =========================================================

    public synchronized int getNextOrderId() {
        return nextOrderId++;
    }

    // =========================================================
    // GET NOTIFICATION SERVICE
    // =========================================================

    public NotificationService getNotificationService() {
        return notificationService;
    }

    // =========================================================
    // GET INVENTORY SERVICE
    // =========================================================

    public InventoryService getInventoryService() {
        return inventoryService;
    }

    // =========================================================
    // SEND STATUS NOTIFICATION
    // =========================================================

    private void sendStatusNotification(
            Order order) {

        if (order == null
                || order.getCustomer() == null) {
            return;
        }

        Customer customer = order.getCustomer();

        NotificationType type;
        String message;

        switch (order.getStatus()) {

            case CONFIRMED:

                type = NotificationType.ORDER_CONFIRMED;

                message =
                        "Your SAIVEE order #"
                                + order.getOrderId()
                                + " has been confirmed.";

                break;

            case SHIPPED:

                type = NotificationType.ORDER_SHIPPED;

                message =
                        "Your SAIVEE order #"
                                + order.getOrderId()
                                + " has been shipped.";

                break;

            case DELIVERED:

                type = NotificationType.ORDER_DELIVERED;

                message =
                        "Your SAIVEE order #"
                                + order.getOrderId()
                                + " has been delivered successfully.";

                break;

            case CANCELLED:

                type = NotificationType.ORDER_CANCELLED;

                message =
                        "Your SAIVEE order #"
                                + order.getOrderId()
                                + " has been cancelled.";

                break;

            default:
                return;
        }

        sendOrderNotification(
                customer,
                type,
                message
        );
    }

    // =========================================================
    // SEND NOTIFICATION
    // =========================================================

    private synchronized void sendOrderNotification(
            Customer customer,
            NotificationType type,
            String message) {

        if (customer == null
                || type == null
                || message == null
                || message.isBlank()) {
            return;
        }

        notificationService.sendNotification(
                nextNotificationId++,
                customer,
                type,
                message
        );
    }
}