package com.aram.erpcrud.modules.branches.rest;

import com.aram.erpcrud.modules.branches.application.BranchFacade;
import com.aram.erpcrud.modules.branches.application.BranchImageService;
import com.aram.erpcrud.modules.branches.payload.CreateBranchCommand;
import com.aram.erpcrud.modules.branches.payload.BranchDTO;
import com.aram.erpcrud.modules.branches.payload.GetBranchesQuery;
import com.aram.erpcrud.modules.branches.payload.UpdateBranchCommand;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/branches")
public class BranchController {

    private final BranchFacade branchFacade;
    private final BranchImageService branchImageService;

    public BranchController(
            BranchFacade branchFacade,
            BranchImageService branchImageService
    ) {
        this.branchFacade = branchFacade;
        this.branchImageService = branchImageService;
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

    @PostMapping(consumes = { "multipart/form-data" })
    public ResponseEntity<Void> createBranch(
            @Valid @RequestPart CreateBranchCommand command,
            @RequestPart(required = false) MultipartFile image
    ) {
        branchFacade.createBranch(command, image);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping(value = "/{id}", consumes = { "multipart/form-data" })
    public ResponseEntity<Void> updateBranch(
            @PathVariable Long id,
            @Valid @RequestPart UpdateBranchCommand command,
            @RequestPart(required = false) MultipartFile image
    ) {
        branchFacade.updateBranch(id, command, image);
        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBranchById(@PathVariable("id") Long id) {
        branchFacade.deleteBranchById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }

    @GetMapping(value = "/branch-image/{image}", produces = { MediaType.APPLICATION_OCTET_STREAM_VALUE })
    public ResponseEntity<byte[]> getBranchImage(@PathVariable String image) {
        return new ResponseEntity<>(branchImageService.getBranchImage(image), HttpStatus.OK);
    }

}