package com.aram.erpcrud.modules.personaldetails.payload;

import lombok.Builder;

@Builder
public record UpdatePersonalDetailsCommand(
        Long accountId,
        String name,
        String lastName,
        String phone,
        UpdateAddressCommand address
) {
}
