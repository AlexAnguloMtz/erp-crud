package com.aram.erpcrud.modules.stockmovements.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductQuantityRepository extends JpaRepository<StockMovementProduct, Long> {
}
