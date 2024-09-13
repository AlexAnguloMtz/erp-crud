package com.aram.erpcrud.products.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BrandRepository extends JpaRepository<Brand, String>, JpaSpecificationExecutor<Brand> {
}
