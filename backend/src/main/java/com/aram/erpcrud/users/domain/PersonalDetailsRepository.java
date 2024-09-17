package com.aram.erpcrud.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;
import java.util.UUID;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, UUID>, JpaSpecificationExecutor<PersonalDetails> {
    Optional<PersonalDetails> findByAccountId(UUID id);
    void deleteByAccountId(UUID id);
}