package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface AccountRepository extends JpaRepository<Account, UUID> {
    List<Account> findAll();

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);

    Set<Account> findByIdIn(Iterable<UUID> ids);
}