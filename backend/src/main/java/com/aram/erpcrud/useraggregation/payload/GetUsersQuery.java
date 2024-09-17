package com.aram.erpcrud.useraggregation.payload;

import java.util.List;

public record GetUsersQuery (
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort,
        List<String> states,
        List<String> roles
) { }
