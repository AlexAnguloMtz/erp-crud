package com.aram.erpcrud.modules.products.payload;

import lombok.Builder;

@Builder
public record GetBrandsQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) {
}