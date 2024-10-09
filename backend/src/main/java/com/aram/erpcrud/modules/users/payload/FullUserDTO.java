package com.aram.erpcrud.modules.users.payload;

import com.aram.erpcrud.modules.personaldetails.payload.AddressDTO;
import lombok.Builder;

@Builder
public record FullUserDTO(
        Long id,
        String name,
        String lastName,
        AddressDTO address,
        String phone,
        String email,
        RoleDTO role
) { }