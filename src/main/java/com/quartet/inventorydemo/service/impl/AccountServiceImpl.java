package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;


@Service("AccountService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    @Autowired
    public AccountServiceImpl(@Qualifier("AccountRepository") final AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Set<Account> getAll() {
        return accountRepository.findAllToSet();
    }

    @Override
    public Account getByAccountId(@NotNull @Valid UUID accountId) {
        return null;
    }

    @Override
    public Account getByLogin(@NotBlank @Valid String login) {
        Optional<Account> byLogin = accountRepository.findByLogin(login);
        byLogin.orElseThrow(ResourceNotFoundException::new);
        return byLogin.get();
    }

    @Override
    public Account getByEmail(@Email @Valid String email) {
        Optional<Account> byEmail = accountRepository.findByEmail(email);
        byEmail.orElseThrow(ResourceNotFoundException::new);
        return byEmail.get();
    }

    @Override
    public Set<Account> getByAccountIDs(@NotNull @Valid Set<UUID> uuidSet) {
        return accountRepository.findByIdIn(uuidSet);
    }

    @Validated(OnCreate.class)
    @Override
    public Account add(@NotNull @Valid Account acc) {
        return accountRepository.saveAndFlush(acc);
    }

    @Validated(OnUpdate.class)
    @Override
    public Account update(@NotNull @Valid Account acc) {
        return accountRepository.saveAndFlush(acc);
    }

    @Validated(OnUpdate.class)
    @Override
    public void remove(@NotNull @Valid Account account) {

    }


}