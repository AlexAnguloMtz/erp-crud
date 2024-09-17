package com.aram.erpcrud.users.application.query;

import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.PersonalNameDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Component
public class GetPersonalNameByAccountIdQueryHandler {

    private final PersonalDetailsRepository personalDetailsRepository;

    public GetPersonalNameByAccountIdQueryHandler(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public PersonalNameDTO handle(UUID accountId) {
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(accountId);
        if (personalDetailsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return toPersonalNameDTO(personalDetailsOptional.get());
    }

    public PersonalNameDTO toPersonalNameDTO(PersonalDetails personalDetails) {
        return new PersonalNameDTO(
                personalDetails.getAccountId().toString(),
                personalDetails.getName(),
                personalDetails.getLastName()
        );
    }
}