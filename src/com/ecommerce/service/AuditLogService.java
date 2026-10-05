package com.ecommerce.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class AuditLogService {
    private static final AuditLogService INSTANCE = new AuditLogService();
    private final List<String> logs = new ArrayList<>();
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private AuditLogService() {}
    public static AuditLogService getInstance() { return INSTANCE; }

    public synchronized void log(String actor, String action) {
        logs.add("[" + LocalDateTime.now().format(formatter) + "] "
                + actor + " -> " + action);
    }

    public synchronized List<String> getLogs() {
        return Collections.unmodifiableList(new ArrayList<>(logs));
    }

    public synchronized String getFormattedLogs() {
        return logs.isEmpty() ? "No audit activity recorded."
                : String.join(System.lineSeparator(), logs);
    }

    public synchronized void clear() { logs.clear(); }
}
