package com.ecommerce.service;

import com.ecommerce.model.Customer;
import com.ecommerce.model.Notification;
import com.ecommerce.model.NotificationType;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.Base64;

public class NotificationService {
    private final List<Notification> notifications;
    private final EmailService emailService;
    private final Path historyFile = Paths.get("data", "notifications.dat");

    public NotificationService() {
        notifications = new ArrayList<>();
        emailService = new EmailService();
    }

    public synchronized void sendNotification(int notificationId, Customer customer,
                                               NotificationType type, String message) {
        if (customer == null) {
            System.out.println("Cannot send notification: customer not found.");
            return;
        }

        Notification notification = new Notification(notificationId, customer, type, message);
        notifications.add(notification);
        saveHistory();
        System.out.println("Notification created successfully.");

        String customerEmail = customer.getEmail();
        if (customerEmail != null && !customerEmail.isBlank()) {
            String subject = buildSubject(type);
            String text = buildText(customer.getName(), type, message);
            emailService.sendEmail(customerEmail, subject, text, buildStatusHtml(customer.getName(), type, message), null);
        } else {
            System.out.println("Customer email not available. Email was not sent.");
        }
    }

    /** Rich order-confirmation notification with invoice PDF attachment. */
    public synchronized void sendOrderPlacedNotification(int notificationId, Order order, Path invoicePdf) {
        if (order == null || order.getCustomer() == null) return;
        Customer customer = order.getCustomer();
        String message = "Your SAIVEE order #" + order.getOrderId() + " has been placed successfully.";

        Notification notification = new Notification(notificationId, customer, NotificationType.ORDER_PLACED, message);
        notifications.add(notification);
        saveHistory();
        System.out.println("Notification created successfully.");

        String email = customer.getEmail();
        if (email == null || email.isBlank()) {
            System.out.println("Customer email not available. Email was not sent.");
            return;
        }

        String subject = "SAIVEE Order #" + order.getOrderId() + " confirmed";
        String text = buildOrderText(order);
        String html = buildOrderPlacedHtml(order);
        emailService.sendEmail(email, subject, text, html, invoicePdf);
    }

    public synchronized void sendAdminNotification(NotificationType type, String message) {
        String adminEmail = System.getenv("SAIVEE_ADMIN_EMAIL");
        if (adminEmail == null || adminEmail.isBlank()) {
            System.out.println("SAIVEE_ADMIN_EMAIL is not configured.");
            return;
        }
        emailService.sendEmail(adminEmail, buildSubject(type), buildText("SAIVEE Administrator", type, message),
                buildStatusHtml("SAIVEE Administrator", type, message), null);
    }

    public void displayCustomerNotifications(Customer customer) {
        if (customer == null) { System.out.println("Customer not found."); return; }
        boolean found = false;
        for (Notification notification : notifications) {
            if (notification.getCustomer() != null && notification.getCustomer().getUserId() == customer.getUserId()) {
                notification.displayNotification(); found = true;
            }
        }
        if (!found) System.out.println("No notifications found for this customer.");
    }

    public synchronized void markNotificationAsRead(int notificationId) {
        for (Notification notification : notifications) {
            if (notification.getNotificationId() == notificationId) {
                notification.markAsRead();
                saveHistory();
                System.out.println("Notification marked as read.");
                return;
            }
        }
        System.out.println("Notification not found.");
    }

    public synchronized int getUnreadCount(Customer customer) {
        if (customer == null) return 0;
        int n=0; for(Notification x:notifications) if(x.getCustomer()!=null&&x.getCustomer().getUserId()==customer.getUserId()&&!x.isRead()) n++; return n;
    }

    public synchronized void loadPersistedNotifications(UserService users) {
        if (!notifications.isEmpty() || users == null || !Files.exists(historyFile)) return;
        try { for(String line:Files.readAllLines(historyFile,StandardCharsets.UTF_8)){String[] x=line.split("\\|",-1); if(x.length<5)continue; try{Customer c=users.findCustomerByEmail(dec(x[1])); if(c==null)continue; Notification n=new Notification(Integer.parseInt(x[0]),c,NotificationType.valueOf(x[2]),dec(x[3])); if(Boolean.parseBoolean(x[4]))n.markAsRead(); notifications.add(n);}catch(Exception ignored){}} } catch(Exception e){System.out.println("Notification history load failed: "+e.getMessage());}
    }

