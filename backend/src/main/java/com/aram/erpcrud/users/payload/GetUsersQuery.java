package com.aram.erpcrud.users.payload;

public record GetUsersQuery (
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) { }
