package com.aram.erpcrud.products.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@Table(name = "brands")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PACKAGE)
public class Brand {

    @Id
    private String id;

    @Column
    private String name;

}