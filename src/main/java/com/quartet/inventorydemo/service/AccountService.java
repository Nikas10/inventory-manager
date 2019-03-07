package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Account;

import java.util.List;

public interface AccountService {
    List<Account> getAll();
    Account getByLogin(String login);
    Account add(Account acc);
    Account update(Account acc);
    Account getByEmail(String email);
}