    private synchronized void saveHistory(){
        try{Files.createDirectories(historyFile.getParent());List<String> lines=new ArrayList<>();for(Notification n:notifications){lines.add(n.getNotificationId()+"|"+enc(n.getCustomer()==null?"":n.getCustomer().getEmail())+"|"+n.getType()+"|"+enc(n.getMessage())+"|"+n.isRead());}Files.write(historyFile,lines,StandardCharsets.UTF_8);}catch(Exception e){System.out.println("Notification history save failed: "+e.getMessage());}
    }
    private static String enc(String s){return Base64.getEncoder().encodeToString((s==null?"":s).getBytes(StandardCharsets.UTF_8));}
    private static String dec(String s){return s==null||s.isBlank()?"":new String(Base64.getDecoder().decode(s),StandardCharsets.UTF_8);}

    public void displayAllNotifications() {
        if (notifications.isEmpty()) { System.out.println("No notifications available."); return; }
        for (Notification notification : notifications) notification.displayNotification();
    }

    public synchronized int getNotificationCount() { return notifications.size(); }

    public synchronized List<Notification> getCustomerNotifications(Customer customer) {
        if (customer == null) return List.of();
        List<Notification> result=new ArrayList<>(); for(Notification n:notifications) if(n.getCustomer()!=null&&n.getCustomer().getUserId()==customer.getUserId()) result.add(n); return List.copyOf(result);
    }

    private String buildSubject(NotificationType type) {
        if (type == null) return "SAIVEE Notification";
        switch (type) {
            case ORDER_PLACED: return "SAIVEE - Order Placed";
            case PAYMENT_SUCCESS: return "SAIVEE - Payment Successful";
            case PAYMENT_FAILED: return "SAIVEE - Payment Failed";
            case ORDER_CONFIRMED: return "SAIVEE - Order Confirmed";
            case ORDER_SHIPPED: return "SAIVEE - Order Shipped";
            case ORDER_DELIVERED: return "SAIVEE - Order Delivered";
            case ORDER_CANCELLED: return "SAIVEE - Order Cancelled";
            case RETURN_REQUESTED: return "SAIVEE - Return Request Received";
            case REFUND_PROCESSED: return "SAIVEE - Refund Processed";
            case LOW_STOCK: return "SAIVEE - Low Stock Alert";
            default: return "SAIVEE - Notification";
        }
    }

    private String buildText(String customerName, NotificationType type, String message) {
        return "Hello " + customerName + ",\n\n" + message + "\n\n"
                + "Notification: " + type + "\n\n"
                + "Thank you for choosing SAIVEE. We’ll keep you updated as your order progresses.\n\n"
                + "SAIVEE E-Commerce Management System";
    }

