package com.ecommerce.ui;

import com.ecommerce.service.AdminReportService;

import javax.swing.*;
import java.awt.*;

public class AdminReportFrame extends JFrame {

    private final AdminReportService adminReportService;

    private final JTextArea reportArea;
    private final JButton generateButton;
    private final JButton closeButton;

    public AdminReportFrame(AdminReportService adminReportService) {

        this.adminReportService = adminReportService;

        setTitle("Admin Reports");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);

        reportArea = new JTextArea();
        reportArea.setEditable(false);

        generateButton = new JButton("Generate Report");
        closeButton = new JButton("Close");

        JPanel buttonPanel = new JPanel();

        buttonPanel.add(generateButton);
        buttonPanel.add(closeButton);

        setLayout(new BorderLayout());

        add(new JScrollPane(reportArea), BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        generateButton.addActionListener(
                e -> generateReport()
        );

        closeButton.addActionListener(
                e -> dispose()
        );
    }

    private void generateReport() {

        if (adminReportService == null) {

            reportArea.setText(
                    "Admin report service is not available."
            );

            return;
        }

        reportArea.setText(
                "Report generation module is ready.\n\n"
                + "Reports can be generated using AdminReportService."
        );
    }
}