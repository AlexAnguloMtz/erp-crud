package com.aram.erpcrud.seeder;

import com.aram.erpcrud.modules.auth.domain.AuthRole;
import com.aram.erpcrud.modules.auth.domain.AuthRoleRepository;
import com.aram.erpcrud.modules.auth.domain.Account;
import com.aram.erpcrud.modules.auth.domain.AccountRepository;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalAddress;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetails;
import com.aram.erpcrud.modules.personaldetails.domain.PersonalDetailsRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class RootUserSeeder {

    private final String rootUserEmail;
    private final String rootUserPassword;
    private final PasswordEncoder passwordEncoder;
    private final AccountRepository accountRepository;
    private final AuthRoleRepository authRoleRepository;
    private final PersonalDetailsRepository personalDetailsRepository;

    public RootUserSeeder(
            @Value("${config.root-user.email}") String rootUserEmail,
            @Value("${config.root-user.password}") String rootUserPassword,
            PasswordEncoder passwordEncoder,
            AccountRepository accountRepository,
            AuthRoleRepository authRoleRepository,
            PersonalDetailsRepository personalDetailsRepository
    ) {
        this.rootUserEmail = rootUserEmail;
        this.rootUserPassword = rootUserPassword;
        this.passwordEncoder = passwordEncoder;
        this.accountRepository = accountRepository;
        this.authRoleRepository = authRoleRepository;
        this.personalDetailsRepository = personalDetailsRepository;
    }

    public void seed() {
        AuthRole superUserRole = authRoleRepository.findByCanonicalName("super_user")
                .orElseThrow(() -> new RuntimeException("Could not find root user role"));

        if (accountRepository.countByRole(superUserRole) == 0) {
            Account rootUserAccount = accountRepository.save(rootUserAccount(superUserRole));
            PersonalDetails rootUserPersonalDetails = createRootUserPersonalDetails(rootUserAccount.getId());
            personalDetailsRepository.save(rootUserPersonalDetails);
        }
    }

    private Account rootUserAccount(AuthRole superUserRole) {
        Account account = new Account();
        account.setRole(superUserRole);
        account.setEmail(rootUserEmail);
        account.setPassword(passwordEncoder.encode(rootUserPassword));
        return account;
    }

    private PersonalDetails createRootUserPersonalDetails(Long accountId) {
        PersonalAddress address = PersonalAddress.builder()
                .district("Colonia")
                .street("Calle")
                .streetNumber("Num Calle")
                .zipCode("00000")
                .build();

        return PersonalDetails.builder()
                .accountId(accountId)
                .name("Usuario Raiz")
                .lastName("Apellido")
                .address(address)
                .phone("0000000000")
                .build();
    }
}