    private String buildStatusHtml(String customerName, NotificationType type, String message) {
        String title = friendlyTitle(type);
        String safeName = h(customerName);
        String safeMessage = h(message);
        String badge = h(type == null ? "NOTIFICATION" : type.toString().replace('_', ' '));

        String extra = "";
        if (type == NotificationType.ORDER_DELIVERED) {
            extra = "<div style='margin-top:22px;padding:18px;background:#fffbeb;border:1px solid #fde68a;border-radius:12px'>"
                    + "<div style='font-weight:700;color:#92400e;margin-bottom:6px'>How was your shopping experience?</div>"
                    + "<div style='color:#57534e;font-size:14px;line-height:1.6'>Your order has been delivered. You can now rate the products you purchased and write a review from the SAIVEE shopping catalogue. "
                    + "Your rating and review will be saved to the product and help other customers.</div></div>"
                    + "<div style='margin-top:18px;padding:16px;background:#f8fafc;border:1px solid #e5e7eb;border-radius:10px'>"
                    + "<strong>Review eligibility</strong><br><span style='font-size:13px;color:#667085'>Ratings and written reviews are available only for products from delivered orders. You can submit one review per purchased product.</span></div>";
        } else if (type == NotificationType.ORDER_CANCELLED) {
            extra = "<div style='margin-top:22px;padding:18px;background:#fff7ed;border:1px solid #fed7aa;border-radius:12px;color:#9a3412'>"
                    + "<strong>Refund information</strong><br><span style='font-size:14px;line-height:1.6'>If this order was prepaid, an eligible refund is processed back through the applicable payment method. The payment provider or bank may take additional processing time before the amount appears in the customer's account. "
                    + "For COD orders, no refund is due when payment was never collected.</span></div>";
        } else if (type == NotificationType.RETURN_REQUESTED) {
            extra = "<div style='margin-top:22px;padding:18px;background:#eff6ff;border:1px solid #bfdbfe;border-radius:12px;color:#1e3a8a'>"
                    + "<strong>Return process</strong><br><span style='font-size:14px;line-height:1.6'>SAIVEE will coordinate pickup, inspect the returned item and update the return decision. If approved, the eligible refund will move to processing according to the payment method.</span></div>";
        } else if (type == NotificationType.REFUND_PROCESSED) {
            extra = "<div style='margin-top:22px;padding:18px;background:#f0fdf4;border:1px solid #bbf7d0;border-radius:12px;color:#166534'>"
                    + "<strong>Refund update</strong><br><span style='font-size:14px;line-height:1.6'>The refund has been recorded by SAIVEE. Please allow your payment provider or bank the applicable processing time for the amount to appear in the destination account.</span></div>";
        }

        return shell(title,
                "<p style='font-size:16px;margin:0 0 18px'>Hi " + safeName + " 👋</p>"
                + "<div style='background:#f8fafc;border:1px solid #e5e7eb;border-radius:10px;padding:18px;margin-bottom:20px'>"
                + "<div style='font-size:12px;color:#667085;text-transform:uppercase;letter-spacing:.7px'>" + badge + "</div>"
                + "<div style='font-size:17px;font-weight:600;margin-top:7px'>" + safeMessage + "</div></div>"
                + extra
                + "<p style='color:#667085;margin-top:24px'>Thank you for choosing SAIVEE. We’ll keep you updated whenever there is an important change.</p>");
    }

    private String buildOrderText(Order order) {
        StringBuilder b = new StringBuilder();
        b.append("Hello ").append(order.getCustomer().getName()).append(",\n\n")
         .append("Your SAIVEE order #").append(order.getOrderId()).append(" has been placed successfully.\n\n")
         .append("ORDER DETAILS\n");
        for (OrderItem item : order.getOrderItems()) {
            b.append("- ").append(item.getProduct().getProductName()).append(" x")
             .append(item.getQuantity()).append(" : INR ").append(String.format("%.2f", item.calculateSubtotal())).append('\n');
        }
        b.append("\nSubtotal: INR ").append(String.format("%.2f", order.getSubtotal()))
         .append("\nDiscount: -INR ").append(String.format("%.2f", order.getDiscount()))
         .append("\nGST: INR ").append(String.format("%.2f", order.getTax()))
         .append("\nDelivery: INR ").append(String.format("%.2f", order.getDeliveryCharge()))
         .append("\nTotal: INR ").append(String.format("%.2f", order.getTotalAmount()))
         .append("\n\nYour invoice is attached to this email.\n\n")
         .append("Thank you for shopping with SAIVEE. Whenever you're ready, we'll be here for your next visit.");
        return b.toString();
    }

