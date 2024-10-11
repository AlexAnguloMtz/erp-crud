package com.aram.erpcrud.modules.branches.application;

import com.aram.erpcrud.modules.branches.application.command.CreateBranchCommandHandler;
import com.aram.erpcrud.modules.branches.application.command.DeleteBranchByIdCommandHandler;
import com.aram.erpcrud.modules.branches.application.command.UpdateBranchCommandHandler;
import com.aram.erpcrud.modules.branches.application.query.GetBranchesQueryHandler;
import com.aram.erpcrud.modules.branches.payload.BranchCommand;
import com.aram.erpcrud.modules.branches.payload.BranchDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchesQuery;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;

@Component
public class BranchFacade {

    private final GetBranchesQueryHandler getBranchesQueryHandler;
    private final CreateBranchCommandHandler createBranchCommandHandler;
    private final UpdateBranchCommandHandler updateBranchCommandHandler;
    private final DeleteBranchByIdCommandHandler deleteBranchByIdCommandHandler;

    public BranchFacade(
            GetBranchesQueryHandler getBranchesQueryHandler,
            CreateBranchCommandHandler createBranchCommandHandler,
            UpdateBranchCommandHandler updateBranchCommandHandler,
            DeleteBranchByIdCommandHandler deleteBranchByIdCommandHandler
    ) {
        this.getBranchesQueryHandler = getBranchesQueryHandler;
        this.createBranchCommandHandler = createBranchCommandHandler;
        this.updateBranchCommandHandler = updateBranchCommandHandler;
        this.deleteBranchByIdCommandHandler = deleteBranchByIdCommandHandler;
    }

    public PageResponse<BranchDTO> getBranches(GetBranchesQuery query) {
        return getBranchesQueryHandler.handle(query);
    }

    public void createBranch(BranchCommand command) {
        createBranchCommandHandler.handle(command);
    }

    public void updateBranch(Long id, BranchCommand command) {
        updateBranchCommandHandler.handle(id, command);
    }

    public void deleteBranchById(Long id) {
        deleteBranchByIdCommandHandler.handle(id);
    }

}