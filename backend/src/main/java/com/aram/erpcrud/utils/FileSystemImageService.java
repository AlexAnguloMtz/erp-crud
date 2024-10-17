package com.aram.erpcrud.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileSystemImageService {

    @Value("${images.modules.path}")
    private String imagesDirectoryPath;

    public byte[] getImage(String path, String image) {
        try {

            // Construct the full path to the image
            Path imagePath = Paths.get(fullImagePath(path), image);

            // Check if image exists
            if (!Files.exists(imagePath)) {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }

            // Read the image file as a byte array
            return Files.readAllBytes(imagePath);

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    public String saveImage(String path, MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty or null");
        }

        try {
            // Generate a unique filename
            String fileName = UUID.randomUUID() + "." + getExtension(image);

            // Assemble path
            Path destinationPath = Paths.get(fullImagePath(path), fileName);

            // Create the storage directory if it doesn't exist
            Files.createDirectories(destinationPath.getParent());

            // Save the file
            Files.copy(image.getInputStream(), destinationPath);

            // Return the fileName of the saved image
            return fileName;

        } catch (Exception e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    public String updateImage(String path, String image, MultipartFile imageFile) {
        if (!StringUtils.hasText(image)) {
            throw new IllegalArgumentException("Image reference is empty or null");
        }

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty or null");
        }

        try {
            // Construct the full path to the existing image
            Path existingImagePath = Paths.get(fullImagePath(path), image);

            // If the image does not exist, just save it and return the pointer to it
            if (!Files.exists(existingImagePath)) {
                return saveImage(path, imageFile);
            }

            // Overwrite the existing image with the new one
            Files.copy(imageFile.getInputStream(), existingImagePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the filename of the updated image
            return image;

        } catch (Exception e) {
            throw new RuntimeException("Failed to update image", e);
        }
    }

    public void deleteImage(String path, String image) {
        if (!StringUtils.hasText(image)) {
            throw new IllegalArgumentException("Image reference is empty or null");
        }

        try {
            Path imagePath = Paths.get(fullImagePath(path), image);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    private String fullImagePath(String path) {
        return imagesDirectoryPath + path;
    }

    private String getExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.lastIndexOf('.') > 0) {
            return filename.substring(filename.lastIndexOf('.') + 1);
        }
        return "";
    }

}
