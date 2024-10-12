package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchRepository;
import com.aram.erpcrud.modules.branches.domain.BranchType;
import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import com.aram.erpcrud.modules.branches.payload.BranchCommand;
import jakarta.transaction.Transactional;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class CreateBranchCommandHandler {

    private final BranchModelMapper branchModelMapper;
    private final BranchRepository branchRepository;
    private final BranchTypeRepository branchTypeRepository;

    public CreateBranchCommandHandler(
            BranchModelMapper branchModelMapper,
            BranchRepository branchRepository,
            BranchTypeRepository branchTypeRepository
    ) {
        this.branchModelMapper = branchModelMapper;
        this.branchRepository = branchRepository;
        this.branchTypeRepository = branchTypeRepository;
    }

    @Transactional
    public void handle(BranchCommand command) {
        Optional<Branch> branchOptional = branchRepository.findByName(command.name());
        if (branchOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Optional<BranchType> branchTypeOptional = branchTypeRepository.findById(command.branchTypeId());
        if (branchTypeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Branch branch = branchModelMapper.toBranch(command, branchTypeOptional.get());

        branchRepository.save(branch);
    }
}