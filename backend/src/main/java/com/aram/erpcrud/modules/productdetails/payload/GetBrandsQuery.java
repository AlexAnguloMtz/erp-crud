package com.aram.erpcrud.modules.productdetails.payload;

public record GetBrandsQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) {
}