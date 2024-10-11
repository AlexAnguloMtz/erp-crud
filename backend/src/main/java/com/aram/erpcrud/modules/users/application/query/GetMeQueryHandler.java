package com.aram.erpcrud.modules.users.application.query;

import com.aram.erpcrud.modules.authorization.AuthService;
import com.aram.erpcrud.modules.authorization.payload.AccountPublicDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import com.aram.erpcrud.modules.users.payload.GetMeResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class GetMeQueryHandler {

    private final AuthService authService;
    private final PersonalDetailsRepository personalDetailsRepository;

    public GetMeQueryHandler(
            AuthService authService,
            PersonalDetailsRepository personalDetailsRepository
    ) {
        this.authService = authService;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public GetMeResponse handle(String email) {
        AccountPublicDetails account = authService.findAccountByEmail(email);
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(account.id());
        if (personalDetailsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "could not find personal details for this account");
        }
        return toGetMeResponse(personalDetailsOptional.get());
    }

    private GetMeResponse toGetMeResponse(PersonalDetails personalDetails) {
        return new GetMeResponse(personalDetails.getName());
    }
}