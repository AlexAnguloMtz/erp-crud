package com.aram.erpcrud.modules.branches.application.command;

import com.aram.erpcrud.modules.branches.application.mapper.BranchModelMapper;
import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchRepository;
import com.aram.erpcrud.modules.branches.domain.BranchType;
import com.aram.erpcrud.modules.branches.domain.BranchTypeRepository;
import com.aram.erpcrud.modules.branches.payload.BranchCommand;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Slf4j
@Component
public class CreateBranch {

    private final BranchModelMapper branchModelMapper;
    private final BranchRepository branchRepository;
    private final BranchTypeRepository branchTypeRepository;
    private final BranchImageService branchImageService;

    public CreateBranch(
            BranchModelMapper branchModelMapper,
            BranchRepository branchRepository,
            BranchTypeRepository branchTypeRepository,
            BranchImageService branchImageService
    ) {
        this.branchModelMapper = branchModelMapper;
        this.branchRepository = branchRepository;
        this.branchTypeRepository = branchTypeRepository;
        this.branchImageService = branchImageService;
    }

    @Transactional
    public void handle(BranchCommand command, MultipartFile image) {
        Optional<Branch> branchOptional = branchRepository.findByName(command.name());
        if (branchOptional.isPresent()) {
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }

        Optional<BranchType> branchTypeOptional = branchTypeRepository.findById(command.branchTypeId());
        if (branchTypeOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }

        Branch branch = branchModelMapper.toBranch(command, branchTypeOptional.get());

        if (image != null && !image.isEmpty()) {
            String imagePath = branchImageService.saveBranchImage(image);
            branch.setImage(imagePath);
        }

        branchRepository.save(branch);
    }

}