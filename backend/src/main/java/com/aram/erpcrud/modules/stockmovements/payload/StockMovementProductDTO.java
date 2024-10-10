package com.aram.erpcrud.modules.stockmovements.payload;

import com.aram.erpcrud.modules.productdetails.payload.ProductDTO;

public record StockMovementProductDTO(ProductDTO product, Integer quantity) {
}
