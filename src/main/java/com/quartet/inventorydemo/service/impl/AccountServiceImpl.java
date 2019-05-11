package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.AccountDTO;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.AccountService;
import com.quartet.inventorydemo.service.HolderService;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("AccountService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class AccountServiceImpl implements AccountService {

  private final AccountRepository accountRepository;
  private final HolderService holderService;

  @Autowired
  public AccountServiceImpl(
      @Qualifier("AccountRepository") final AccountRepository accountRepository,
      @Qualifier("HolderService") final HolderService holderService) {
    this.accountRepository = accountRepository;
    this.holderService = holderService;
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
      throw new ResourceAlreadyExistsException(
          "Account with same login or email already exists. Can not make changes");
    }
    Account newAccount =
        new Account(
            account.getFirstName(),
            account.getMiddleName(),
            account.getLastName(),
            account.getLogin(),
            account.getPassword(),
            account.getRole(),
            account.getEmail());
    return accountRepository.saveAndFlush(newAccount);
  }

  @Override
  public Account update(@NotBlank @Valid String login, @NotNull @Valid AccountDTO accountDTO) {
    // getting existing resource
    Optional<Account> accountOptional = getByLogin(login);
    Account accountToModify =
        accountOptional.orElseThrow(
            () -> new ResourceNotFoundException("Account with login: " + login + " not found"));

    if ((accountDTO.getEmail() != null) &&
        (!accountDTO.getEmail().equals(""))) {
      accountToModify.setEmail(accountDTO.getEmail());
    }

    if ((accountDTO.getFirstName() != null) &&
        (!accountDTO.getFirstName().equals(""))) {
      accountToModify.setFirstName(accountDTO.getFirstName());
    }

    if ((accountDTO.getLastName() != null) &&
        (!accountDTO.getLastName().equals(""))) {
      accountToModify.setLastName(accountDTO.getLastName());
    }

    if ((accountDTO.getMiddleName() != null) &&
        (!accountDTO.getMiddleName().equals(""))) {
      accountToModify.setMiddleName(accountDTO.getMiddleName());
    }

    if ((accountDTO.getPassword() != null) &&
        (!accountDTO.getPassword().equals(""))) {
      accountToModify.setPassword(accountDTO.getPassword());
    }

    if ((accountDTO.getRole() != null) &&
        (!accountDTO.getRole().equals(""))) {
      accountToModify.setRole(accountDTO.getRole());
    }

    return accountRepository.saveAndFlush(accountToModify);
  }

  @Override
  public void remove(@NotBlank @Valid String login) {
    // getting existing resource
    Optional<Account> accountOptional = getByLogin(login);
    Account accountToDelete =
        accountOptional.orElseThrow(
            () -> new ResourceNotFoundException("Account with login: " + login + " not found"));

    accountRepository.delete(accountToDelete);
  }

  @Override
  public Account addHolders(@NotBlank @Valid String login, @NotNull @Valid Set<UUID> holderIds) {
    Optional<Account> accountOptional = getByLogin(login);
    Account accountWithHolders =
        accountOptional.orElseThrow(
            () -> new ResourceNotFoundException("Account with login: " + login + " not found"));


    Set<Holder> accountHolders = accountWithHolders.getHolders();
    Collection<Holder> holdersToAdd = holderService.getByHolderIDs(holderIds);

    if (holdersToAdd.isEmpty()) {
        throw new ResourceNotFoundException("No holders with specified ids.");
    }
    checkHolderExistence(accountHolders, holdersToAdd);

    accountHolders.addAll(holdersToAdd);

    return accountRepository.saveAndFlush(accountWithHolders);
  }

  @Override
  public Account removeHolders(@NotBlank @Valid String login, @NotNull @Valid Set<UUID> holderIds) {
    Optional<Account> accountOptional = getByLogin(login);
    Account accountWithHolders =
        accountOptional.orElseThrow(
            () -> new ResourceNotFoundException("Account with login: " + login + " not found"));

    Set<Holder> currentHolders = accountWithHolders.getHolders();
    currentHolders.removeAll(holderService.getByHolderIDs(holderIds));

    return accountRepository.saveAndFlush(accountWithHolders);
  }

  private boolean isExists(@NotNull @Valid Account account) {
    ExampleMatcher uniqueMatcher =
        ExampleMatcher.matchingAny()
            .withMatcher("email", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
            .withMatcher("login", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
            .withIgnorePaths(
                "id",
                "firstName",
                "middleName",
                "lastName",
                "password",
                "role",
                "holders",
                "requisitions");
    Example<Account> accountExample = Example.of(account, uniqueMatcher);
    Optional<Account> alreadyExists = accountRepository.findOne(accountExample);

    return alreadyExists.isPresent();
  }

  private void checkHolderExistence(Set<Holder> accountHolders, Collection<Holder> holdersToAdd) {
    for (Holder currentHolder: holdersToAdd) {
      if (accountHolders.contains(currentHolder)) {
        throw new ResourceAlreadyExistsException("Holder with id: " + currentHolder.getId() + " already exists at specified account.");
      }
    }
  }
}
