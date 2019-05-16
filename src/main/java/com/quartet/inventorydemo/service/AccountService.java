package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.AccountDTO;
import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.model.InventoryPosition;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface AccountService {

  Collection<Account> getAll();

  Optional<Account> getByAccountId(@NotNull @Valid UUID accountId);

  Optional<Account> getByLogin(@NotBlank @Valid String login);

  Optional<Account> getByEmail(@Email @Valid String email);

  Collection<Account> getByAccountIDs(@NotNull @Valid Set<UUID> uuidSet);

  Account add(@NotNull @Valid Account acc);

  Account update(@NotBlank @Valid String login, @NotNull @Valid AccountDTO acccountDTO);

  void remove(@NotBlank @Valid String login);

  Account addHolder(@NotBlank @Valid String login, @NotNull @Valid UUID holderId);

  Account removeHolder(@NotBlank @Valid String login, @NotNull @Valid UUID holderId);

  Set<InventoryPosition> getAvailablePositions(@NotNull @Valid UUID accountId);
}
