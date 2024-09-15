package com.aram.erpcrud.personaldetails.payload;

public record GetUsersQuery (
        String search,
        Integer pageNumber,
        Integer pageSize,
        String sort
) { }
