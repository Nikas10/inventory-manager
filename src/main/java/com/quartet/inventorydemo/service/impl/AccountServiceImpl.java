package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;


@Service("AccountService")
@Transactional
public class AccountServiceImpl implements AccountService {

    @Autowired
    private AccountRepository accRepo;

    @Override
    public List<Account> getAll() {
        return accRepo.findAll();
    }

    @Override
    public Set<Account> getByAccountIDs(Set<UUID> uuidSet) {
        return accRepo.findByUidIn(uuidSet);
    }

    @Override
    public Account getByLogin(String login) {
        return accRepo.findByLogin(login);
    }

    @Override
    public Account add(Account acc) {
        acc.setUid(UUID.randomUUID());
        return accRepo.saveAndFlush(acc);
    }

    @Override
    public Account update(Account acc) {
        return accRepo.saveAndFlush(acc);
    }


    @Override
    public Account getByEmail(String email) {
        return accRepo.findByEmail(email);
    }


}