package com.aram.erpcrud.movements.payload;

import com.aram.erpcrud.products.payload.ProductDTO;

public record ProductQuantityDTO(ProductDTO product, Integer quantity) {
}
