package com.aram.erpcrud.users.application;

import com.aram.erpcrud.auth.AuthService;
import com.aram.erpcrud.auth.payload.AccountPublicDetails;
import com.aram.erpcrud.common.PageResponse;
import com.aram.erpcrud.common.SafePagination;
import com.aram.erpcrud.users.domain.PersonalDetails;
import com.aram.erpcrud.users.domain.PersonalDetailsRepository;
import com.aram.erpcrud.users.payload.UserPreview;
import com.aram.erpcrud.users.payload.GetUsersQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
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

    public PageResponse<UserPreview> handle(GetUsersQuery query) {
        Page<PersonalDetails> personalDetailsPage = findPage(query);
        List<String> accountIds = getAccountIds(personalDetailsPage.getContent());
        List<AccountPublicDetails> accounts = authService.findAccounts(accountIds);
        List<UserPreview> userPreviews = assembleUserPreviews(accounts, personalDetailsPage.getContent());

        return new PageResponse<>(
            personalDetailsPage.getNumber(),
            personalDetailsPage.getSize(),
            personalDetailsPage.getTotalElements(),
            personalDetailsPage.getTotalPages(),
            personalDetailsPage.isLast(),
            userPreviews
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

        Specification<PersonalDetails> specification = withSearchCriteria(query.search());

        return personalDetailsRepository.findAll(specification, pageable);
    }

    private List<String> getAccountIds(List<PersonalDetails> personalDetails) {
        return personalDetails.stream().map(PersonalDetails::getAccountId).toList();
    }

    private List<UserPreview> assembleUserPreviews(List<AccountPublicDetails> accounts, List<PersonalDetails> personalDetails) {
        List<UserPreview> userPreviews = new ArrayList<>();
        for (PersonalDetails somePersonalDetails : personalDetails) {
            Optional<AccountPublicDetails> accountOptional = findAccountForPersonalDetails(accounts, somePersonalDetails);
            AccountPublicDetails accountPublicDetails = accountOptional.orElse(emptyAccountDetails());
            userPreviews.add(toUserPreview(accountPublicDetails, somePersonalDetails));
        }
        return userPreviews;
    }

    private Specification<PersonalDetails> withSearchCriteria(String search) {
        return (Root<PersonalDetails> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) -> {
            if (search == null || search.isEmpty()) {
                return criteriaBuilder.conjunction();
            }
            String searchPattern = "%" + search.toLowerCase() + "%";
            Predicate firstNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), searchPattern);
            Predicate lastNamePredicate = criteriaBuilder.like(criteriaBuilder.lower(root.get("lastName")), searchPattern);
            return criteriaBuilder.or(firstNamePredicate, lastNamePredicate);
        };
    }

    private UserSort toUserSort(String sort) {
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

    private UserPreview toUserPreview(AccountPublicDetails accountPublicDetails, PersonalDetails personalDetails) {
        return new UserPreview(
                personalDetails.getId(),
                personalDetails.getName(),
                personalDetails.getLastName(),
                personalDetails.getState(),
                personalDetails.getCity(),
                personalDetails.getPhone(),
                accountPublicDetails.email(),
                accountPublicDetails.role()
        );
    }

    private AccountPublicDetails emptyAccountDetails() {
        return new AccountPublicDetails("", "", "");
    }
}