package com.aram.erpcrud.modules.authorization.application.command;

import com.aram.erpcrud.modules.authorization.domain.AccountRepository;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class DeleteAccountByIdCommandHandler {

    private final AccountRepository accountRepository;

    public DeleteAccountByIdCommandHandler(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional
    public void handle(Long id) {
        this.accountRepository.deleteById(id);
    }
}