package com.aram.erpcrud.modules.users.application.mapper;

import com.aram.erpcrud.modules.personaldetails.payload.CreatePersonalDetailsCommand;
import com.aram.erpcrud.modules.personaldetails.payload.CreateUserAddressCommand;
import com.aram.erpcrud.modules.users.payload.CreateUserCommand;
import org.springframework.stereotype.Component;

@Component("users.personal-details-model-mapper")
public class PersonalDetailsModelMapper {

    public CreatePersonalDetailsCommand toPersonalDetailsCreationCommand(Long accountId, CreateUserCommand command) {
        return CreatePersonalDetailsCommand.builder()
                .accountId(accountId)
                .name(command.name())
                .lastName(command.lastName())
                .address(makeUserAddressCommand(command))
                .phone(command.phone())
                .build();
    }

    private CreateUserAddressCommand makeUserAddressCommand(CreateUserCommand command) {
        return CreateUserAddressCommand.builder()
                .district(command.district())
                .street(command.street())
                .streetNumber(command.streetNumber())
                .zipCode(command.zipCode())
                .build();
    }

}