    private String buildOrderPlacedHtml(Order order) {
        StringBuilder rows = new StringBuilder();
        for (OrderItem item : order.getOrderItems()) {
            rows.append("<tr>")
                .append("<td style='padding:12px 8px;border-bottom:1px solid #eef0f3'>").append(h(item.getProduct().getProductName())).append("</td>")
                .append("<td style='padding:12px 8px;border-bottom:1px solid #eef0f3;text-align:center'>").append(item.getQuantity()).append("</td>")
                .append("<td style='padding:12px 8px;border-bottom:1px solid #eef0f3;text-align:right'>INR ").append(String.format("%.2f", item.calculateSubtotal())).append("</td>")
                .append("</tr>");
        }
        String content = "<p style='font-size:16px;margin:0 0 5px'>Hi " + h(order.getCustomer().getName()) + " 👋</p>"
                + "<p style='color:#667085;margin-top:5px'>Thanks for shopping with SAIVEE. Your order has been placed successfully.</p>"
                + "<div style='margin:22px 0;padding:18px;border:1px solid #e5e7eb;border-radius:12px;background:#f8fafc'>"
                + "<div style='font-size:12px;color:#667085;text-transform:uppercase;letter-spacing:.8px'>Order number</div>"
                + "<div style='font-size:24px;font-weight:700;margin-top:4px'>#" + order.getOrderId() + "</div>"
                + "<div style='margin-top:8px;color:#475467'>Status: <strong>Placed</strong></div></div>"
                + "<h3 style='margin:26px 0 10px;font-size:16px'>Order summary</h3>"
                + "<table style='width:100%;border-collapse:collapse;font-size:14px'><thead><tr>"
                + "<th style='padding:10px 8px;text-align:left;background:#f8fafc'>Item</th><th style='padding:10px 8px;background:#f8fafc'>Qty</th><th style='padding:10px 8px;text-align:right;background:#f8fafc'>Amount</th>"
                + "</tr></thead><tbody>" + rows + "</tbody></table>"
                + "<div style='margin-top:18px;margin-left:auto;max-width:320px;font-size:14px'>"
                + totalRow("Subtotal", order.getSubtotal(), false)
                + totalRow("Discount", order.getDiscount(), true)
                + totalRow("GST", order.getTax(), false)
                + totalRow("Delivery", order.getDeliveryCharge(), false)
                + "<div style='border-top:2px solid #111827;margin-top:8px;padding-top:12px;font-size:17px;font-weight:700'><span>Total</span><span style='float:right'>INR " + String.format("%.2f", order.getTotalAmount()) + "</span></div></div>"
                + "<div style='margin-top:28px;padding:16px;background:#f0fdf4;border:1px solid #bbf7d0;border-radius:10px;color:#166534'><strong>Invoice attached</strong><br><span style='font-size:13px'>Your SAIVEE invoice is included as a PDF attachment for your records.</span></div>"
                + "<p style='margin-top:24px;color:#667085'>We’ll notify you when your order moves to the next stage. No action is needed right now.</p>";
        return shell("Order confirmation", content);
    }

    private String totalRow(String label, double amount, boolean negative) {
        return "<div style='padding:5px 0'><span>" + label + "</span><span style='float:right'>" + (negative ? "-" : "") + "INR " + String.format("%.2f", amount) + "</span></div>";
    }

    private String shell(String title, String content) {
        return "<html><body style='margin:0;background:#f4f6f8;font-family:Arial,Helvetica,sans-serif;color:#101828'>"
                + "<div style='max-width:680px;margin:30px auto;background:#fff;border:1px solid #e4e7ec;border-radius:16px;overflow:hidden'>"
                + "<div style='padding:25px 30px;background:#111827;color:#fff'><div style='font-size:27px;font-weight:800;letter-spacing:.4px'>SAIVEE</div><div style='font-size:12px;opacity:.75;margin-top:5px'>E-Commerce Management System</div></div>"
                + "<div style='padding:28px 30px'><div style='font-size:13px;color:#667085;margin-bottom:12px'>" + h(title) + "</div>" + content + "</div>"
                + "<div style='padding:18px 30px;background:#f8fafc;border-top:1px solid #eaecf0;color:#667085;font-size:12px;line-height:1.6'>This is an automated email from SAIVEE. Please keep this message for your records.</div>"
                + "</div></body></html>";
    }

    private String friendlyTitle(NotificationType type) {
        if (type == null) return "SAIVEE notification";
        switch (type) {
            case PAYMENT_SUCCESS: return "Payment successful";
            case PAYMENT_FAILED: return "Payment update";
            case ORDER_CONFIRMED: return "Order confirmed";
            case ORDER_SHIPPED: return "Your order is on the way";
            case ORDER_DELIVERED: return "Order delivered";
            case ORDER_CANCELLED: return "Order cancelled";
            case RETURN_REQUESTED: return "Return request received";
            case REFUND_PROCESSED: return "Refund processed";
            case LOW_STOCK: return "Inventory alert";
            default: return "Order update";
        }
    }

    private static String h(String s) {
        return (s == null ? "" : s).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
