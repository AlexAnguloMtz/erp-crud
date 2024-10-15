package com.aram.erpcrud.modules.products.application.query;

import com.aram.erpcrud.modules.products.domain.ProductCategory;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

public interface ProductCategorySpecifications {

    static Specification<ProductCategory> withSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
            return criteriaBuilder.or(namePredicate);
        };
    }

}
