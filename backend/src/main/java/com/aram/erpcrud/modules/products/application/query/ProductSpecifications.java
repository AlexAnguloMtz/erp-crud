package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.domain.Brand;
import com.aram.erpcrud.modules.products.domain.Product;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

public interface ProductSpecifications {

    static Specification<Product> withSearch(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return builder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";

            Join<Product, Brand> brandJoin = root.join("brand");

            Predicate productNamePredicate = builder.like(builder.lower(root.get("name")), searchPattern);
            Predicate brandNamePredicate = builder.like(builder.lower(brandJoin.get("name")), searchPattern);

            return builder.or(productNamePredicate, brandNamePredicate);
        };
    }
}