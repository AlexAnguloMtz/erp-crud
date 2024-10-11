package com.aram.erpcrud.modules.products.payload;

import lombok.Builder;

import java.util.List;

@Builder
public record GetProductsQuery(
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort,
        List<Long> productTypeIds
) {
}