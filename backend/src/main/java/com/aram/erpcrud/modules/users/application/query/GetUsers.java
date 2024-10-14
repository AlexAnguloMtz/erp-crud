package com.aram.erpcrud.modules.users.application.query;

import com.aram.erpcrud.modules.personaldetails.payload.AddressDTO;
import com.aram.erpcrud.modules.users.domain.UserRole;
import com.aram.erpcrud.modules.users.domain.FullUser;
import com.aram.erpcrud.modules.users.domain.FullUserRepository;
import com.aram.erpcrud.modules.users.domain.UserAddress;
import com.aram.erpcrud.modules.users.payload.FullUserDTO;
import com.aram.erpcrud.modules.users.payload.GetUsersQuery;
import com.aram.erpcrud.modules.users.payload.RoleDTO;
import com.aram.erpcrud.utils.PageResponse;
import com.aram.erpcrud.utils.SafePagination;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@Component
public class GetUsers {

    private final SafePagination safePagination;
    private final FullUserRepository fullUserRepository;

    public GetUsers(
            SafePagination safePagination,
            FullUserRepository fullUserRepository
    ) {
        this.safePagination = safePagination;
        this.fullUserRepository = fullUserRepository;
    }

    enum FullUserSort {
        NameAsc("name", Sort.Direction.ASC),
        NameDesc("name", Sort.Direction.DESC),
        LastNameAsc("lastName", Sort.Direction.ASC),
        LastNameDesc("lastName", Sort.Direction.DESC),
        RoleAsc("account.role.name", Sort.Direction.ASC),
        RoleDesc("account.role.name", Sort.Direction.DESC),
        EmailAsc("account.email", Sort.Direction.ASC),
        EmailDesc("account.email", Sort.Direction.DESC),
        PhoneAsc("phone", Sort.Direction.ASC),
        PhoneDesc("phone", Sort.Direction.DESC);

        private final String field;
        private final Sort.Direction direction;

        FullUserSort(String field, Sort.Direction direction) {
            this.field = field;
            this.direction = direction;
        }
    }

    public PageResponse<FullUserDTO> handle(GetUsersQuery query) {
        FullUserSort fullUserSort = toFullUserSort(query.sort());

        Sort sort = Sort.by(Sort.Order.by(fullUserSort.field).with(fullUserSort.direction));

        Pageable pageable = PageRequest.of(
                safePagination.safePageNumber(query.pageNumber()),
                safePagination.safePageSize(query.pageSize()),
                sort
        );

        Specification<FullUser> specification = FullUserSpecifications.withSearch(query.search())
                .and(FullUserSpecifications.withAnyRole(query.roles()));

        Page<FullUser> page = fullUserRepository.findAll(specification, pageable);

        return new PageResponse<>(
                page.getNumber(),
                page.getSize(),
                page.getTotalElements(),
                page.getTotalPages(),
                page.isLast(),
                page.stream().map(this::toFullUserDTO).toList()
        );
    }

    private FullUserSort toFullUserSort(String sort) {
        if ("name-desc".equals(sort)) {
            return FullUserSort.NameDesc;
        }
        if ("lastName-asc".equals(sort)) {
            return FullUserSort.LastNameAsc;
        }
        if ("lastName-desc".equals(sort)) {
            return FullUserSort.LastNameDesc;
        }
        if ("role-asc".equals(sort)) {
            return FullUserSort.RoleAsc;
        }
        if ("role-desc".equals(sort)) {
            return FullUserSort.RoleDesc;
        }
        if ("email-asc".equals(sort)) {
            return FullUserSort.EmailAsc;
        }
        if ("email-desc".equals(sort)) {
            return FullUserSort.EmailDesc;
        }
        if ("phone-asc".equals(sort)) {
            return FullUserSort.PhoneAsc;
        }
        if ("phone-desc".equals(sort)) {
            return FullUserSort.PhoneDesc;
        }
        return FullUserSort.NameAsc;
    }

    private FullUserDTO toFullUserDTO(FullUser fullUser) {
        return FullUserDTO.builder()
                .id(fullUser.getId())
                .role(toRoleDTO(fullUser.getAccount().getRole()))
                .email(fullUser.getAccount().getEmail())
                .address(toAddressDTO(fullUser.getAddress()))
                .name(fullUser.getName())
                .lastName(fullUser.getLastName())
                .phone(fullUser.getPhone())
                .build();
    }

    private RoleDTO toRoleDTO(UserRole role) {
        return new RoleDTO(role.getId(), role.getName());
    }

    private AddressDTO toAddressDTO(UserAddress address) {
        return AddressDTO.builder()
                .id(address.getId())
                .street(address.getStreet())
                .streetNumber(address.getStreetNumber())
                .district(address.getDistrict())
                .zipCode(address.getZipCode())
                .build();
    }

}