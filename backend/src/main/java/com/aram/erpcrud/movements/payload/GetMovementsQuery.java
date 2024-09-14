package com.aram.erpcrud.movements.payload;

import java.time.Instant;

public record GetMovementsQuery(
        Integer pageNumber,
        Integer pageSize,
        String sort,
        Instant start,
        Instant end,
        String productId,
        String responsibleId,
        String movementTypeId
) {
}