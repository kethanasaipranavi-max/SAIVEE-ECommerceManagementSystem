package com.ecommerce.service;

import com.ecommerce.model.Admin;
import com.ecommerce.model.Report;
import com.ecommerce.model.ReportType;

public class AdminReportService {

    private final UserService userService;
    private final ReportService reportService;

    public AdminReportService(UserService userService,
                              ReportService reportService) {

        this.userService = userService;
        this.reportService = reportService;
    }

    public Report generateReport(int adminId,
                                 int reportId,
                                 ReportType type,
                                 String title,
                                 String description) {

        Admin admin = userService.findAdminById(adminId);

        if (admin == null) {

            System.out.println(
                    "Access denied. Only admins can generate reports."
            );

            return null;
        }

        if (type == null) {

            System.out.println("Report type cannot be null.");
            return null;
        }

        if (title == null || title.trim().isEmpty()) {

            System.out.println("Report title cannot be empty.");
            return null;
        }

        return reportService.generateReport(
                reportId,
                type,
                title,
                description
        );
    }

    public void displayAllReports(int adminId) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can view reports."
            );

            return;
        }

        reportService.displayAllReports();
    }

    public void displayReportsByType(int adminId,
                                     ReportType type) {

        if (!isAdmin(adminId)) {

            System.out.println(
                    "Access denied. Only admins can view reports."
            );

            return;
        }

        if (type == null) {

            System.out.println("Report type cannot be null.");
            return;
        }

        reportService.displayReportsByType(type);
    }

    private boolean isAdmin(int adminId) {

        Admin admin = userService.findAdminById(adminId);

        return admin != null;
    }
}