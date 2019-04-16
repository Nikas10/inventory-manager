package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Account;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AccountService {
    List<Account> getAll();

    Set<Account> getByAccountIDs(Set<UUID> uuidSet);

    Account getByLogin(String login);
    Account add(Account acc);
    Account update(Account acc);
    Account getByEmail(String email);
}