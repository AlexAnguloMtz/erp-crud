package com.aram.erpcrud.modules.users.application.query;

import com.aram.erpcrud.modules.users.domain.FullUser;
import com.aram.erpcrud.modules.users.domain.UserAccount;
import com.aram.erpcrud.modules.users.domain.UserRole;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public interface FullUserSpecifications {

    static Specification<FullUser> withSearch(String search) {
        return (root, query, builder) -> {
            if (search == null || search.isEmpty()) {
                return builder.conjunction();
            }

            String searchPattern = "%" + search.toLowerCase() + "%";

            Join<FullUser, UserAccount> accountJoin = root.join("account");
            Join<UserAccount, UserRole> roleJoin = accountJoin.join("role");

            return builder.or(
                    builder.like(builder.lower(root.get("name")), searchPattern),
                    builder.like(builder.lower(root.get("lastName")), searchPattern),
                    builder.like(builder.lower(root.get("phone")), searchPattern),
                    builder.like(builder.lower(accountJoin.get("email")), searchPattern),
                    builder.like(builder.lower(roleJoin.get("name")), searchPattern)
            );
        };
    }

    static Specification<FullUser> withAnyRole(List<Long> roleIds) {
        return (root, query, builder) -> {
            if (roleIds == null || roleIds.isEmpty()) {
                return builder.conjunction();
            }
            var accountJoin = root.join("account", JoinType.INNER);
            var roleJoin = accountJoin.join("role", JoinType.INNER);
            return roleJoin.get("id").in(roleIds);
        };
    }
}