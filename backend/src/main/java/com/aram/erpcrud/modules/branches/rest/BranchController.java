package com.aram.erpcrud.modules.branches.rest;

import com.aram.erpcrud.modules.branches.application.BranchFacade;
import com.aram.erpcrud.modules.branches.payload.BranchCommand;
import com.aram.erpcrud.modules.branches.payload.BranchDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchesQuery;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchFacade branchFacade;

    public BranchController(BranchFacade branchFacade) {
        this.branchFacade = branchFacade;
    }

    @GetMapping
    public ResponseEntity<PageResponse<BranchDTO>> getBranches(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetBranchesQuery query = GetBranchesQuery.builder()
                .search(search)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        return new ResponseEntity<>(branchFacade.getBranches(query), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createBranch(@Valid @RequestBody BranchCommand command) {
        branchFacade.createBranch(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateBranch(
            @PathVariable Long id,
            @Valid @RequestBody BranchCommand command
    ) {
        branchFacade.updateBranch(id, command);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchById(@PathVariable("id") Long id) {
        branchFacade.deleteBranchById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

}