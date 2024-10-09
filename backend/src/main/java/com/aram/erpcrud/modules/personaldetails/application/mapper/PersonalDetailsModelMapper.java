package com.aram.erpcrud.modules.personaldetails.application.mapper;

import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalAddress;
import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.personaldetails.payload.CreateUserAddressCommand;
import org.springframework.stereotype.Component;

@Component("personal-details.personal-details-model-mapper")
public class PersonalDetailsModelMapper {

    public PersonalDetails toPersonalDetails(CreatePersonalDetailsCommand command) {
        return PersonalDetails.builder()
                .accountId(command.accountId())
                .name(command.name())
                .lastName(command.lastName())
                .phone(command.phone())
                .address(toAddress(command.address()))
                .build();
    }

    private PersonalAddress toAddress(CreateUserAddressCommand command) {
        return PersonalAddress.builder()
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .zipCode(command.zipCode())
                .build();
    }

}
