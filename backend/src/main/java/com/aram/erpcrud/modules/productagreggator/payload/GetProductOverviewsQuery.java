package com.aram.erpcrud.modules.productagreggator.payload;

import lombok.Builder;

@Builder
public record GetProductOverviewsQuery(
        String search,
        String sort,
        Integer pageNumber,
        Integer pageSize
) {
}
