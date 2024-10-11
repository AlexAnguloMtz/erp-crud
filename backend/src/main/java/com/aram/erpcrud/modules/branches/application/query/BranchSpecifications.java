package com.aram.erpcrud.modules.branches.application.query;

import com.aram.erpcrud.modules.branches.domain.Branch;
import com.aram.erpcrud.modules.branches.domain.BranchAddress;
import jakarta.persistence.criteria.Join;
import org.springframework.data.jpa.domain.Specification;

public interface BranchSpecifications {

    static Specification<Branch> withSearch(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return builder.conjunction();
            }

            String searchPattern = "%" + search.toLowerCase() + "%";

            Join<Branch, BranchAddress> addressJoin = root.join("address");

            return builder.or(
                    builder.like(builder.lower(root.get("name")), searchPattern),
                    builder.like(builder.lower(root.get("phone")), searchPattern),
                    builder.like(builder.lower(addressJoin.get("district")), searchPattern),
                    builder.like(builder.lower(addressJoin.get("street")), searchPattern),
                    builder.like(builder.lower(addressJoin.get("streetNumber")), searchPattern),
                    builder.like(builder.lower(addressJoin.get("zipCode")), searchPattern)
            );
        };
    }
}