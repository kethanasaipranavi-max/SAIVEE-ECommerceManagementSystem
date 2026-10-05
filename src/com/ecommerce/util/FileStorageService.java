package com.ecommerce.util;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;

public class FileStorageService {

    private final Path filePath;

    public FileStorageService(String fileName) {

        this.filePath = Path.of(fileName);
    }

    public void writeData(String data) throws IOException {

        Files.writeString(
                filePath,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
        );
    }

    public void appendData(String data) throws IOException {

        Files.writeString(
                filePath,
                data,
                StandardOpenOption.CREATE,
                StandardOpenOption.APPEND
        );
    }

    public String readData() throws IOException {

        if (!Files.exists(filePath)) {
            return "";
        }

        return Files.readString(filePath);
    }

    public List<String> readAllLines() throws IOException {

        if (!Files.exists(filePath)) {
            return List.of();
        }

        return Files.readAllLines(filePath);
    }

    public boolean fileExists() {

        return Files.exists(filePath);
    }

    public void deleteFile() throws IOException {

        if (Files.exists(filePath)) {

            Files.delete(filePath);

            System.out.println(
                    "File deleted successfully."
            );
        }
    }

    public Path getFilePath() {

        return filePath;
    }
}