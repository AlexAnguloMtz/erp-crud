package com.aram.erpcrud.modules.useraggregator.payload;

import java.util.List;

public record GetUsersQuery (
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort,
        List<Long> roles
) { }
