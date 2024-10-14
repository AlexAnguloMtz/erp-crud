package com.aram.erpcrud.modules.products.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "stock")
public class Stock {

    @EmbeddedId
    private StockId id;

    @Column(name = "cantidad")
    private Integer quantity;

}