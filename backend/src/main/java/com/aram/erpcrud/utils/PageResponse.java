package com.aram.erpcrud.utils;

public record PageResponse<T>(
        Integer pageNumber,
        Integer pageSize,
        Long totalItems,
        Integer totalPages,
        boolean isLastPage,
        Iterable<T> items
) { }