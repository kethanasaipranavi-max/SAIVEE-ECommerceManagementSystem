package com.ecommerce.service;

import com.ecommerce.model.Report;
import com.ecommerce.model.ReportType;

public class ReportGenerationThread extends Thread {

    private final AdminReportService adminReportService;
    private final int adminId;
    private final int reportId;
    private final ReportType type;
    private final String title;
    private final String description;

    public ReportGenerationThread(
            AdminReportService adminReportService,
            int adminId,
            int reportId,
            ReportType type,
            String title,
            String description) {

        this.adminReportService = adminReportService;
        this.adminId = adminId;
        this.reportId = reportId;
        this.type = type;
        this.title = title;
        this.description = description;
    }

    @Override
    public void run() {

        System.out.println(
                "Generating report in thread: "
                + Thread.currentThread().getName()
        );

        Report report =
                adminReportService.generateReport(
                        adminId,
                        reportId,
                        type,
                        title,
                        description
                );

        if (report != null) {
            System.out.println("Report generated successfully.");
        }
    }
}