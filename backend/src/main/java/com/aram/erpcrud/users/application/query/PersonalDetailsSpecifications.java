package com.aram.erpcrud.users.application.query;

import com.aram.erpcrud.users.domain.PersonalDetails;
import jakarta.persistence.criteria.*;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface PersonalDetailsSpecifications {

    static Specification<PersonalDetails> withSearch(String search) {
        return (root, query, criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
            Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern);
            return criteriaBuilder.or(firstNamePredicate, lastNamePredicate);
        };
    }

    static Specification<PersonalDetails> livesInAnyState(List<String> stateIds) {
        return (root, query, criteriaBuilder) -> {
            if (stateIds == null || stateIds.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            return root.get("state").get("id").in(stateIds);
        };
    }
}