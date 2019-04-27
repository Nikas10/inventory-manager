package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface AccountService {

    Collection<Account> getAll();

    Account getByAccountId(@NotNull @Valid UUID accountId);

    Account getByLogin(@NotBlank @Valid String login);

    Account getByEmail(@Email @Valid String email);

    Collection<Account> getByAccountIDs(@NotNull @Valid Set<UUID> uuidSet);

    @Validated(OnCreate.class)
    Account add(@NotNull @Valid Account acc);

    @Validated(OnUpdate.class)
    Account update(@NotNull @Valid Account acc);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid Account account);
}