package com.aram.erpcrud.modules.products.adapters;

import com.aram.erpcrud.modules.products.application.ProductImageService;
import com.aram.erpcrud.utils.FileSystemImageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileSystemProductImageService implements ProductImageService {

    @Value("${images.modules.product.path}")
    private String productImagesPath;

    private final FileSystemImageService fileSystemImageService;

    public FileSystemProductImageService(
            FileSystemImageService fileSystemImageService
    ) {
        this.fileSystemImageService = fileSystemImageService;
    }

    @Override
    public String saveProductImage(MultipartFile image) {
        return fileSystemImageService.saveImage(productImagesPath, image);
    }

    @Override
    public byte[] getProductImage(String image) {
        return fileSystemImageService.getImage(productImagesPath, image);
    }

    @Override
    public String updateProductImage(String image, MultipartFile imageFile) {
        return fileSystemImageService.updateImage(productImagesPath, image, imageFile);
    }

    @Override
    public void deleteProductImage(String image) {
        fileSystemImageService.deleteImage(productImagesPath, image);
    }

}
