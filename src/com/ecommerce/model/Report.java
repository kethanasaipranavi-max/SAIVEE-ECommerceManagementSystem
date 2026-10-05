package com.ecommerce.model;

import java.time.LocalDateTime;

public class Report {

    private int reportId;
    private ReportType type;
    private String title;
    private String description;
    private LocalDateTime generatedAt;

    public Report() {
        generatedAt = LocalDateTime.now();
    }

    public Report(int reportId,
                  ReportType type,
                  String title,
                  String description) {

        this.reportId = reportId;
        this.type = type;
        this.title = title;
        this.description = description;
        this.generatedAt = LocalDateTime.now();
    }

    public int getReportId() {
        return reportId;
    }

    public ReportType getType() {
        return type;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void displayReport() {

        System.out.println("----------------------------");
        System.out.println("Report ID: " + reportId);
        System.out.println("Report Type: " + type);
        System.out.println("Title: " + title);
        System.out.println("Description: " + description);
        System.out.println("Generated At: " + generatedAt);
    }

    @Override
    public String toString() {

        return "Report ID: " + reportId +
                "\nReport Type: " + type +
                "\nTitle: " + title +
                "\nDescription: " + description +
                "\nGenerated At: " + generatedAt;
    }
}