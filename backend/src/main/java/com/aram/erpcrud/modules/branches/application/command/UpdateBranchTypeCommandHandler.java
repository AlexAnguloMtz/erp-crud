package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.domain.BranchType;
import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import com.aram.erpcrud.modules.branches.payload.BranchTypeCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Objects;
import java.util.Optional;

@Component
public class UpdateBranchTypeCommandHandler {

    private final BranchTypeRepository branchTypeRepository;

    public UpdateBranchTypeCommandHandler(BranchTypeRepository branchTypeRepository) {
        this.branchTypeRepository = branchTypeRepository;
    }

    @Transactional
    public void handle(Long id, BranchTypeCommand command) {
        Optional<BranchType> byIdOptional = branchTypeRepository.findById(id);
        if (byIdOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        BranchType branchType = byIdOptional.get();

        Optional<BranchType> byNameOptional = branchTypeRepository.findByName(command.name());

        boolean nameConflict = byNameOptional.isPresent() &&
                (!Objects.equals(branchType.getId(), byNameOptional.get().getId()));

        if (nameConflict) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        branchType.setName(command.name());
        branchType.setDescription(command.description());

        branchTypeRepository.save(branchType);
    }
}
