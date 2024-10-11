package com.aram.erpcrud.modules.branches.payload;

import lombok.Builder;

@Builder
public record GetBranchesQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) {
}
