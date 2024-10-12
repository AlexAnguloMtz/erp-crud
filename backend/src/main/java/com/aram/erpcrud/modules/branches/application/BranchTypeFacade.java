package com.aram.erpcrud.modules.branches.application;

import com.aram.erpcrud.modules.branches.application.command.CreateBranchTypeCommandHandler;
import com.aram.erpcrud.modules.branches.application.command.DeleteBranchTypeByIdCommandHandler;
import com.aram.erpcrud.modules.branches.application.command.UpdateBranchTypeCommandHandler;
import com.aram.erpcrud.modules.branches.application.query.GetAllBranchTypesQueryHandler;
import com.aram.erpcrud.modules.branches.payload.BranchTypeCommand;
import com.aram.erpcrud.modules.branches.payload.BranchTypeDTO;
import org.springframework.stereotype.Component;

@Component
public class BranchTypeFacade {

    private final GetAllBranchTypesQueryHandler getAllBranchTypesQueryHandler;
    private final CreateBranchTypeCommandHandler createBranchTypeCommandHandler;
    private final UpdateBranchTypeCommandHandler updateBranchTypeCommandHandler;
    private final DeleteBranchTypeByIdCommandHandler deleteBranchTypeByIdCommandHandler;

    public BranchTypeFacade(
            GetAllBranchTypesQueryHandler getAllBranchTypesQueryHandler,
            CreateBranchTypeCommandHandler createBranchTypeCommandHandler,
            UpdateBranchTypeCommandHandler updateBranchTypeCommandHandler,
            DeleteBranchTypeByIdCommandHandler deleteBranchTypeByIdCommandHandler
    ) {
        this.getAllBranchTypesQueryHandler = getAllBranchTypesQueryHandler;
        this.createBranchTypeCommandHandler = createBranchTypeCommandHandler;
        this.updateBranchTypeCommandHandler = updateBranchTypeCommandHandler;
        this.deleteBranchTypeByIdCommandHandler = deleteBranchTypeByIdCommandHandler;
    }

    public Iterable<BranchTypeDTO> getAllBranchTypes() {
        return getAllBranchTypesQueryHandler.get();
    }

    public void createBranchType(BranchTypeCommand command) {
        createBranchTypeCommandHandler.handle(command);
    }

    public void updateBranchType(Long id, BranchTypeCommand command) {
        updateBranchTypeCommandHandler.handle(id, command);
    }

    public void deleteBranchTypeById(Long id) {
        deleteBranchTypeByIdCommandHandler.handle(id);
    }

}