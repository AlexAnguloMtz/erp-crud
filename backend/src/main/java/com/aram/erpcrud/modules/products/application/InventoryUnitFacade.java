package com.aram.erpcrud.modules.products.application;

import com.aram.erpcrud.modules.products.application.mapper.ProductModelMapper;
import com.aram.erpcrud.modules.products.domain.InventoryUnitRepository;
import com.aram.erpcrud.modules.products.payload.InventoryUnitDTO;
import org.springframework.stereotype.Component;

@Component
public class InventoryUnitFacade {

    private final InventoryUnitRepository inventoryUnitRepository;
    private final ProductModelMapper productModelMapper;

    public InventoryUnitFacade(
            InventoryUnitRepository inventoryUnitRepository,
            ProductModelMapper productModelMapper
    ) {
        this.inventoryUnitRepository = inventoryUnitRepository;
        this.productModelMapper = productModelMapper;
    }

    public Iterable<InventoryUnitDTO> getAllInventoryUnits() {
        return inventoryUnitRepository.findAll().stream()
                .map(productModelMapper::toInventoryUnitDTO)
                .toList();
    }

}
