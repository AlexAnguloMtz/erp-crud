package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.domain.*;
import com.aram.erpcrud.modules.branches.payload.UpdateBranchCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Component
public class UpdateBranch {

    private final BranchRepository branchRepository;
    private final BranchTypeRepository branchTypeRepository;
    private final BranchImageService branchImageService;

    public UpdateBranch(
            BranchRepository branchRepository,
            BranchTypeRepository branchTypeRepository,
            BranchImageService branchImageService
    ) {
        this.branchRepository = branchRepository;
        this.branchTypeRepository = branchTypeRepository;
        this.branchImageService = branchImageService;
    }

    @Transactional
    public void handle(Long id, UpdateBranchCommand command, MultipartFile imageFile) {
        ValidImageAction validImageAction = ValidImageAction.makeOrThrow(command.imageAction(), imageFile);

        Branch branch = findBranchWithUniqueFieldsOrThrow(id, command.name());
        BranchType branchType = findBranchTypeByIdOrThrow(command.branchTypeId());

        String imageReference = executeBranchImageAction(validImageAction, branch.getImage());
        branch.setImage(imageReference);

        setBranchFields(branch, branchType, command);

        branchRepository.save(branch);
    }

    private Branch findBranchWithUniqueFieldsOrThrow(Long branchId, String newBranchName) {
        Optional<Branch> byIdOptional = branchRepository.findById(branchId);
        if (byIdOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Branch branch = byIdOptional.get();

        Optional<Branch> byNameOptional = branchRepository.findByName(newBranchName);

        boolean nameConflict = byNameOptional.isPresent() &&
                (!Objects.equals(branch.getId(), byNameOptional.get().getId()));

        if (nameConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        return branch;
    }

    private BranchType findBranchTypeByIdOrThrow(Long id) {
        return branchTypeRepository.findById(id).orElseThrow(
                () -> new ResponseStatusException(HttpStatus.NOT_FOUND)
        );
    }

    private void setBranchFields(Branch branch, BranchType branchType, UpdateBranchCommand command) {
        branch.setName(command.name());
        branch.setPhone(command.phone());
        branch.setBranchType(branchType);
        setBranchAddressFields(branch.getAddress(), command);
    }

    private void setBranchAddressFields(BranchAddress address, UpdateBranchCommand command) {
        address.setDistrict(command.district());
        address.setStreet(command.street());
        address.setStreetNumber(command.streetNumber());
        address.setZipCode(command.zipCode());
    }

    private String editBranchImage(String imageReference, MultipartFile imageFile) {
        if (StringUtils.hasText(imageReference)) {
            return branchImageService.updateBranchImage(imageReference, imageFile);
        } else {
            return branchImageService.saveBranchImage(imageFile);
        }
    }

    private void deleteBranchImage(String imageReference) {
        if (StringUtils.hasText(imageReference)) {
            branchImageService.deleteBranchImage(imageReference);
        }
    }

    private String executeBranchImageAction(
            ValidImageAction imageAction,
            String imageReference
    ) {
        if (Objects.equals("edit", imageAction.action)) {
            return editBranchImage(imageReference, imageAction.imageFile);
        } else if (Objects.equals("delete", imageAction.action)) {
            deleteBranchImage(imageReference);
            return null;
        }
        return imageReference;
    }

    private record ValidImageAction(String action, MultipartFile imageFile) {

        private static ValidImageAction makeOrThrow(String imageAction, MultipartFile image) {
            if (!List.of("none", "edit", "delete").contains(imageAction)) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid image action: %s".formatted(imageAction));
            }

            if (!Objects.equals("edit", imageAction) && image != null) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "If Branch image file is present, action must be 'edit'. Got action: %s".formatted(imageAction)
                );
            }

            if (Objects.equals("edit", imageAction) && (image == null || image.isEmpty())) {
                throw new ResponseStatusException(
                        HttpStatus.BAD_REQUEST,
                        "Can't edit Branch image because image file is empty"
                );
            }

            return new ValidImageAction(imageAction, image);
        }

    }

}