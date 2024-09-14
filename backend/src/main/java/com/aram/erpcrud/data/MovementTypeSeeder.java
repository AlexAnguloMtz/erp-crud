package com.aram.erpcrud.data;

import com.aram.erpcrud.movements.domain.MovementType;
import com.aram.erpcrud.movements.domain.MovementTypeRepository;
import org.springframework.stereotype.Component;

import java.util.List;

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
                new MovementType("1", "Ingreso"),
                new MovementType("2", "Devolución"),
                new MovementType("3", "Transferencia"),
                new MovementType("4", "Donación"),
                new MovementType("5", "Merma")
        );
    }
}