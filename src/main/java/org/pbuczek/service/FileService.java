package org.pbuczek.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class FileService {
    public void createDirectory(String folderPath) throws IOException {
        try {
            Path path = Paths.get(folderPath);
            Files.createDirectories(path);

        } catch (IOException e) {
            throw new IOException("Failed to create directory:" + folderPath  + e.getMessage());
        }
    }
}
