package com.aram.erpcrud.modules.branches.application.command;

import org.springframework.web.multipart.MultipartFile;

public interface BranchImageService {

    String saveBranchImage(MultipartFile image);

    byte[] getBranchImage(String image);

    String updateBranchImage(String image, MultipartFile imageFile);

    void deleteBranchImage(String image);
}