package com.aram.erpcrud.movements.application;

import com.aram.erpcrud.movements.domain.Movement;
import com.aram.erpcrud.movements.domain.StockMovementProduct;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public interface MovementSpecifications {

    static Specification<Movement> withResponsibleId(String responsibleId) {
        return (root, _, criteriaBuilder) -> {
            if (responsibleId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("responsibleId"), responsibleId);
        };
    }

    static Specification<Movement> withMovementType(String movementTypeId) {
        return (root, _, criteriaBuilder) -> {
            if (movementTypeId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("movementTypeId"), movementTypeId);
        };
    }

    static Specification<Movement> after(Instant instant) {
        return ((root, _, criteriaBuilder) -> {
            if (instant == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("timestamp"), instant);
        });
    }

    static Specification<Movement> before(Instant instant) {
        return ((root, _, criteriaBuilder) -> {
            if (instant == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThan(root.get("timestamp"), instant);
        });
    }

    static Specification<Movement> withProduct(String productId) {
        return ((root, _, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<Movement, StockMovementProduct> productQuantitiesJoin = root.join("productQuantities");
            return criteriaBuilder.equal(productQuantitiesJoin.get("productId"), productId);
        });
    }
}