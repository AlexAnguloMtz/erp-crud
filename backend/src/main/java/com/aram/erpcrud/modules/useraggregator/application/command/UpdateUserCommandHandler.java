package com.aram.erpcrud.modules.useraggregator.application.command;

import com.aram.erpcrud.modules.auth.AuthService;
import com.aram.erpcrud.modules.auth.payload.RolePublicDetails;
import com.aram.erpcrud.modules.auth.payload.UpdateAccountCommand;
import com.aram.erpcrud.modules.auth.payload.UpdateAccountResponse;
import com.aram.erpcrud.modules.personaldetails.PersonalDetailsService;
import com.aram.erpcrud.modules.personaldetails.payload.PersonalDetailsDTO;
import com.aram.erpcrud.modules.personaldetails.payload.UpdateAddressCommand;
import com.aram.erpcrud.modules.personaldetails.payload.UpdatePersonalDetailsCommand;
import com.aram.erpcrud.modules.useraggregator.payload.FullUserDTO;
import com.aram.erpcrud.modules.useraggregator.payload.RoleDTO;
import com.aram.erpcrud.modules.useraggregator.payload.UpdateUserCommand;
import com.aram.erpcrud.modules.useraggregator.payload.UpdateUserResponse;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class UpdateUserCommandHandler {

    private final AuthService authService;
    private final PersonalDetailsService personalDetailsService;

    public UpdateUserCommandHandler(
            AuthService authService,
            PersonalDetailsService personalDetailsService
    ) {
        this.authService = authService;
        this.personalDetailsService = personalDetailsService;
    }

    @Transactional
    public UpdateUserResponse handle(
            Long id,
            String requestingUserEmail,
            UpdateUserCommand command
    ) {
        UpdateAccountCommand updateAccountCommand = toUpdateAccountCommand(id, requestingUserEmail, command);
        UpdateAccountResponse updateAccountResponse = authService.updateAccount(updateAccountCommand);

        UpdatePersonalDetailsCommand updatePersonalDetailsCommand = toUpdatePersonalDetailsCommand(id, command);
        PersonalDetailsDTO personalDetailsDTO = personalDetailsService.updatePersonalDetails(updatePersonalDetailsCommand);

        FullUserDTO fullUserDTO = toFullUserDTO(id, updateAccountResponse, personalDetailsDTO);

        return new UpdateUserResponse(fullUserDTO, updateAccountResponse.jwt());
    }

    private FullUserDTO toFullUserDTO(
            Long id,
            UpdateAccountResponse updateAccountResponse,
            PersonalDetailsDTO personalDetailsDTO
    ) {
        return FullUserDTO.builder()
                .id(id)
                .name(personalDetailsDTO.name())
                .lastName(personalDetailsDTO.lastName())
                .address(personalDetailsDTO.address())
                .phone(personalDetailsDTO.phone())
                .email(updateAccountResponse.email())
                .role(toAddressDTO(updateAccountResponse.rolePublicDetails()))
                .build();
    }

    private RoleDTO toAddressDTO(RolePublicDetails rolePublicDetails) {
        return new RoleDTO(rolePublicDetails.id(), rolePublicDetails.name());
    }

    private UpdatePersonalDetailsCommand toUpdatePersonalDetailsCommand(
            Long accountId,
            UpdateUserCommand command
    ) {
        return UpdatePersonalDetailsCommand.builder()
                .accountId(accountId)
                .name(command.name())
                .lastName(command.lastName())
                .phone(command.phone())
                .address(makeAddressDTO(command))
                .build();
    }

    private UpdateAddressCommand makeAddressDTO(UpdateUserCommand command) {
        return UpdateAddressCommand.builder()
                .street(command.street())
                .streetNumber(command.streetNumber())
                .district(command.district())
                .zipCode(command.zipCode())
                .build();
    }

    private UpdateAccountCommand toUpdateAccountCommand(
            Long id,
            String requestingUserEmail,
            UpdateUserCommand command
    ) {
        return new UpdateAccountCommand(
            id,
            command.roleId(),
            command.email(),
            requestingUserEmail
        );
    }
}