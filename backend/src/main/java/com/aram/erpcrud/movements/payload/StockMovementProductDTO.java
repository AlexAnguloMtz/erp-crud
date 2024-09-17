package com.aram.erpcrud.movements.payload;

import com.aram.erpcrud.products.payload.ProductDTO;

public record StockMovementProductDTO(ProductDTO product, Integer quantity) {
}
