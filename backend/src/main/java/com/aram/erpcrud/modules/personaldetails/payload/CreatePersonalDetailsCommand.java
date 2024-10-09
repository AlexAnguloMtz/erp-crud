package com.aram.erpcrud.modules.personaldetails.payload;

import lombok.Builder;

@Builder
public record CreatePersonalDetailsCommand (
        Long accountId,
        String name,
        String lastName,
        CreateUserAddressCommand address,
        String phone
){
}
