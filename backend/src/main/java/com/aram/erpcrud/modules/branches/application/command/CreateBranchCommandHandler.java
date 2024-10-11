package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchRepository;
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

    public CreateBranchCommandHandler(
            BranchModelMapper branchModelMapper,
            BranchRepository branchRepository
    ) {
        this.branchModelMapper = branchModelMapper;
        this.branchRepository = branchRepository;
    }

    @Transactional
    public void handle(BranchCommand command) {
        Optional<Branch> branchOptional = branchRepository.findByName(command.name());
        if (branchOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Branch branch = branchModelMapper.toBranch(command);

        branchRepository.save(branch);
    }
}