package com.ecommerce.model;

public class NetBankingPayment extends PaymentMethod {
    private final String bankName;
    private final String accountHolder;

    public NetBankingPayment(String paymentId, double amount, String bankName, String accountHolder) {
        super(paymentId, amount);
        this.bankName = bankName;
        this.accountHolder = accountHolder;
    }
    public String getBankName() { return bankName; }
    public String getAccountHolder() { return accountHolder; }
    @Override public boolean processPayment() { setSuccessful(true); return true; }
    @Override protected String getPaymentMethodName() { return "Net Banking"; }
}
