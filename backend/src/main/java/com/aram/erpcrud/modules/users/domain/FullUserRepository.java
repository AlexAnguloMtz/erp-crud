package com.aram.erpcrud.modules.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface FullUserRepository extends JpaRepository<FullUser, Long>, JpaSpecificationExecutor<FullUser> {
}
