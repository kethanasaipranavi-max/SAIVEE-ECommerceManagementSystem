package com.ecommerce.ui;

import com.ecommerce.model.CardPayment;
import com.ecommerce.model.CashOnDeliveryPayment;
import com.ecommerce.model.Customer;
import com.ecommerce.model.NotificationType;
import com.ecommerce.model.PaymentMethod;
import com.ecommerce.model.UpiPayment;
import com.ecommerce.model.NetBankingPayment;
import com.ecommerce.service.NotificationService;
import com.ecommerce.service.PaymentService;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class PaymentFrame extends JFrame {

    private final double amount;
    private final Runnable paymentSuccessAction;
    private final Runnable paymentCancelledAction;
    private final Customer customer;

    private final PaymentService paymentService;
    private final NotificationService notificationService;

    private final JTextField phoneField;
    private final JTextArea addressArea;
    private final JComboBox<String> paymentMethodBox;
    private final JTextField paymentDetailsField;
    private final JComboBox<String> outcomeBox;

    private boolean checkoutCompleted;
    private boolean reservationReleased;

    // =========================================================
    // CONSTRUCTOR
    // =========================================================

    public PaymentFrame(
            double amount,
            Customer customer,
            PaymentService paymentService,
            NotificationService notificationService,
            Runnable paymentSuccessAction,
            Runnable paymentCancelledAction) {

        this.amount = amount;
        this.customer = customer;

        this.paymentSuccessAction =
                paymentSuccessAction;

        this.paymentCancelledAction =
                paymentCancelledAction;

        this.paymentService =
                paymentService != null
                        ? paymentService
                        : new PaymentService();

        this.notificationService =
                notificationService;

        checkoutCompleted = false;
        reservationReleased = false;

        setTitle(
                "SAIVEE - Checkout & Payment"
        );

        setSize(
                550,
                550
        );

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        // =====================================================
        // TITLE
        // =====================================================

        JLabel titleLabel =
                new JLabel(
                        "SAIVEE Checkout & Payment",
                        SwingConstants.CENTER
                );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        // =====================================================
        // AMOUNT
        // =====================================================

        JLabel amountLabel =
                new JLabel(
                        String.format(
                                "Order Total: ₹%.2f",
                                amount
                        ),
                        SwingConstants.CENTER
                );

        amountLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        18
                )
        );

        // =====================================================
        // PHONE
        // =====================================================

        phoneField =
                new JTextField();

        if (customer != null
                && customer.getPhone() != null) {

            phoneField.setText(
                    customer.getPhone()
            );
        }

        // =====================================================
        // ADDRESS
        // =====================================================

        addressArea =
                new JTextArea(
                        4,
                        20
                );

        addressArea.setLineWrap(true);
        addressArea.setWrapStyleWord(true);

        JScrollPane addressScrollPane =
                new JScrollPane(
                        addressArea
                );

        // =====================================================
        // PAYMENT METHOD
        // =====================================================

        paymentMethodBox =
                new JComboBox<>(
                        new String[]{
                                "Card",
                                "UPI",
                                "Net Banking",
                                "Cash on Delivery"
                        }
                );

        // =====================================================
        // PAYMENT DETAILS
        // =====================================================

        paymentDetailsField =
                new JTextField();

        outcomeBox = new JComboBox<>(new String[]{"SUCCESS", "FAILED"});

        // =====================================================
        // BUTTONS
        // =====================================================

        JButton payButton =
                new JButton(
                        "Pay & Place Order"
                );

        JButton cancelButton =
                new JButton(
                        "Cancel"
                );

        // =====================================================
        // FORM
        // =====================================================

        JPanel formPanel =
                new JPanel(
                        new GridBagLayout()
                );

        formPanel.setBorder(
                BorderFactory.createEmptyBorder(
                        15,
                        30,
                        15,
                        30
                )
        );

        GridBagConstraints gbc =
                new GridBagConstraints();

        gbc.insets =
                new Insets(
                        7,
                        7,
                        7,
                        7
                );

        gbc.fill =
                GridBagConstraints.HORIZONTAL;

        gbc.weightx = 1.0;

        // Phone
        gbc.gridx = 0;
        gbc.gridy = 0;

        formPanel.add(
                new JLabel(
                        "Phone Number:"
                ),
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                phoneField,
                gbc
        );

        // Address
        gbc.gridx = 0;
        gbc.gridy = 1;

        formPanel.add(
                new JLabel(
                        "Delivery Address:"
                ),
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                addressScrollPane,
                gbc
        );

        // Payment method
        gbc.gridx = 0;
        gbc.gridy = 2;

        formPanel.add(
                new JLabel(
                        "Payment Method:"
                ),
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                paymentMethodBox,
                gbc
        );

        // Payment details
        gbc.gridx = 0;
        gbc.gridy = 3;

        formPanel.add(
                new JLabel(
                        "Payment Details:"
                ),
                gbc
        );

        gbc.gridx = 1;

        formPanel.add(
                paymentDetailsField,
                gbc
        );

        gbc.gridx = 0;
        gbc.gridy = 5;
        formPanel.add(new JLabel("Simulation Outcome:"), gbc);
        gbc.gridx = 1;
        formPanel.add(outcomeBox, gbc);

        // =====================================================
        // BUTTON PANEL
        // =====================================================

        JPanel buttonPanel =
                new JPanel();

        buttonPanel.add(
                payButton
        );

        buttonPanel.add(
                cancelButton
        );

        // =====================================================
        // CENTER
        // =====================================================

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.add(
                amountLabel,
                BorderLayout.NORTH
        );

        centerPanel.add(
                formPanel,
                BorderLayout.CENTER
        );

        // =====================================================
        // MAIN LAYOUT
        // =====================================================

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        add(
                titleLabel,
                BorderLayout.NORTH
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );

        add(
                buttonPanel,
                BorderLayout.SOUTH
        );

        // =====================================================
        // EVENTS
        // =====================================================

        paymentMethodBox.addActionListener(
                e -> updatePaymentDetails()
        );

        payButton.addActionListener(
                e -> processPayment()
        );

        cancelButton.addActionListener(
                e -> cancelCheckout()
        );

        addWindowListener(
                new WindowAdapter() {

                    @Override
                    public void windowClosing(
                            WindowEvent e) {

                        cancelCheckout();
                    }
                }
        );

        updatePaymentDetails();
    }

    // =========================================================
    // PAYMENT DETAILS
    // =========================================================

    private void updatePaymentDetails() {

        String method =
                (String) paymentMethodBox
                        .getSelectedItem();

        if ("Card".equals(method)) {

            paymentDetailsField.setText("");

            paymentDetailsField.setEnabled(
                    true
            );

            paymentDetailsField.setToolTipText(
                    "Enter your 16-digit card number"
            );

        } else if ("UPI".equals(method)) {

            paymentDetailsField.setText("");

            paymentDetailsField.setEnabled(
                    true
            );

            paymentDetailsField.setToolTipText(
                    "Enter your UPI ID"
            );

        } else if ("Net Banking".equals(method)) {
            paymentDetailsField.setText("");
            paymentDetailsField.setEnabled(true);
            paymentDetailsField.setToolTipText("Enter bank name");
        } else {

            paymentDetailsField.setText(
                    "No payment details required"
            );

            paymentDetailsField.setEnabled(
                    false
            );

            paymentDetailsField.setToolTipText(
                    null
            );
        }
    }

    // =========================================================
    // PROCESS PAYMENT
    // =========================================================

    private void processPayment() {

        String phone =
                phoneField
                        .getText()
                        .trim();

        String address =
                addressArea
                        .getText()
                        .trim();

        String method =
                (String) paymentMethodBox
                        .getSelectedItem();

        // =====================================================
        // VALIDATION
        //
        // IMPORTANT:
        // Validation errors DO NOT release reservation.
        // =====================================================

        if (amount <= 0) {

            showError(
                    "Order amount must be greater than zero."
            );

            return;
        }

        if (phone.isEmpty()) {

            showError(
                    "Please enter your phone number."
            );

            return;
        }

        if (!phone.matches("\\d{10}")) {

            showError(
                    "Phone number must contain exactly 10 digits."
            );

            return;
        }

        if (address.isEmpty()) {

            showError(
                    "Please enter your delivery address."
            );

            return;
        }

        if (customer == null) {

            showError(
                    "Customer session is invalid."
            );

            return;
        }

        // =====================================================
        // CREATE PAYMENT METHOD
        // =====================================================

        PaymentMethod paymentMethod;

        String paymentId =
                "PAY-" + System.currentTimeMillis();

        if ("Card".equals(method)) {

            String cardNumber =
                    paymentDetailsField
                            .getText()
                            .trim();

            if (!cardNumber.matches("\\d{16}")) {

                showError(
                        "Card number must contain exactly 16 digits."
                );

                return;
            }

            paymentMethod =
                    new CardPayment(
                            paymentId,
                            amount,
                            cardNumber,
                            customer.getName()
                    );

        } else if ("UPI".equals(method)) {

            String upiId =
                    paymentDetailsField
                            .getText()
                            .trim();

            if (upiId.isEmpty()) {

                showError(
                        "Please enter your UPI ID."
                );

                return;
            }

            paymentMethod =
                    new UpiPayment(
                            paymentId,
                            amount,
                            upiId
                    );

        } else if ("Net Banking".equals(method)) {
            String bank = paymentDetailsField.getText().trim();
            if (bank.isEmpty()) { showError("Please enter your bank name."); return; }
            paymentMethod = new NetBankingPayment(paymentId, amount, bank, customer.getName());
        } else {

            paymentMethod =
                    new CashOnDeliveryPayment(
                            paymentId,
                            amount,
                            address
                    );
        }

        if ("FAILED".equals(outcomeBox.getSelectedItem())) {
            paymentMethod.markFailed();
        }

        // =====================================================
        // PROCESS PAYMENT
        // =====================================================

        boolean success =
                paymentService.processPayment(
                        paymentMethod
                );

        if (!success) {
            // A declined/cancelled payment is not an order: do not send email or
            // success-style confirmation. Release reserved stock and close checkout.
            releaseReservation();
            dispose();
            return;
        }

        // Persist the simulated gateway transaction before checkout closes.
        com.ecommerce.service.PaymentHistoryService.getInstance().recordPending(customer, paymentMethod);

        // =====================================================
        // PAYMENT SUCCESS NOTIFICATION
        // =====================================================

        String notificationMessage;

        if ("Cash on Delivery".equals(method)) {

            notificationMessage =
                    "Your SAIVEE Cash on Delivery request "
                            + "has been accepted.\n"
                            + "Amount to collect: ₹"
                            + String.format(
                                    "%.2f",
                                    amount
                            );

        } else {

            notificationMessage =
                    "Your SAIVEE payment of ₹"
                            + String.format(
                                    "%.2f",
                                    amount
                            )
                            + " was successful.\n"
                            + "Payment ID: "
                            + paymentId;
        }

        sendPaymentNotification(
                NotificationType.PAYMENT_SUCCESS,
                notificationMessage
        );

        // =====================================================
        // RECEIPT
        // =====================================================

        String receipt =
                paymentMethod.generateReceipt();

        JOptionPane.showMessageDialog(
                this,
                "Payment successful!\n\n"
                        + "Payment ID: "
                        + paymentId
                        + "\nAmount: ₹"
                        + String.format(
                                "%.2f",
                                amount
                        )
                        + "\nPayment Method: "
                        + method
                        + "\nPhone: "
                        + phone
                        + "\nDelivery Address:\n"
                        + address,
                "SAIVEE - Payment Successful",
                JOptionPane.INFORMATION_MESSAGE
        );

        showReceipt(
                receipt
        );

        // =====================================================
        // COMPLETE CHECKOUT
        // =====================================================

        checkoutCompleted = true;

        if (paymentSuccessAction != null) {

            paymentSuccessAction.run();
        }

        dispose();
    }

    // =========================================================
    // CANCEL CHECKOUT
    // =========================================================

    private void cancelCheckout() {

        if (checkoutCompleted) {
            return;
        }

        releaseReservation();

        dispose();
    }

    // =========================================================
    // RELEASE RESERVATION
    // =========================================================

    private void releaseReservation() {

        if (reservationReleased) {
            return;
        }

        reservationReleased = true;

        if (paymentCancelledAction != null) {

            paymentCancelledAction.run();
        }
    }

    // =========================================================
    // PAYMENT NOTIFICATION
    // =========================================================

    private void sendPaymentNotification(
            NotificationType type,
            String message) {

        if (notificationService == null
                || customer == null) {

            return;
        }

        int notificationId =
                (int) (
                        System.nanoTime()
                                & 0x7fffffff
                );

        notificationService.sendNotification(
                notificationId,
                customer,
                type,
                message
        );
    }

    // =========================================================
    // RECEIPT
    // =========================================================

    private void showReceipt(
            String receipt) {

        JTextArea receiptArea =
                new JTextArea(
                        receipt
                );

        receiptArea.setEditable(
                false
        );

        receiptArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        13
                )
        );

        receiptArea.setMargin(
                new Insets(
                        10,
                        10,
                        10,
                        10
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        receiptArea
                );

        scrollPane.setPreferredSize(
                new Dimension(
                        500,
                        350
                )
        );

        JOptionPane.showMessageDialog(
                this,
                scrollPane,
                "SAIVEE - Payment Receipt",
                JOptionPane.INFORMATION_MESSAGE
        );
    }

    // =========================================================
    // ERROR
    // =========================================================

    private void showError(
            String message) {

        JOptionPane.showMessageDialog(
                this,
                message,
                "SAIVEE - Invalid Information",
                JOptionPane.ERROR_MESSAGE
        );
    }
}