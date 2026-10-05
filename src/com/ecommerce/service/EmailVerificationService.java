package com.ecommerce.service;

import java.security.SecureRandom;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EmailVerificationService {
    private static final EmailVerificationService INSTANCE = new EmailVerificationService();
    private final SecureRandom random = new SecureRandom();
    private final Map<String, String> otps = new ConcurrentHashMap<>();
    private final Map<String, Long> expiry = new ConcurrentHashMap<>();
    private final EmailService emailService = new EmailService();

    public static EmailVerificationService getInstance() { return INSTANCE; }

    public boolean sendOtp(String email, String name) {
        String normalized = email == null ? "" : email.trim().toLowerCase();
        if (!normalized.matches("^[A-Za-z0-9.!#$%&'*+/=?^_`{|}~-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")) return false;
        String otp = String.format("%06d", random.nextInt(1_000_000));
        otps.put(normalized, otp);
        expiry.put(normalized, System.currentTimeMillis() + 5 * 60_000L);
        String subject = "SAIVEE email verification code";
        String text = "Hi " + (name == null ? "Customer" : name) + ",\n\nYour SAIVEE verification code is: " + otp + "\n\nThis code expires in 5 minutes. Never share it with anyone.";
        String html = "<html><body style='font-family:Arial;background:#f4f6f8;padding:30px'><div style='max-width:620px;margin:auto;background:#fff;border-radius:14px;overflow:hidden'><div style='background:#111827;color:#fff;padding:26px'><h1 style='margin:0'>SAIVEE</h1><p>Email verification</p></div><div style='padding:30px'><p>Hi " + escape(name) + " 👋</p><p>Use this verification code to finish creating your SAIVEE account:</p><div style='font-size:34px;font-weight:800;letter-spacing:8px;text-align:center;padding:20px;background:#f3f4f6;border-radius:12px'>" + otp + "</div><p style='color:#667085'>The code expires in 5 minutes. Never share your OTP, password or banking PIN.</p></div></div></body></html>";
        return emailService.sendEmail(normalized, subject, text, html, null);
    }

    public boolean verify(String email, String otp) {
        String normalized = email == null ? "" : email.trim().toLowerCase();
        String expected = otps.get(normalized);
        Long until = expiry.get(normalized);
        boolean ok = expected != null && until != null && System.currentTimeMillis() <= until && expected.equals(otp == null ? "" : otp.trim());
        if (ok) { otps.remove(normalized); expiry.remove(normalized); }
        return ok;
    }

    private static String escape(String s) { return s == null ? "Customer" : s.replace("&","&amp;").replace("<","&lt;").replace(">","&gt;").replace("\"","&quot;"); }
}
