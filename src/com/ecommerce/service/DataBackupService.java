package com.ecommerce.service;

import com.ecommerce.util.FileStorageService;

import java.io.IOException;

public class DataBackupService {

    private final FileStorageService fileStorageService;

    public DataBackupService(String fileName) {
        fileStorageService = new FileStorageService(fileName);
    }

    public void backupData(String data) {

        try {

            fileStorageService.writeData(data);

            System.out.println(
                    "Data backup completed successfully."
            );

        } catch (IOException e) {

            System.out.println(
                    "Backup failed: " + e.getMessage()
            );
        }
    }

    public String restoreData() {

        try {

            return fileStorageService.readData();

        } catch (IOException e) {

            System.out.println(
                    "Restore failed: " + e.getMessage()
            );

            return "";
        }
    }

    public boolean backupExists() {

        return fileStorageService.fileExists();
    }
}