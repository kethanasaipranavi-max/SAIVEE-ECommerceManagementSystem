package com.ecommerce.service;

import com.ecommerce.model.*;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class InvoiceService {
    private final Path invoiceDirectory;

    public InvoiceService() {
        invoiceDirectory = Paths.get("invoices");
        try { Files.createDirectories(invoiceDirectory); } catch (IOException ignored) {}
    }

    public synchronized String generateInvoice(Order order) {
        if (order == null) return "";
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        String number = "INV-" + date + "-" + order.getOrderId();
        String dateTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss"));
        StringBuilder s = new StringBuilder();
        s.append("=============================================\nSAIVEE INVOICE\n=============================================\n")
         .append("Invoice No : ").append(number).append('\n')
         .append("Date       : ").append(dateTime).append('\n')
         .append("Order No   : #").append(order.getOrderId()).append('\n')
         .append("Customer   : ").append(order.getCustomer() == null ? "-" : order.getCustomer().getName()).append('\n')
         .append("Email      : ").append(order.getCustomer() == null ? "-" : order.getCustomer().getEmail()).append('\n')
         .append("---------------------------------------------\nPRODUCTS\n");
        for (OrderItem item : order.getOrderItems()) {
            s.append(String.format("%-28s x%-3d INR %.2f%n", item.getProduct().getProductName(), item.getQuantity(), item.calculateSubtotal()));
        }
        s.append("---------------------------------------------\n")
         .append(String.format("Subtotal        : INR %.2f%n", order.getSubtotal()))
         .append(String.format("Discount        : -INR %.2f%n", order.getDiscount()))
         .append(String.format("GST (18%%)       : INR %.2f%n", order.getTax()))
         .append(String.format("Delivery        : INR %.2f%n", order.getDeliveryCharge()))
         .append(String.format("FINAL TOTAL     : INR %.2f%n", order.getTotalAmount()))
         .append("=============================================\nThank you for shopping with SAIVEE.\n");
        try {
            Files.writeString(invoiceDirectory.resolve(number + ".txt"), s.toString(), StandardCharsets.UTF_8);
            writePdf(order, number, dateTime);
        } catch (IOException e) { System.out.println("Invoice file creation warning: " + e.getMessage()); }
        return s.toString();
    }

    public Path getInvoicePdfPath(Order order) {
        if (order == null) return null;
        String date = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Path p = invoiceDirectory.resolve("INV-" + date + "-" + order.getOrderId() + ".pdf");
        return Files.exists(p) ? p : null;
    }

    private void writePdf(Order order, String number, String dateTime) throws IOException {
        StringBuilder text = new StringBuilder();
        text.append("SAIVEE INVOICE\nInvoice No: ").append(number)
            .append("\nOrder No: #").append(order.getOrderId())
            .append("\nDate: ").append(dateTime)
            .append("\nCustomer: ").append(order.getCustomer() == null ? "-" : order.getCustomer().getName())
            .append("\nEmail: ").append(order.getCustomer() == null ? "-" : order.getCustomer().getEmail())
            .append("\n----------------------------------------\n");
        for (OrderItem item : order.getOrderItems()) {
            text.append(item.getProduct().getProductName()).append(" x").append(item.getQuantity())
                .append("  INR ").append(String.format("%.2f", item.calculateSubtotal())).append('\n');
        }
        text.append("----------------------------------------\n")
            .append(String.format("Subtotal: INR %.2f%n", order.getSubtotal()))
            .append(String.format("Discount: -INR %.2f%n", order.getDiscount()))
            .append(String.format("GST: INR %.2f%n", order.getTax()))
            .append(String.format("Delivery: INR %.2f%n", order.getDeliveryCharge()))
            .append(String.format("TOTAL: INR %.2f%n", order.getTotalAmount()))
            .append("\nThank you for shopping with SAIVEE.");

        StringBuilder stream = new StringBuilder("BT\n/F1 11 Tf\n50 760 Td\n");
        for (String line : text.toString().split("\\n", -1)) {
            stream.append("(").append(pdfEscape(line)).append(") Tj\n0 -16 Td\n");
        }
        stream.append("ET\n");
        byte[] sb = stream.toString().getBytes(StandardCharsets.ISO_8859_1);
        String[] obj = new String[5];
        obj[0] = "<< /Type /Catalog /Pages 2 0 R >>";
        obj[1] = "<< /Type /Pages /Kids [3 0 R] /Count 1 >>";
        obj[2] = "<< /Type /Page /Parent 2 0 R /MediaBox [0 0 595 842] /Resources << /Font << /F1 4 0 R >> >> /Contents 5 0 R >>";
        obj[3] = "<< /Type /Font /Subtype /Type1 /BaseFont /Helvetica >>";
        obj[4] = "<< /Length " + sb.length + " >>\nstream\n" + stream + "endstream";
        StringBuilder pdf = new StringBuilder("%PDF-1.4\n");
        long[] off = new long[6];
        for (int i=0;i<obj.length;i++) { off[i+1] = pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length; pdf.append(i+1).append(" 0 obj\n").append(obj[i]).append("\nendobj\n"); }
        long xref = pdf.toString().getBytes(StandardCharsets.ISO_8859_1).length;
        pdf.append("xref\n0 6\n0000000000 65535 f \n");
        for (int i=1;i<=5;i++) pdf.append(String.format("%010d 00000 n \n", off[i]));
        pdf.append("trailer\n<< /Size 6 /Root 1 0 R >>\nstartxref\n").append(xref).append("\n%%EOF\n");
        Files.write(invoiceDirectory.resolve(number + ".pdf"), pdf.toString().getBytes(StandardCharsets.ISO_8859_1));
    }
    private static String pdfEscape(String s) { return s.replace("\\", "\\\\").replace("(", "\\(").replace(")", "\\)"); }
}
