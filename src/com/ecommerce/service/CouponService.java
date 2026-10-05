package com.ecommerce.service;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;

public class CouponService {
    public record Coupon(String code, double percent, double flatAmount,
                         double minimumOrder, double maximumDiscount,
                         LocalDate expiryDate) {
        public boolean isExpired() {
            return expiryDate != null && LocalDate.now().isAfter(expiryDate);
        }
    }

    private final Map<String,Coupon> coupons = new LinkedHashMap<>();

    public CouponService() {
        addCoupon(new Coupon("SAVE10",10,0,500,1000,LocalDate.now().plusDays(30)));
        addCoupon(new Coupon("SAVE20",20,0,1500,1500,LocalDate.now().plusDays(30)));
        addCoupon(new Coupon("FLAT500",0,500,2500,500,LocalDate.now().plusDays(30)));
        addCoupon(new Coupon("WELCOME100",0,100,999,100,LocalDate.now().plusDays(30)));
    }

    public void addCoupon(Coupon coupon) {
        if (coupon != null && coupon.code() != null)
            coupons.put(coupon.code().toUpperCase(), coupon);
    }

    public Coupon find(String code) {
        return code == null ? null : coupons.get(code.trim().toUpperCase());
    }

    public double calculateDiscount(String code, double subtotal) {
        Coupon c = find(code);
        if (c == null || subtotal < c.minimumOrder() || c.isExpired()) return 0;
        double d = subtotal * c.percent() / 100.0 + c.flatAmount();
        if (c.maximumDiscount() > 0) d = Math.min(d, c.maximumDiscount());
        return Math.min(d, subtotal);
    }

    public String validate(String code, double subtotal) {
        Coupon c = find(code);
        if (c == null) return "Invalid coupon code.";
        if (c.isExpired()) return "Coupon has expired.";
        if (subtotal < c.minimumOrder())
            return String.format("Minimum order value is ₹%.2f.", c.minimumOrder());
        return "VALID";
    }
}
