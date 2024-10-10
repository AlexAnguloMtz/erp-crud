package com.aram.erpcrud.modules.productagreggator.payload;

import lombok.Builder;

@Builder
public record ProductOverviewDTO(
        Long productId
) {
}
