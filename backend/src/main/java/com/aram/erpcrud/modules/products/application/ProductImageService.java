package com.aram.erpcrud.modules.products.application;

import org.springframework.web.multipart.MultipartFile;

public interface ProductImageService {

    String saveProductImage(MultipartFile image);

    byte[] getProductImage(String image);

    String updateProductImage(String image, MultipartFile imageFile);

    void deleteProductImage(String image);

}