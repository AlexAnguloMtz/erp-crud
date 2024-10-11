package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchAddress;
import com.aram.erpcrud.modules.branches.domain.BranchRepository;
import com.aram.erpcrud.modules.branches.payload.BranchCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Component
public class UpdateBranchCommandHandler {

    private final BranchRepository branchRepository;

    public UpdateBranchCommandHandler(
            BranchRepository branchRepository
    ) {
        this.branchRepository = branchRepository;
    }

    @Transactional
    public void handle(Long id, BranchCommand command) {
        Optional<Branch> byIdOptional = branchRepository.findById(id);
        if (byIdOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Branch branch = byIdOptional.get();

        Optional<Branch> byNameOptional = branchRepository.findByName(command.name());

        boolean nameConflict = byNameOptional.isPresent() &&
                (!Objects.equals(branch.getId(), byNameOptional.get().getId()));

        if (nameConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        branch.setName(command.name());
        branch.setPhone(command.phone());

        BranchAddress address = branch.getAddress();
        address.setDistrict(command.district());
        address.setStreet(command.street());
        address.setStreetNumber(command.streetNumber());
        address.setZipCode(command.zipCode());

        branchRepository.save(branch);
    }

}