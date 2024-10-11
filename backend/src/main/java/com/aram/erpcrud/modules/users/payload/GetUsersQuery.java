package com.aram.erpcrud.modules.users.payload;

import java.util.List;

public record GetUsersQuery (
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort,
        List<Long> roles
) { }
