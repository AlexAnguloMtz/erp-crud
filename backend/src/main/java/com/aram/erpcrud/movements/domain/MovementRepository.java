package com.aram.erpcrud.movements.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.UUID;

public interface MovementRepository extends JpaRepository<Movement, UUID>, JpaSpecificationExecutor<Movement> {
}
