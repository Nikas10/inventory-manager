package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.Account;
import com.quartet.inventorydemo.repository.AccountRepository;
import com.quartet.inventorydemo.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
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
        return accRepo.findByIdIn(uuidSet);
    }

    @Override
    public Account getByLogin(String login) {
        Optional<Account> byLogin = accRepo.findByLogin(login);
        byLogin.orElseThrow(RuntimeException::new);
        return byLogin.get();
    }

    @Override
    public Account add(Account acc) {
        return accRepo.saveAndFlush(acc);
    }

    @Override
    public Account update(Account acc) {
        return accRepo.saveAndFlush(acc);
    }


    @Override
    public Account getByEmail(String email) {
        Optional<Account> byEmail = accRepo.findByEmail(email);
        byEmail.orElseThrow(RuntimeException::new);
        return byEmail.get();
    }


}