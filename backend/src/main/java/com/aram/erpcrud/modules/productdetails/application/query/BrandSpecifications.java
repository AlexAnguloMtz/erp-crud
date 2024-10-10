package com.aram.erpcrud.modules.productdetails.application.query;

import com.aram.erpcrud.modules.productdetails.domain.Brand;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

public interface BrandSpecifications {

    static Specification<Brand> withSearch(String search) {
        return (Root<Brand> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate namePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
            return criteriaBuilder.or(namePredicate);
        };
    }

}
