package com.aram.erpcrud.useraggregation.application;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.useraggregation.payload.FullUserDetails;
import com.aram.erpcrud.useraggregation.payload.GetUsersQuery;
import org.springframework.stereotype.Component;

@Component
public class GetUsersQueryHandler {

    public PageResponse<FullUserDetails> handle(GetUsersQuery query) {
        return null;
    }

}
