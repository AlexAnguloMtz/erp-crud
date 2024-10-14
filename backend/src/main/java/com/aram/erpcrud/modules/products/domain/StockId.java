package com.aram.erpcrud.modules.products.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

import java.io.Serializable;

@Embeddable
@NoArgsConstructor(access = AccessLevel.PACKAGE)
@EqualsAndHashCode
@Getter
@Setter
public class StockId implements Serializable {

    @Column(name = "producto_id")
    private Long productId;

    @Column(name = "sucursal_id")
    private Long branchId;

}