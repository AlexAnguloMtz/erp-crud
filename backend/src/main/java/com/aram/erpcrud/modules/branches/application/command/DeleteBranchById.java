package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.domain.BranchRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DeleteBranchById {

    private final BranchRepository branchRepository;

    public DeleteBranchById(
            BranchRepository branchRepository
    ) {
        this.branchRepository = branchRepository;
    }

    @Transactional
    public void handle(Long id) {
        branchRepository.deleteById(id);
    }
}