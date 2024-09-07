package com.aram.erpcrud.users.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, String> {
    Optional<PersonalDetails> findByAccountId(String accountId);
}