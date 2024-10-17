package com.aram.erpcrud.modules.branches.adapters;

import com.aram.erpcrud.modules.branches.application.BranchImageService;
import com.aram.erpcrud.utils.FileSystemImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSystemBranchImageService implements BranchImageService {

    @Value("${images.modules.branch.path}")
    private String branchImagesPath;

    private final FileSystemImageService fileSystemImageService;

    public FileSystemBranchImageService(
            FileSystemImageService fileSystemImageService
    ) {
        this.fileSystemImageService = fileSystemImageService;
    }

    @Override
    public String saveBranchImage(MultipartFile image) {
        return fileSystemImageService.saveImage(branchImagesPath, image);
    }

    @Override
    public byte[] getBranchImage(String image) {
        return fileSystemImageService.getImage(branchImagesPath, image);
    }

    @Override
    public String updateBranchImage(String image, MultipartFile imageFile) {
        return fileSystemImageService.updateImage(branchImagesPath, image, imageFile);
    }

    @Override
    public void deleteBranchImage(String image) {
        fileSystemImageService.deleteImage(branchImagesPath, image);
    }

}