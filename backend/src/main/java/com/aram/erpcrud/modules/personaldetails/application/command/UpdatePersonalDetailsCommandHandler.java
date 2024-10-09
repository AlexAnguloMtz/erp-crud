package com.aram.erpcrud.modules.personaldetails.application.command;

import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalAddress;
import com.aram.erpcrud.modules.personaldetails.payload.AddressDTO;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalDetailsDTO;
import com.aram.erpcrud.modules.personaldetails.payload.UpdatePersonalDetailsCommand;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

@Component
public class UpdatePersonalDetailsCommandHandler {

    private final PersonalDetailsRepository personalDetailsRepository;

    public UpdatePersonalDetailsCommandHandler(PersonalDetailsRepository personalDetailsRepository) {
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public PersonalDetailsDTO handle(UpdatePersonalDetailsCommand command) {
        PersonalDetails personalDetails = personalDetailsRepository.findByAccountId(command.accountId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        PersonalAddress address = personalDetails.getAddress();

        address.setStreet(command.address().street());
        address.setStreetNumber(command.address().streetNumber());
        address.setDistrict(command.address().district());
        address.setZipCode(command.address().zipCode());

        personalDetails.setName(command.name());
        personalDetails.setLastName(command.lastName());
        personalDetails.setPhone(command.phone());

        personalDetailsRepository.save(personalDetails);

        return toPersonalDetailsDTO(personalDetails);
    }

    private PersonalDetailsDTO toPersonalDetailsDTO(PersonalDetails personalDetails) {
        return PersonalDetailsDTO.builder()
                .name(personalDetails.getName())
                .lastName(personalDetails.getLastName())
                .phone(personalDetails.getPhone())
                .address(toAddressDTO(personalDetails.getAddress()))
                .build();
    }

    private AddressDTO toAddressDTO(PersonalAddress address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .district(address.getDistrict())
                .zipCode(address.getZipCode())
                .build();
    }

}
