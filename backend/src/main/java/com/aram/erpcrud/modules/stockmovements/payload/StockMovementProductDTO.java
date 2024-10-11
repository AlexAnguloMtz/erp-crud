package com.aram.erpcrud.modules.stockmovements.payload;

import com.aram.erpcrud.modules.products.payload.ProductDTO;

public record StockMovementProductDTO(ProductDTO product, Integer quantity) {
}
