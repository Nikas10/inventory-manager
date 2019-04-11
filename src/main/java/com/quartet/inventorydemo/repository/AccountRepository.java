package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAll();

    Set<Account> findAll(Iterable<UUID> ids);
    Account findByLogin(String login);
    Account findByEmail(String email);
}