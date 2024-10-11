package com.aram.erpcrud.modules.users.rest;

import com.aram.erpcrud.modules.users.payload.GetUsersQuery;
import com.aram.erpcrud.modules.users.payload.*;
import com.aram.erpcrud.modules.users.application.UserFacade;
import com.aram.erpcrud.utils.PageResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserFacade usersFacade;

    public UserController(UserFacade usersFacade) {
        this.usersFacade = usersFacade;
    }

    @GetMapping
    public ResponseEntity<PageResponse<FullUserDTO>> getUsers(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) @Min(0) Integer pageNumber,
            @RequestParam(required = false) @Min(1) @Max(50) Integer pageSize,
            @RequestParam(required = false) String sort,
            @RequestParam(required = false) List<Long> roles
    ) {
        GetUsersQuery query = new GetUsersQuery(search, pageNumber, pageSize, sort, roles);
        return ResponseEntity.ok(usersFacade.handle(query));
    }

    @GetMapping("/me")
    public ResponseEntity<GetMeResponse> getMe(Principal principal) {
        GetMeResponse response = usersFacade.getMe(principal.getName());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<Void> createUser(@Valid @RequestBody CreateUserCommand command) {
        usersFacade.createUser(command);
        return new ResponseEntity<>(null, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UpdateUserResponse> updateUser(
            @Valid @RequestBody UpdateUserCommand command,
            @PathVariable Long id,
            Principal principal
    ) {
        return new ResponseEntity<>(usersFacade.updateUser(id, principal.getName(), command), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserById(@PathVariable("id") Long id) {
        usersFacade.deleteUserById(id);
        return new ResponseEntity<>(null, HttpStatus.NO_CONTENT);
    }
}