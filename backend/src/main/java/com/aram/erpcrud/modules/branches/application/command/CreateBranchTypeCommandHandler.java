package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.BranchType;
import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import com.aram.erpcrud.modules.branches.payload.BranchTypeCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class CreateBranchTypeCommandHandler {

    private final BranchModelMapper branchModelMapper;
    private final BranchTypeRepository branchTypeRepository;

    public CreateBranchTypeCommandHandler(
            BranchModelMapper branchModelMapper,
            BranchTypeRepository branchTypeRepository
    ) {
        this.branchModelMapper = branchModelMapper;
        this.branchTypeRepository = branchTypeRepository;
    }

    @Transactional
    public void handle(BranchTypeCommand command) {
        Optional<BranchType> byNameOptional = branchTypeRepository.findByName(command.name());
        if (byNameOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        BranchType branchType = branchModelMapper.toBranchType(command);

        branchTypeRepository.save(branchType);
    }

}