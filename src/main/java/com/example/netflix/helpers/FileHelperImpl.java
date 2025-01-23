package com.example.netflix.helpers;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Component
public class FileHelperImpl implements FileHelper {
    @Override
    public String uploadFile(String folder, String fileName, byte[] fileContent) {
        try {
            var folderPath = Paths.get(folder);
            if (!Files.exists(folderPath)) {
                Files.createDirectories(folderPath);
            }
            String newName = UUID.randomUUID() + "_" + fileName;
            var path = Paths.get(folder, newName);
            Files.write(path, fileContent);

            return newName;
        } catch (IOException e) {
            System.out.println("Error uploading file: " + e.getMessage());
            return null;
        }
    }
}