package com.aram.erpcrud.products.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "product")
@Getter
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@AllArgsConstructor
public class Product {

    @Id
    private String id;

    @Column
    private String name;

    @ManyToOne
    private Brand brand;

    @ManyToOne
    private ProductCategory productCategory;

}