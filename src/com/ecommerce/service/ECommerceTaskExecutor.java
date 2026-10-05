package com.ecommerce.service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ECommerceTaskExecutor {

    private final ExecutorService executorService;

    public ECommerceTaskExecutor() {
        executorService = Executors.newFixedThreadPool(3);
    }

    public void submitTask(Runnable task) {

        if (task == null) {
            return;
        }

        executorService.submit(task);
    }

    public void shutdown() {

        executorService.shutdown();

        System.out.println(
                "Task executor shutdown requested."
        );
    }

    public boolean isShutdown() {

        return executorService.isShutdown();
    }
}