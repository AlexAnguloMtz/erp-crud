package com.aram.erpcrud.personaldetails.application.query;

import com.aram.erpcrud.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.personaldetails.domain.PersonalDetailsRepository;
import com.aram.erpcrud.personaldetails.payload.PersonalNameDTO;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class GetPersonalNameByAccountIdQueryHandler {

    private final PersonalDetailsRepository personalDetailsRepository;

    public GetPersonalNameByAccountIdQueryHandler(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public PersonalNameDTO handle(String accountId) {
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(accountId);
        if (personalDetailsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return toPersonalNameDTO(personalDetailsOptional.get());
    }

    public PersonalNameDTO toPersonalNameDTO(PersonalDetails personalDetails) {
        return new PersonalNameDTO(
                personalDetails.getAccountId(),
                personalDetails.getName(),
                personalDetails.getLastName()
        );
    }
}