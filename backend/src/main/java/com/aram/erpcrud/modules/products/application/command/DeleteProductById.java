package com.aram.erpcrud.modules.products.application.command;

import com.aram.erpcrud.modules.products.domain.ProductRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Component;

@Component
public class DeleteProductById {

    private final ProductRepository productRepository;

    public DeleteProductById(
            ProductRepository productRepository
    ) {
        this.productRepository = productRepository;
    }

    @Transactional
    public void handle(Long id) {
        productRepository.deleteById(id);
    }
}