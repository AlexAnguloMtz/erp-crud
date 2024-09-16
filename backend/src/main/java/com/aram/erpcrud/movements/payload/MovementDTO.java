package com.aram.erpcrud.movements.payload;

import com.aram.erpcrud.users.payload.PersonalNameDTO;

import java.time.Instant;
import java.util.List;

public record MovementDTO(
        String id,
        PersonalNameDTO responsible,
        MovementTypeDTO movementType,
        List<ProductQuantityDTO> productQuantities,
        String observations,
        Instant timestamp
) {
}