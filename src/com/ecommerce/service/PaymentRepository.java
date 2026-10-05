package com.ecommerce.service;

import com.ecommerce.model.PaymentMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PaymentRepository {

    private final Map<String, PaymentMethod> payments;

    public PaymentRepository() {
        payments = new HashMap<>();
    }

    public boolean save(PaymentMethod payment) {

        if (payment == null
                || payment.getPaymentId() == null) {

            return false;
        }

        payments.put(
                payment.getPaymentId(),
                payment
        );

        return true;
    }

    public PaymentMethod findById(String paymentId) {

        return payments.get(paymentId);
    }

    public boolean exists(String paymentId) {

        return payments.containsKey(paymentId);
    }

    public PaymentMethod removeById(String paymentId) {

        return payments.remove(paymentId);
    }

    public List<PaymentMethod> findAll() {

        return new ArrayList<>(payments.values());
    }

    public int count() {

        return payments.size();
    }

    public void clear() {

        payments.clear();
    }
}