package com.aram.erpcrud.modules.branches.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface BranchTypeRepository extends JpaRepository<BranchType, Long> {

    Optional<BranchType> findByName(String name);

    Collection<BranchType> findAllByOrderByNameAsc();

}