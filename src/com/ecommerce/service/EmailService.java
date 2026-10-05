package com.ecommerce.service;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.UUID;

public class EmailService {
    private final String smtpHost, smtpUsername, smtpPassword, fromEmail;
    private final int smtpPort;
    private final Path statusFile = Paths.get("email-status.csv");

    public EmailService() {
        smtpHost = env("SAIVEE_SMTP_HOST", "smtp.gmail.com");
        smtpPort = Integer.parseInt(env("SAIVEE_SMTP_PORT", "465"));
        smtpUsername = env("SAIVEE_SMTP_USERNAME", "");
        smtpPassword = env("SAIVEE_SMTP_PASSWORD", "");
        fromEmail = env("SAIVEE_FROM_EMAIL", smtpUsername);
        try {
            if (!Files.exists(statusFile)) Files.writeString(statusFile, "timestamp,id,recipient,subject,status,details\n");
        } catch (Exception ignored) {}
    }

    public boolean sendEmail(String recipient, String subject, String body) {
        return sendEmail(recipient, subject, body, null, null);
    }

    public boolean sendEmail(String recipient, String subject, String body, Path attachment) {
        return sendEmail(recipient, subject, body, null, attachment);
    }

    public boolean sendEmail(String recipient, String subject, String textBody, String htmlBody) {
        return sendEmail(recipient, subject, textBody, htmlBody, null);
    }

    public boolean sendEmail(String recipient, String subject, String textBody, String htmlBody, Path attachment) {
        String id = UUID.randomUUID().toString().substring(0, 8);
        recipient = recipient == null ? "" : recipient.trim();
        if (!validEmail(recipient)) {
            status(id, recipient, subject, "Failed", "Invalid email address");
            System.out.println("Email not sent: invalid recipient email address.");
            return false;
        }
        status(id, recipient, subject, "Pending", "Queued for delivery");
        if (htmlBody == null || htmlBody.isBlank()) htmlBody = genericHtml(textBody);

        if (smtpUsername.isBlank() || smtpPassword.isBlank() || fromEmail.isBlank()) {
            queueLocal(recipient, subject, textBody, htmlBody, attachment);
            status(id, recipient, subject, "Pending", "SMTP not configured; saved to local outbox");
            return true;
        }

        Exception last = null;
        for (int attempt = 1; attempt <= 2; attempt++) {
            try {
                smtp(recipient, subject, textBody, htmlBody, attachment);
                status(id, recipient, subject, "Sent", "Accepted by SMTP server");
                System.out.println("Professional email sent successfully to: " + recipient);
                return true;
            } catch (Exception e) {
                last = e;
                if (attempt == 1 && retryable(e)) {
                    try { Thread.sleep(700); } catch (InterruptedException x) { Thread.currentThread().interrupt(); break; }
                } else break;
            }
        }
        String reason = last == null ? "Unknown email error" : last.getMessage();
        status(id, recipient, subject, "Failed", reason);
        System.out.println("Email sending failed: " + reason);
        System.out.println("In-app notification was still created.");
        return false;
    }

