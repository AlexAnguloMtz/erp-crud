package com.aram.erpcrud.movements.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ProductQuantityRepository extends JpaRepository<StockMovementProduct, UUID> {
}
