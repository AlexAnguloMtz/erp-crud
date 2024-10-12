package com.aram.erpcrud.modules.branches.application.query;

import com.aram.erpcrud.modules.branches.domain.BranchType;
import org.springframework.data.jpa.domain.Specification;

public interface BranchTypeSpecifications {

    static Specification<BranchType> withSearch(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return builder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            return builder.or(
                    builder.like(builder.lower(root.get("name")), searchPattern),
                    builder.like(builder.lower(root.get("description")), searchPattern)
            );
        };
    }

}