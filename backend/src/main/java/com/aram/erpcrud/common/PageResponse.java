package com.aram.erpcrud.common;

public record PageResponse<T>(
        Integer pageNumber,
        Integer pageSize,
        Long totalItems,
        Integer totalPages,
        boolean isLastPage,
        Iterable<T> items
) { }