    private void smtp(String recipient, String subject, String text, String html, Path attachment) throws Exception {
        SSLSocketFactory f = (SSLSocketFactory) SSLSocketFactory.getDefault();
        try (SSLSocket socket = (SSLSocket) f.createSocket(smtpHost, smtpPort);
             BufferedReader r = new BufferedReader(new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8));
             BufferedWriter w = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream(), StandardCharsets.UTF_8))) {
            expect(r, 220);
            cmd(w, r, "EHLO saivee.local", 250);
            cmd(w, r, "AUTH LOGIN", 334);
            cmd(w, r, b64(smtpUsername), 334);
            cmd(w, r, b64(smtpPassword), 235);
            cmd(w, r, "MAIL FROM:<" + fromEmail + ">", 250);
            cmd(w, r, "RCPT TO:<" + recipient + ">", 250);
            cmd(w, r, "DATA", 354);
            w.write(mime(recipient, subject, text, html, attachment));
            w.write("\r\n.\r\n"); w.flush();
            String response = read(r);
            if (!response.startsWith("250")) throw new Exception("Email delivery rejected: " + response);
            cmd(w, r, "QUIT", 221);
        }
    }

    private String mime(String recipient, String subject, String text, String html, Path attachment) throws Exception {
        String mixed = "SAIVEE-MIXED-" + UUID.randomUUID();
        String alt = "SAIVEE-ALT-" + UUID.randomUUID();
        StringBuilder s = new StringBuilder();
        s.append("From: SAIVEE <").append(fromEmail).append(">\r\n")
         .append("To: ").append(recipient).append("\r\n")
         .append("Subject: ").append(header(subject)).append("\r\n")
         .append("Date: ").append(java.time.ZonedDateTime.now()).append("\r\n")
         .append("MIME-Version: 1.0\r\n")
         .append("Content-Type: multipart/mixed; boundary=\"").append(mixed).append("\"\r\n\r\n")
         .append("--").append(mixed).append("\r\n")
         .append("Content-Type: multipart/alternative; boundary=\"").append(alt).append("\"\r\n\r\n")
         .append("--").append(alt).append("\r\nContent-Type: text/plain; charset=UTF-8\r\nContent-Transfer-Encoding: 8bit\r\n\r\n")
         .append(text == null ? "" : text).append("\r\n\r\n")
         .append("--").append(alt).append("\r\nContent-Type: text/html; charset=UTF-8\r\nContent-Transfer-Encoding: 8bit\r\n\r\n")
         .append(html).append("\r\n\r\n--").append(alt).append("--\r\n");
        if (attachment != null && Files.exists(attachment)) {
            String n = attachment.getFileName().toString();
            s.append("--").append(mixed).append("\r\nContent-Type: application/pdf; name=\"").append(n).append("\"\r\n")
             .append("Content-Transfer-Encoding: base64\r\nContent-Disposition: attachment; filename=\"").append(n).append("\"\r\n\r\n")
             .append(Base64.getMimeEncoder(76, "\r\n".getBytes(StandardCharsets.US_ASCII)).encodeToString(Files.readAllBytes(attachment))).append("\r\n");
        }
        return s.append("--").append(mixed).append("--\r\n").toString();
    }

    private void queueLocal(String recipient, String subject, String text, String html, Path attachment) {
        try {
            Path out = Paths.get("email-outbox"); Files.createDirectories(out);
            String safe = recipient.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path file = out.resolve(System.currentTimeMillis() + "-" + safe + ".eml");
            Files.writeString(file, mime(recipient, subject, text, html, attachment), StandardCharsets.UTF_8);
            System.out.println("Email queued to local outbox: " + file.toAbsolutePath());
        } catch (Exception e) { System.out.println("Local email queue failed: " + e.getMessage()); }
    }

    private String genericHtml(String body) {
        return "<html><body style='margin:0;background:#f4f6f8;font-family:Arial,sans-serif;color:#101828'><div style='max-width:680px;margin:30px auto;background:#fff;border:1px solid #e4e7ec;border-radius:16px;overflow:hidden'>"
                + "<div style='padding:25px 30px;background:#111827;color:#fff'><div style='font-size:27px;font-weight:800'>SAIVEE</div><div style='font-size:12px;opacity:.75'>E-Commerce Management System</div></div>"
                + "<div style='padding:30px;font-size:15px;line-height:1.7'><p>" + html(body).replace("\n", "<br>") + "</p><p style='color:#667085'>Thank you for choosing SAIVEE.</p></div>"
                + "<div style='padding:18px 30px;background:#f8fafc;color:#667085;font-size:12px'>Automated message from SAIVEE.</div></div></body></html>";
    }

    private synchronized void status(String id, String to, String subject, String state, String details) {
        try {
            Files.writeString(statusFile, String.format("%s,%s,%s,%s,%s,%s%n", csv(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME)), csv(id), csv(to), csv(subject), csv(state), csv(details)), StandardCharsets.UTF_8, StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        } catch (Exception ignored) {}
    }
    private boolean retryable(Exception e) { String m = e.getMessage() == null ? "" : e.getMessage(); return m.startsWith("4") || m.contains("timed out") || m.contains("Connection") || m.contains("closed"); }
    private void cmd(BufferedWriter w, BufferedReader r, String c, int code) throws Exception { w.write(c); w.write("\r\n"); w.flush(); String x = read(r); if (!x.startsWith(String.valueOf(code))) throw new Exception("SMTP error. Expected " + code + " but received: " + x); }
    private void expect(BufferedReader r, int code) throws Exception { String x = read(r); if (!x.startsWith(String.valueOf(code))) throw new Exception("SMTP error. Expected " + code + " but received: " + x); }
    private String read(BufferedReader r) throws Exception { StringBuilder s = new StringBuilder(); String line; while ((line = r.readLine()) != null) { if (s.length() > 0) s.append('\n'); s.append(line); if (line.length() < 4 || line.charAt(3) != '-') break; } if (s.length() == 0) throw new Exception("SMTP server closed the connection."); return s.toString(); }
    private static String b64(String s) { return Base64.getEncoder().encodeToString(s.getBytes(StandardCharsets.UTF_8)); }
    private static String header(String s) { return "=?UTF-8?B?" + b64(s == null ? "" : s) + "?="; }
    private static String html(String s) { return (s == null ? "" : s).replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"); }
    private static String csv(String s) { return "\"" + (s == null ? "" : s).replace("\"", "\"\"") + "\""; }
    private static String env(String n, String d) { String v = System.getenv(n); return v == null || v.isBlank() ? d : v; }
    private static boolean validEmail(String e) { return e.matches("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$"); }
}
