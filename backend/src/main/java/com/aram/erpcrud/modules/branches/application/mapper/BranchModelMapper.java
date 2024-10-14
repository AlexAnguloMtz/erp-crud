package com.aram.erpcrud.modules.branches.application.mapper;

import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchAddress;
import com.aram.erpcrud.modules.branches.domain.BranchType;
import com.aram.erpcrud.modules.branches.payload.*;
import org.springframework.stereotype.Component;

@Component
public class BranchModelMapper {

    public BranchDTO toBranchDTO(Branch branch) {
        return BranchDTO.builder()
                .id(branch.getId())
                .name(branch.getName())
                .phone(branch.getPhone())
                .image(branch.getImage())
                .address(toBranchAddressDTO(branch.getAddress()))
                .branchType(toBranchTypeDTO(branch.getBranchType()))
                .build();
    }

    public BranchAddressDTO toBranchAddressDTO(BranchAddress address) {
        return BranchAddressDTO.builder()
                .id(address.getId())
                .district(address.getDistrict())
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .zipCode(address.getZipCode())
                .build();
    }

    public Branch toBranch(CreateBranchCommand command, BranchType branchType) {
        return Branch.builder()
                .name(command.name())
                .phone(command.phone())
                .address(toBranchAddress(command))
                .branchType(branchType)
                .build();
    }

    public BranchAddress toBranchAddress(CreateBranchCommand command) {
        return BranchAddress.builder()
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .zipCode(command.zipCode())
                .build();
    }

    public BranchTypeDTO toBranchTypeDTO(BranchType branchType) {
        return BranchTypeDTO.builder()
                .id(branchType.getId())
                .name(branchType.getName())
                .description(branchType.getDescription())
                .build();
    }

    public BranchType toBranchType(BranchTypeCommand command) {
        return BranchType.builder()
                .name(command.name())
                .description(command.description())
                .build();
    }
}