package com.aram.erpcrud.personaldetails.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, String>, JpaSpecificationExecutor<PersonalDetails> {
    Optional<PersonalDetails> findByAccountId(String accountId);
    void deleteByAccountId(String id);
}