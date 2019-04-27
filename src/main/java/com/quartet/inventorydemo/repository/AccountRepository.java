package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository("AccountRepository")
public interface AccountRepository extends JpaRepository<Account, UUID> {
    default Set<Account> findAllToSet() {
        return new HashSet<>(findAll());
    }

    Optional<Account> findByLogin(String login);

    Optional<Account> findByEmail(String email);

    Set<Account> findByIdIn(Iterable<UUID> ids);
}