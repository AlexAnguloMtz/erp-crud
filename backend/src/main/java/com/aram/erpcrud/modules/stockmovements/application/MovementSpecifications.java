package com.aram.erpcrud.modules.stockmovements.application;

import com.aram.erpcrud.modules.stockmovements.domain.StockMovement;
import com.aram.erpcrud.modules.stockmovements.domain.StockMovementProduct;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

import java.time.Instant;

public interface MovementSpecifications {

    static Specification<StockMovement> withResponsibleId(Long responsibleId) {
        return (root, _, criteriaBuilder) -> {
            if (responsibleId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("responsibleId"), responsibleId);
        };
    }

    static Specification<StockMovement> withMovementType(Long movementTypeId) {
        return (root, _, criteriaBuilder) -> {
            if (movementTypeId == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.equal(root.get("movementTypeId"), movementTypeId);
        };
    }

    static Specification<StockMovement> after(Instant instant) {
        return ((root, _, criteriaBuilder) -> {
            if (instant == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.greaterThan(root.get("timestamp"), instant);
        });
    }

    static Specification<StockMovement> before(Instant instant) {
        return ((root, _, criteriaBuilder) -> {
            if (instant == null) {
                return criteriaBuilder.conjunction();
            }
            return criteriaBuilder.lessThan(root.get("timestamp"), instant);
        });
    }

    static Specification<StockMovement> withProduct(Long productId) {
        return ((root, _, criteriaBuilder) -> {
            if (productId == null) {
                return criteriaBuilder.conjunction();
            }
            Join<StockMovement, StockMovementProduct> productQuantitiesJoin = root.join("productQuantities");
            return criteriaBuilder.equal(productQuantitiesJoin.get("productId"), productId);
        });
    }
}