package com.aram.erpcrud.modules.branches.application;

import com.aram.erpcrud.modules.branches.application.command.CreateBranch;
import com.aram.erpcrud.modules.branches.application.command.DeleteBranchById;
import com.aram.erpcrud.modules.branches.application.command.UpdateBranch;
import com.aram.erpcrud.modules.branches.application.query.GetBranches;
import com.aram.erpcrud.modules.branches.payload.CreateBranchCommand;
import com.aram.erpcrud.modules.branches.payload.BranchDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchesQuery;
import com.aram.erpcrud.modules.branches.payload.UpdateBranchCommand;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class BranchFacade {

    private final GetBranches getBranches;
    private final CreateBranch createBranch;
    private final UpdateBranch updateBranch;
    private final DeleteBranchById deleteBranchById;

    public BranchFacade(
            GetBranches getBranches,
            CreateBranch createBranch,
            UpdateBranch updateBranch,
            DeleteBranchById deleteBranchById
    ) {
        this.getBranches = getBranches;
        this.createBranch = createBranch;
        this.updateBranch = updateBranch;
        this.deleteBranchById = deleteBranchById;
    }

    public PageResponse<BranchDTO> getBranches(GetBranchesQuery query) {
        return getBranches.handle(query);
    }

    public void createBranch(CreateBranchCommand command, MultipartFile image) {
        createBranch.handle(command, image);
    }

    public void updateBranch(Long id, UpdateBranchCommand command, MultipartFile image) {
        updateBranch.handle(id, command, image);
    }

    public void deleteBranchById(Long id) {
        deleteBranchById.handle(id);
    }

}