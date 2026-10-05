package com.ecommerce.service;

import com.ecommerce.model.Report;
import com.ecommerce.model.ReportType;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ReportService {

    private final List<Report> reports;

    public ReportService() {
        reports = new ArrayList<>();
    }

    // =========================================================
    // GENERATE REPORT
    // =========================================================

    public synchronized Report generateReport(
            int reportId,
            ReportType type,
            String title,
            String description) {

        if (type == null) {

            System.out.println(
                    "Report type cannot be null."
            );

            return null;
        }

        if (title == null
                || title.isBlank()) {

            System.out.println(
                    "Report title is required."
            );

            return null;
        }

        if (findReportById(reportId) != null) {

            System.out.println(
                    "Report ID already exists."
            );

            return null;
        }

        Report report =
                new Report(
                        reportId,
                        type,
                        title,
                        description
                );

        reports.add(report);

        System.out.println(
                "Report generated successfully."
        );

        return report;
    }

    // =========================================================
    // FIND REPORT
    // =========================================================

    public synchronized Report findReportById(
            int reportId) {

        for (Report report :
                reports) {

            if (report != null
                    && report.getReportId()
                    == reportId) {

                return report;
            }
        }

        return null;
    }

    // =========================================================
    // DISPLAY REPORTS BY TYPE
    // =========================================================

    public synchronized void displayReportsByType(
            ReportType type) {

        if (type == null) {

            System.out.println(
                    "Report type cannot be null."
            );

            return;
        }

        boolean found = false;

        System.out.println(
                "===== SAIVEE REPORTS: "
                        + type
                        + " ====="
        );

        for (Report report :
                reports) {

            if (report != null
                    && report.getType() == type) {

                report.displayReport();
                found = true;
            }
        }

        if (!found) {

            System.out.println(
                    "No reports found for type: "
                            + type
            );
        }
    }

    // =========================================================
    // DISPLAY ALL REPORTS
    // =========================================================

    public synchronized void displayAllReports() {

        if (reports.isEmpty()) {

            System.out.println(
                    "No reports available."
            );

            return;
        }

        System.out.println(
                "===== SAIVEE REPORTS ====="
        );

        for (Report report :
                reports) {

            if (report == null) {
                continue;
            }

            System.out.println(
                    "----------------------------"
            );

            report.displayReport();
        }

        System.out.println(
                "----------------------------"
        );
    }

    // =========================================================
    // REPORT COUNT
    // =========================================================

    public synchronized int getReportCount() {

        return reports.size();
    }

    // =========================================================
    // CHECK REPORT
    // =========================================================

    public synchronized boolean reportExists(
            int reportId) {

        return findReportById(
                reportId
        ) != null;
    }

    // =========================================================
    // GET ALL REPORTS
    //
    // Returns a read-only snapshot.
    // =========================================================

    public synchronized List<Report>
    getReports() {

        return Collections.unmodifiableList(
                new ArrayList<>(
                        reports
                )
        );
    }

    // =========================================================
    // GET REPORTS BY TYPE
    // =========================================================

    public synchronized List<Report>
    getReportsByType(
            ReportType type) {

        List<Report> result =
                new ArrayList<>();

        if (type == null) {
            return Collections.emptyList();
        }

        for (Report report :
                reports) {

            if (report != null
                    && report.getType() == type) {

                result.add(report);
            }
        }

        return Collections.unmodifiableList(
                result
        );
    }
}