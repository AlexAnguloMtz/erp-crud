package com.aram.erpcrud.modules.branches.payload;

import lombok.Builder;

@Builder
public record GetBranchTypesQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) {
}
