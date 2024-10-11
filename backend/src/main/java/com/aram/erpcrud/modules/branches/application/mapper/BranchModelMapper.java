package com.aram.erpcrud.modules.branches.application.mapper;

import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchAddress;
import com.aram.erpcrud.modules.branches.payload.BranchAddressDTO;
import com.aram.erpcrud.modules.branches.payload.BranchCommand;
import com.aram.erpcrud.modules.branches.payload.BranchDTO;
import org.springframework.stereotype.Component;

@Component
public class BranchModelMapper {

    public BranchDTO toBranchDTO(Branch branch) {
        return BranchDTO.builder()
                .id(branch.getId())
                .name(branch.getName())
                .phone(branch.getPhone())
                .address(toBranchAddressDTO(branch.getAddress()))
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

    public Branch toBranch(BranchCommand command) {
        return Branch.builder()
                .name(command.name())
                .phone(command.phone())
                .address(makeBranchAddress(command))
                .build();
    }

    public BranchAddress makeBranchAddress(BranchCommand command) {
        return BranchAddress.builder()
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .zipCode(command.zipCode())
                .build();
    }
}