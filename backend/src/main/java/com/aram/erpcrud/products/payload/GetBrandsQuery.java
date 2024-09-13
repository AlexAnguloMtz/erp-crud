package com.aram.erpcrud.products.payload;

public record GetBrandsQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) {
}