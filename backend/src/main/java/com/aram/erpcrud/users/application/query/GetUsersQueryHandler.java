package com.aram.erpcrud.users.application.query;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.auth.payload.RolePublicDetails;
import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.common.SafePagination;
import com.aram.erpcrud.locations.domain.State;
import com.aram.erpcrud.locations.payload.StateDTO;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.FullUserDetails;
import com.aram.erpcrud.users.payload.GetUsersQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class GetUsersQueryHandler {

    private final AuthService authService;
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
            PersonalDetailsRepository personalDetailsRepository,
            SafePagination safePagination
    ) {
        this.authService = authService;
        this.personalDetailsRepository = personalDetailsRepository;
        this.safePagination = safePagination;
    }

    public PageResponse<FullUserDetails> handle(GetUsersQuery query) {
        Page<PersonalDetails> personalDetailsPage = findPage(query);
        List<String> accountIds = getAccountIds(personalDetailsPage.getContent());
        List<AccountPublicDetails> accounts = authService.findAccounts(accountIds);
        List<FullUserDetails> users = assembleUserPreviews(accounts, personalDetailsPage.getContent());

        return new PageResponse<>(
            personalDetailsPage.getNumber(),
            personalDetailsPage.getSize(),
            personalDetailsPage.getTotalElements(),
            personalDetailsPage.getTotalPages(),
            personalDetailsPage.isLast(),
            users
        );
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

    private List<String> getAccountIds(List<PersonalDetails> personalDetails) {
        return personalDetails.stream().map(PersonalDetails::getAccountId).toList();
    }

    private List<FullUserDetails> assembleUserPreviews(List<AccountPublicDetails> accounts, List<PersonalDetails> personalDetails) {
        List<FullUserDetails> userPreviews = new ArrayList<>();
        for (PersonalDetails somePersonalDetails : personalDetails) {
            Optional<AccountPublicDetails> accountOptional = findAccountForPersonalDetails(accounts, somePersonalDetails);
            AccountPublicDetails accountPublicDetails = accountOptional.orElse(emptyAccountDetails());
            userPreviews.add(toUserPreview(accountPublicDetails, somePersonalDetails));
        }
        return userPreviews;
    }

    private UserSort toUserSort(String sort) {
        if (sort == null) {
            return UserSort.NameAsc;
        }

        return switch (sort) {
            case "name-desc" -> UserSort.NameDesc;
            case "lastName-asc" -> UserSort.LastNameAsc;
            case "lastName-desc" -> UserSort.LastNameDesc;
            default -> UserSort.NameAsc;
        };
    }

    private Optional<AccountPublicDetails> findAccountForPersonalDetails(List<AccountPublicDetails> accounts, PersonalDetails personalDetails) {
        return accounts.stream()
            .filter(x -> x.id().equals(personalDetails.getAccountId()))
            .findFirst();
    }

    private FullUserDetails toUserPreview(AccountPublicDetails accountPublicDetails, PersonalDetails personalDetails) {
        return new FullUserDetails(
                accountPublicDetails.id(),
                personalDetails.getName(),
                personalDetails.getLastName(),
                toDto(personalDetails.getState()),
                personalDetails.getCity(),
                personalDetails.getDistrict(),
                personalDetails.getStreet(),
                personalDetails.getStreetNumber(),
                personalDetails.getZipCode(),
                personalDetails.getPhone(),
                accountPublicDetails.email(),
                accountPublicDetails.rolePublicDetails()
        );
    }

    private AccountPublicDetails emptyAccountDetails() {
        return new AccountPublicDetails("N/A", "N/A", new RolePublicDetails("N/A", "N/A"));
    }

    private StateDTO toDto(State state) {
        return new StateDTO(state.getId(), state.getName());
    }
}