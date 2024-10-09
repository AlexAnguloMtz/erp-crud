package com.aram.erpcrud.modules.stockmovements.payload;

import java.time.Instant;

public record GetMovementsQuery(
        Integer pageNumber,
        Integer pageSize,
        String sort,
        Instant start,
        Instant end,
        Long productId,
        Long responsibleId,
        Long movementTypeId
) {
}