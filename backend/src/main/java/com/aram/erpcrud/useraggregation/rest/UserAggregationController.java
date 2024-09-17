package com.aram.erpcrud.useraggregation.rest;

import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.useraggregation.application.GetUsersQueryHandler;
import com.aram.erpcrud.useraggregation.payload.FullUserDetails;
import com.aram.erpcrud.useraggregation.payload.GetUsersQuery;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users-aggregation")
public class UserAggregationController {

    private final GetUsersQueryHandler getUsersQueryHandler;

    public UserAggregationController(GetUsersQueryHandler getUsersQueryHandler) {
        this.getUsersQueryHandler = getUsersQueryHandler;
    }

    @GetMapping
    public ResponseEntity<PageResponse<FullUserDetails>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) List<String> states,
            @RequestParam(required = false) List<String> roles
    ) {
        GetUsersQuery query = new GetUsersQuery(search, pageNumber, pageSize, sort, states, roles);
        return ResponseEntity.ok(getUsersQueryHandler.handle(query));
    }

}