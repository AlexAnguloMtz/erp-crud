package com.aram.erpcrud.data;

import com.aram.erpcrud.movements.domain.MovementType;
import com.aram.erpcrud.movements.domain.MovementTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;

@Component
class MovementTypeSeeder {

    private final MovementTypeRepository movementTypeRepository;

    MovementTypeSeeder(MovementTypeRepository movementTypeRepository) {
        this.movementTypeRepository = movementTypeRepository;
    }

    public void seed() {
        movementTypeRepository.saveAll(movementTypes());
    }

    private Iterable<MovementType> movementTypes() {
        return List.of(
                new MovementType(UUID.randomUUID(), "Ingreso"),
                new MovementType(UUID.randomUUID(), "Devolución"),
                new MovementType(UUID.randomUUID(), "Transferencia"),
                new MovementType(UUID.randomUUID(), "Donación"),
                new MovementType(UUID.randomUUID(), "Merma")
        );
    }
}