package com.aram.erpcrud.modules.personaldetails.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface PersonalDetailsRepository extends JpaRepository<PersonalDetails, Long>, JpaSpecificationExecutor<PersonalDetails> {

    Optional<PersonalDetails> findByAccountId(Long id);

    void deleteByAccountId(Long id);

}