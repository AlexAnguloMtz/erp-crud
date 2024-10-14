package com.aram.erpcrud.modules.branches.application;

import com.aram.erpcrud.modules.branches.application.command.CreateBranchType;
import com.aram.erpcrud.modules.branches.application.command.DeleteBranchTypeById;
import com.aram.erpcrud.modules.branches.application.command.UpdateBranchType;
import com.aram.erpcrud.modules.branches.application.query.GetAllBranchTypes;
import com.aram.erpcrud.modules.branches.application.query.GetBranchTypes;
import com.aram.erpcrud.modules.branches.payload.BranchTypeCommand;
import com.aram.erpcrud.modules.branches.payload.BranchTypeDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchTypesQuery;
import com.aram.erpcrud.utils.PageResponse;
import org.springframework.stereotype.Component;

@Component
public class BranchTypeFacade {

    private final GetBranchTypes getBranchTypes;
    private final GetAllBranchTypes getAllBranchTypes;
    private final CreateBranchType createBranchType;
    private final UpdateBranchType updateBranchType;
    private final DeleteBranchTypeById deleteBranchTypeById;

    public BranchTypeFacade(
            GetBranchTypes getBranchTypes,
            GetAllBranchTypes getAllBranchTypes,
            CreateBranchType createBranchType,
            UpdateBranchType updateBranchType,
            DeleteBranchTypeById deleteBranchTypeById
    ) {
        this.getBranchTypes = getBranchTypes;
        this.getAllBranchTypes = getAllBranchTypes;
        this.createBranchType = createBranchType;
        this.updateBranchType = updateBranchType;
        this.deleteBranchTypeById = deleteBranchTypeById;
    }

    public PageResponse<BranchTypeDTO> getBranchTypes(GetBranchTypesQuery query) {
        return getBranchTypes.handle(query);
    }

    public Iterable<BranchTypeDTO> getAllBranchTypes() {
        return getAllBranchTypes.get();
    }

    public void createBranchType(BranchTypeCommand command) {
        createBranchType.handle(command);
    }

    public void updateBranchType(Long id, BranchTypeCommand command) {
        updateBranchType.handle(id, command);
    }

    public void deleteBranchTypeById(Long id) {
        deleteBranchTypeById.handle(id);
    }

}