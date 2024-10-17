package com.aram.erpcrud.modules.products.rest;

import com.aram.erpcrud.modules.products.application.InventoryUnitFacade;
import com.aram.erpcrud.modules.products.payload.BrandDTO;
import com.aram.erpcrud.modules.products.payload.InventoryUnitDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inventory-units")
public class InventoryUnitController {

    private final InventoryUnitFacade inventoryUnitFacade;

    public InventoryUnitController(InventoryUnitFacade inventoryUnitFacade) {
        this.inventoryUnitFacade = inventoryUnitFacade;
    }

    @GetMapping("/all")
    public ResponseEntity<Iterable<InventoryUnitDTO>> getAllInventoryUnits() {
        return new ResponseEntity<>(inventoryUnitFacade.getAllInventoryUnits(), HttpStatus.OK);
    }

}
