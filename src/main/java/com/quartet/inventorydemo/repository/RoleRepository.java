package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    List<Role> findAll();

    Optional<Role> findByName(String name);

    Set<Role> findByIdIn(Iterable<UUID> ids);
}
