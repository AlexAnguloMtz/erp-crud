package com.aram.erpcrud.modules.branches.adapters;

import com.aram.erpcrud.modules.branches.application.command.BranchImageService;
import lombok.extern.slf4j.Slf4j;
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

@Slf4j
@Component
public class FileSystemBranchImageService implements BranchImageService {

    @Value("${images.modules.path}")
    private String imagesDirectoryPath;

    @Value("${images.modules.branch.path}")
    private String branchImagesPath;

    @Override
    public byte[] getBranchImage(String image) {
        try {

            // Construct the full path to the image
            Path imagePath = Paths.get(fullBranchImagesPath(), image);

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

    @Override
    public String saveBranchImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty or null");
        }

        try {
            // Generate a unique filename
            String fileName = UUID.randomUUID() + "." + getExtension(image);

            // Assemble path
            Path destinationPath = Paths.get(fullBranchImagesPath(), fileName);

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

    @Override
    public String updateBranchImage(String image, MultipartFile imageFile) {
        if (!StringUtils.hasText(image)) {
            throw new IllegalArgumentException("Image reference is empty or null");
        }

        if (imageFile == null || imageFile.isEmpty()) {
            throw new IllegalArgumentException("Image file is empty or null");
        }

        try {
            // Construct the full path to the existing image
            Path existingImagePath = Paths.get(fullBranchImagesPath(), image);

            // If the image does not exist, just save it and return the pointer to it
            if (!Files.exists(existingImagePath)) {
                return saveBranchImage(imageFile);
            }

            // Overwrite the existing image with the new one
            Files.copy(imageFile.getInputStream(), existingImagePath, StandardCopyOption.REPLACE_EXISTING);

            // Return the filename of the updated image
            return image;

        } catch (Exception e) {
            log.error(e.getMessage());
            throw new RuntimeException("Failed to update image", e);
        }
    }

    @Override
    public void deleteBranchImage(String image) {
        if (!StringUtils.hasText(image)) {
            throw new IllegalArgumentException("Image reference is empty or null");
        }

        try {
            Path imagePath = Paths.get(fullBranchImagesPath(), image);
            if (Files.exists(imagePath)) {
                Files.delete(imagePath);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND);
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }

    private String fullBranchImagesPath() {
        return imagesDirectoryPath + branchImagesPath;
    }

    private String getExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename != null && filename.lastIndexOf('.') > 0) {
            return filename.substring(filename.lastIndexOf('.') + 1);
        }
        return "";
    }

}