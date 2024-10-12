package com.aram.erpcrud.modules.branches.rest;

import com.aram.erpcrud.modules.branches.application.BranchTypeFacade;
import com.aram.erpcrud.modules.branches.payload.BranchTypeCommand;
import com.aram.erpcrud.modules.branches.payload.BranchTypeDTO;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branch-types")
public class BranchTypeController {

    private final BranchTypeFacade branchTypeFacade;

    public BranchTypeController(BranchTypeFacade branchTypeFacade) {
        this.branchTypeFacade = branchTypeFacade;
    }

    @GetMapping
    public ResponseEntity<Iterable<BranchTypeDTO>> getAllBranchTypes() {
        return new ResponseEntity<>(branchTypeFacade.getAllBranchTypes(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createBranchType(@Valid @RequestBody BranchTypeCommand command) {
        branchTypeFacade.createBranchType(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBranchType(
            @PathVariable Long id,
            @Valid @RequestBody BranchTypeCommand command
    ) {
        branchTypeFacade.updateBranchType(id, command);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchTypeById(@PathVariable("id") Long id) {
        branchTypeFacade.deleteBranchTypeById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}