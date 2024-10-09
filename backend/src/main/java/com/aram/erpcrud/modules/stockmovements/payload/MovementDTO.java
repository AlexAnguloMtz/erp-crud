package com.aram.erpcrud.modules.stockmovements.payload;

import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;

import java.time.Instant;
import java.util.List;

public record MovementDTO(
        Long id,
        PersonalNameDTO responsible,
        MovementTypeDTO movementType,
        List<StockMovementProductDTO> productQuantities,
        String observations,
        Instant timestamp
) {
}