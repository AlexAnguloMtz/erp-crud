package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.domain.Product;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecifications {

    static Specification<Product> withSearch(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return builder.conjunction();
            }

            String searchPattern = "%" + search.toLowerCase() + "%";

            return builder.or(
                    builder.like(builder.lower(root.get("name")), searchPattern),
                    builder.like(builder.lower(root.get("sku")), searchPattern),
                    builder.like(builder.lower(root.join("brand").get("name")), searchPattern),
                    builder.like(builder.lower(root.join("productCategory").get("name")), searchPattern),
                    builder.like(builder.lower(root.join("inventoryUnit").get("name")), searchPattern)
            );
        };
    }
}