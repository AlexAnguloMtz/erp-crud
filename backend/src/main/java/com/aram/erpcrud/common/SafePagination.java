package com.aram.erpcrud.common;

import org.springframework.stereotype.Component;

@Component
public class SafePagination {

    private static final Integer DEFAULT_PAGE_SIZE = 10;

    public Integer safePageNumber(Integer rawPageNumber) {
        if (rawPageNumber == null || rawPageNumber < 0) {
            return 0;
        }
        return rawPageNumber;
    }

    public Integer safePageSize(Integer rawPageSize) {
        if (rawPageSize == null || rawPageSize < 1) {
            return DEFAULT_PAGE_SIZE;
        }
        return rawPageSize;
    }
}