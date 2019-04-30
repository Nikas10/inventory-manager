package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.AccountService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
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
    public Optional<Account> getByAccountId(@NotNull @Valid UUID accountId) {
        return accountRepository.findById(accountId);
    }

    @Override
    public Optional<Account> getByLogin(@NotBlank @Valid String login) {
        return accountRepository.findByLogin(login);
    }

    @Override
    public Optional<Account> getByEmail(@Email @Valid String email) {
        return accountRepository.findByEmail(email);
    }

    @Override
    public Set<Account> getByAccountIDs(@NotNull @Valid Set<UUID> uuidSet) {
        return accountRepository.findByIdIn(uuidSet);
    }

    @Override
    public Account add(@NotNull @Valid Account account) {
        if (isExists(account)) {
            throw new ResourceAlreadyExistsException("Account with same login or email already exists. Can not make changes");
        }
        Account newAccount = new Account(account.getFirstName(), account.getMiddleName(), account.getLastName(),
                account.getLogin(), account.getPassword(), account.getRole(), account.getEmail());
        return accountRepository.saveAndFlush(newAccount);
    }

    @Override
    public Account update(@NotBlank @Valid String login, @NotNull @Valid Account account) {
        //getting existing resource
        Optional<Account> accountOptional = getByLogin(login);
        accountOptional.orElseThrow(() -> new ResourceNotFoundException("Account with login: " + login + " not found"));

        //checking if changes may lead to unnecessary exceptions
        if (isExists(account)) {
            throw new ResourceAlreadyExistsException("Account with same login or email already exists. Can not make changes");
        }

        //changing
        accountOptional.ifPresent(e -> BeanUtils.copyProperties(account, e, "id", "holders", "requisitions"));
        Account modifiedAccount = accountOptional.get();

        return accountRepository.saveAndFlush(modifiedAccount);
    }

    @Override
    public void remove(@NotBlank @Valid String login) {
        //getting existing resource
        Optional<Account> accountOptional = getByLogin(login);
        accountOptional.orElseThrow(() -> new ResourceNotFoundException("Account with login: " + login + " not found"));

        accountRepository.delete(accountOptional.get());
    }

    private boolean isExists(@NotNull @Valid Account account) {
        ExampleMatcher uniqueMatcher = ExampleMatcher.matchingAny()
                .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id", "firstName", "middleName", "lastName", "password", "role", "holders", "requisitions");
        Example<Account> accountExample = Example.of(account, uniqueMatcher);
        Optional<Account> alreadyExists = accountRepository.findOne(accountExample);

        return alreadyExists.isPresent();
    }
}