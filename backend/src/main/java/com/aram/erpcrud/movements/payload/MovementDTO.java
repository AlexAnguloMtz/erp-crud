package com.aram.erpcrud.movements.payload;

import com.aram.erpcrud.users.payload.PersonalNameDTO;

import java.time.Instant;
import java.util.List;

public record MovementDTO(
        String id,
        PersonalNameDTO responsible,
        MovementTypeDTO movementTypeDTO,
        List<ProductQuantityDTO> productQuantities,
        Instant timestamp
) {
}