package com.aram.erpcrud.modules.branches.rest;

import com.aram.erpcrud.modules.branches.application.BranchTypeFacade;
import com.aram.erpcrud.modules.branches.payload.*;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    public ResponseEntity<PageResponse<BranchTypeDTO>> getBranchTypes(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort
    ) {
        GetBranchTypesQuery query = GetBranchTypesQuery.builder()
                .search(search)
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .sort(sort)
                .build();

        return new ResponseEntity<>(branchTypeFacade.getBranchTypes(query), HttpStatus.OK);
    }

    @GetMapping("/all")
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