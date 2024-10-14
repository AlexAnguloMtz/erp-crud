package com.aram.erpcrud.modules.branches.adapters;

import com.aram.erpcrud.modules.branches.application.command.BranchImageService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Slf4j
@Component
public class FileSystemBranchImageService implements BranchImageService {

    @Value("${images.modules.path}")
    private String imagesDirectoryPath;

    @Value("${images.modules.branch.path}")
    private String branchImagesPath;

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