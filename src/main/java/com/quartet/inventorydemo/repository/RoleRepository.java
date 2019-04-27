package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository("RoleRepository")
public interface RoleRepository extends JpaRepository<Role, UUID> {
    default Set<Role> findAllToSet() {
        return new HashSet<>(findAll());
    }

    Optional<Role> findByName(String name);

    Set<Role> findByIdIn(Iterable<UUID> ids);
}
