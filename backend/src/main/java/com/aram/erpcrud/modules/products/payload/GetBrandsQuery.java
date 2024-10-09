package com.aram.erpcrud.modules.products.payload;

public record GetBrandsQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) {
}