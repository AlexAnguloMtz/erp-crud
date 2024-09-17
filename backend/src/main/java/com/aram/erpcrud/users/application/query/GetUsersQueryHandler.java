package com.aram.erpcrud.users.application.query;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.common.SafePagination;
import com.aram.erpcrud.locations.LocationsService;
import com.aram.erpcrud.locations.payload.StateDTO;
import com.aram.erpcrud.users.domain.UserAddress;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.AddressDTO;
import com.aram.erpcrud.users.payload.FullUserDetails;
import com.aram.erpcrud.users.payload.GetUsersQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
public class GetUsersQueryHandler {

    private final AuthService authService;
    private final LocationsService locationsService;
    private final PersonalDetailsRepository personalDetailsRepository;
    private final SafePagination safePagination;

    enum UserSort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC),
        LastNameAsc("lastName", Sort.Direction.ASC),
        LastNameDesc("lastName", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        UserSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    public GetUsersQueryHandler(
            AuthService authService,
            LocationsService locationsService,
            PersonalDetailsRepository personalDetailsRepository,
            SafePagination safePagination
    ) {
        this.authService = authService;
        this.locationsService = locationsService;
        this.personalDetailsRepository = personalDetailsRepository;
        this.safePagination = safePagination;
    }

    public PageResponse<FullUserDetails> handle(GetUsersQuery query) {
        // Find personal details
        Page<PersonalDetails> personalDetailsPage = findPage(query);

        // Find accounts
        Set<UUID> accountIds = getAccountIds(personalDetailsPage.getContent());
        List<AccountPublicDetails> accounts = authService.findAccounts(accountIds);

        // Find states
        Set<String> stateIds = getStateIds(personalDetailsPage.getContent());
        List<StateDTO> stateDtos = locationsService.findStates(stateIds);

        // Assemble users
        List<FullUserDetails> users = assembleUsers(accounts, stateDtos, personalDetailsPage.getContent());

        return toPageResponse(personalDetailsPage, users);
    }

    private PageResponse<FullUserDetails> toPageResponse(
            Page<PersonalDetails> personalDetailsPage,
            List<FullUserDetails> users
    ) {
        return new PageResponse<>(
                personalDetailsPage.getNumber(),
                personalDetailsPage.getSize(),
                personalDetailsPage.getTotalElements(),
                personalDetailsPage.getTotalPages(),
                personalDetailsPage.isLast(),
                users
        );
    }

    private Set<String> getStateIds(List<PersonalDetails> personalDetails) {
        return personalDetails.stream()
                .map(PersonalDetails::getAddress)
                .map(UserAddress::getStateId)
                .collect(Collectors.toSet());
    }

    private Page<PersonalDetails> findPage(GetUsersQuery query) {
        UserSort userSort = toUserSort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(userSort.field).with(userSort.direction));

        Pageable pageable = PageRequest.of(
            safePagination.safePageNumber(query.pageNumber()),
            safePagination.safePageSize(query.pageSize()),
            sort
        );

        Specification<PersonalDetails> specification = PersonalDetailsSpecifications.withSearch(query.search())
                .and(PersonalDetailsSpecifications.livesInAnyState(query.states()));

        return personalDetailsRepository.findAll(specification, pageable);
    }

    private Set<UUID> getAccountIds(List<PersonalDetails> personalDetails) {
        return personalDetails.stream()
                .map(PersonalDetails::getAccountId)
                .collect(Collectors.toSet());
    }

    private List<FullUserDetails> assembleUsers(
            List<AccountPublicDetails> accounts,
            List<StateDTO> stateDtos,
            List<PersonalDetails> personalDetails
    ) {
        List<FullUserDetails> users = new ArrayList<>();
        for (PersonalDetails somePersonalDetails : personalDetails) {
            // Find account
            Optional<AccountPublicDetails> accountOptional = findAccountForPersonalDetails(accounts, somePersonalDetails);
            AccountPublicDetails accountPublicDetails = accountOptional.orElse(emptyAccountDetails());

            // Find state
            Optional<StateDTO> stateDtoOptional = findStateById(stateDtos, somePersonalDetails.getAddress().getStateId());
            StateDTO stateDto = stateDtoOptional.orElse(emptyStateDto());

            // Assemble user
            FullUserDetails user = toFullUserDetails(accountPublicDetails, somePersonalDetails, stateDto);

            users.add(user);
        }
        return users;
    }

    private Optional<StateDTO> findStateById(List<StateDTO> states, String stateId) {
        return states.stream()
                .filter(x -> x.id().equals(stateId))
                .findFirst();
    }

    private Optional<AccountPublicDetails> findAccountForPersonalDetails(
            List<AccountPublicDetails> accounts,
            PersonalDetails personalDetails
    ) {
        return accounts.stream()
            .filter(x -> x.id().equals(personalDetails.getAccountId().toString()))
            .findFirst();
    }

    private FullUserDetails toFullUserDetails(
            AccountPublicDetails accountPublicDetails,
            PersonalDetails personalDetails,
            StateDTO stateDto
    ) {
        return new FullUserDetails(
                accountPublicDetails.id(),
                personalDetails.getName(),
                personalDetails.getLastName(),
                toAddressDto(personalDetails.getAddress(), stateDto),
                personalDetails.getPhone(),
                accountPublicDetails.email(),
                accountPublicDetails.rolePublicDetails()
        );
    }

    private AddressDTO toAddressDto(UserAddress address, StateDTO stateDto) {
        return new AddressDTO(
                address.getId().toString(),
                stateDto,
                address.getCity(),
                address.getDistrict(),
                address.getStreet(),
                address.getStreetNumber(),
                address.getZipCode()
        );
    }

    private StateDTO emptyStateDto() {
        return new StateDTO("", "");
    }

    private AccountPublicDetails emptyAccountDetails() {
        return new AccountPublicDetails("N/A", "N/A", new RolePublicDetails("N/A", "N/A"));
    }

    private UserSort toUserSort(String sort) {
        UserSort defaultSort = UserSort.NameAsc;

        if (sort == null) {
            return defaultSort;
        }

        return switch (sort) {
            case "name-desc" -> UserSort.NameDesc;
            case "lastName-asc" -> UserSort.LastNameAsc;
            case "lastName-desc" -> UserSort.LastNameDesc;
            default -> defaultSort;
        };
    }

}