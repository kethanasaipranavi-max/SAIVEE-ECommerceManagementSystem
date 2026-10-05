package com.ecommerce.ui;

import com.ecommerce.model.Report;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.List;

public class ReportFrame extends JFrame {

    private JTextArea reportArea;
    private JButton closeButton;

    private static final Color NAVY =
            new Color(13, 45, 82);

    private static final Color LIGHT_BLUE =
            new Color(220, 240, 255);

    private static final Color PALE_BLUE =
            new Color(240, 248, 255);

    public ReportFrame(List<Report> reports) {

        setTitle("SAIVEE - Reports");
        setSize(750, 550);

        setDefaultCloseOperation(
                JFrame.DISPOSE_ON_CLOSE
        );

        setLocationRelativeTo(null);

        setLayout(
                new BorderLayout(
                        10,
                        10
                )
        );

        createHeader();
        createReportArea();
        createFooter();

        displayReports(reports);
    }

    // ==================================================
    // HEADER
    // ==================================================

    private void createHeader() {

        JPanel headerPanel =
                new JPanel(
                        new BorderLayout()
                );

        headerPanel.setBackground(
                NAVY
        );

        headerPanel.setBorder(
                new EmptyBorder(
                        18,
                        25,
                        18,
                        25
                )
        );

        JLabel titleLabel =
                new JLabel(
                        "SAIVEE REPORTS"
                );

        titleLabel.setForeground(
                Color.WHITE
        );

        titleLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        24
                )
        );

        JLabel subtitleLabel =
                new JLabel(
                        "Business and system reporting"
                );

        subtitleLabel.setForeground(
                LIGHT_BLUE
        );

        subtitleLabel.setFont(
                new Font(
                        "Arial",
                        Font.PLAIN,
                        13
                )
        );

        JPanel titlePanel =
                new JPanel(
                        new GridLayout(
                                2,
                                1
                        )
                );

        titlePanel.setOpaque(false);

        titlePanel.add(titleLabel);
        titlePanel.add(subtitleLabel);

        headerPanel.add(
                titlePanel,
                BorderLayout.WEST
        );

        add(
                headerPanel,
                BorderLayout.NORTH
        );
    }

    // ==================================================
    // REPORT AREA
    // ==================================================

    private void createReportArea() {

        reportArea =
                new JTextArea();

        reportArea.setEditable(false);

        reportArea.setLineWrap(true);

        reportArea.setWrapStyleWord(true);

        reportArea.setFont(
                new Font(
                        "Monospaced",
                        Font.PLAIN,
                        14
                )
        );

        reportArea.setForeground(
                NAVY
        );

        reportArea.setBackground(
                PALE_BLUE
        );

        reportArea.setBorder(
                new EmptyBorder(
                        15,
                        15,
                        15,
                        15
                )
        );

        JScrollPane scrollPane =
                new JScrollPane(
                        reportArea
                );

        scrollPane.setBorder(
                BorderFactory.createLineBorder(
                        LIGHT_BLUE,
                        2
                )
        );

        JPanel centerPanel =
                new JPanel(
                        new BorderLayout()
                );

        centerPanel.setBackground(
                Color.WHITE
        );

        centerPanel.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        centerPanel.add(
                scrollPane,
                BorderLayout.CENTER
        );

        add(
                centerPanel,
                BorderLayout.CENTER
        );
    }

    // ==================================================
    // FOOTER
    // ==================================================

    private void createFooter() {

        JPanel footerPanel =
                new JPanel(
                        new BorderLayout()
                );

        footerPanel.setBackground(
                NAVY
        );

        footerPanel.setBorder(
                new EmptyBorder(
                        10,
                        20,
                        10,
                        20
                )
        );

        closeButton =
                new JButton(
                        "Close"
                );

        closeButton.setFocusPainted(false);

        closeButton.addActionListener(
                e -> dispose()
        );

        JLabel footerLabel =
                new JLabel(
                        "SAIVEE • Administrative Reporting"
                );

        footerLabel.setForeground(
                LIGHT_BLUE
        );

        footerLabel.setFont(
                new Font(
                        "Arial",
                        Font.BOLD,
                        12
                )
        );

        footerPanel.add(
                footerLabel,
                BorderLayout.WEST
        );

        footerPanel.add(
                closeButton,
                BorderLayout.EAST
        );

        add(
                footerPanel,
                BorderLayout.SOUTH
        );
    }

    // ==================================================
    // DISPLAY REPORTS
    // ==================================================

    private void displayReports(
            List<Report> reports) {

        if (reports == null || reports.isEmpty()) {

            reportArea.setText(
                    "No reports available.\n\n"
                            + "Reports generated by the "
                            + "SAIVEE reporting module will "
                            + "appear here."
            );

            return;
        }

        StringBuilder output =
                new StringBuilder();

        output.append(
                "SAIVEE BUSINESS REPORTS\n"
        );

        output.append(
                "========================================\n\n"
        );

        output.append(
                "Total Reports: "
        );

        output.append(
                reports.size()
        );

        output.append(
                "\n\n"
        );

        for (Report report : reports) {

            if (report == null) {
                continue;
            }

            output.append(
                    "----------------------------------------\n"
            );

            output.append(
                    "Report ID      : "
            )
                    .append(
                            report.getReportId()
                    )
                    .append("\n");

            output.append(
                    "Report Type    : "
            )
                    .append(
                            report.getType()
                    )
                    .append("\n");

            output.append(
                    "Title          : "
            )
                    .append(
                            report.getTitle()
                    )
                    .append("\n");

            output.append(
                    "Description    : "
            )
                    .append(
                            report.getDescription()
                    )
                    .append("\n");

            output.append(
                    "Generated At   : "
            )
                    .append(
                            report.getGeneratedAt()
                    )
                    .append("\n");
        }

        output.append(
                "----------------------------------------\n"
        );

        reportArea.setText(
                output.toString()
        );

        reportArea.setCaretPosition(0);
    }
}