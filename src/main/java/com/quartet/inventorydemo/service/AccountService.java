package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Account;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AccountService {

    Collection<Account> getAll();

    Optional<Account> getByAccountId(@NotNull @Valid UUID accountId);

    Optional<Account> getByLogin(@NotBlank @Valid String login);

    Optional<Account> getByEmail(@Email @Valid String email);

    Collection<Account> getByAccountIDs(@NotNull @Valid Set<UUID> uuidSet);

    Account add(@NotNull @Valid Account acc);

    Account update(@NotBlank @Valid String login, @NotNull @Valid Account acc);

    void remove(@NotBlank @Valid String login);
}