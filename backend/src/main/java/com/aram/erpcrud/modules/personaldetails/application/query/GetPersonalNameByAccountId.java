package com.aram.erpcrud.modules.personaldetails.application.query;

import com.aram.erpcrud.modules.personaldetails.payload.PersonalNameDTO;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Component
public class GetPersonalNameByAccountId {

    private final PersonalDetailsRepository personalDetailsRepository;

    public GetPersonalNameByAccountId(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public PersonalNameDTO handle(Long accountId) {
        Optional<PersonalDetails> personalDetailsOptional = personalDetailsRepository.findByAccountId(accountId);
        if (personalDetailsOptional.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
        return toPersonalNameDTO(personalDetailsOptional.get());
    }

    private PersonalNameDTO toPersonalNameDTO(PersonalDetails personalDetails) {
        return new PersonalNameDTO(
                personalDetails.getAccountId().toString(),
                personalDetails.getName(),
                personalDetails.getLastName()
        );
    }
}