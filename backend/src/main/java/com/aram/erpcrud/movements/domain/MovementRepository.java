package com.aram.erpcrud.movements.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface MovementRepository extends JpaRepository<Movement, String>, JpaSpecificationExecutor<Movement> {
}
