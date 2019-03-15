package com.quartet.inventorydemo.repository;

import com.quartet.inventorydemo.model.Role;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface RoleRepository extends JpaRepository<Role, UUID> {
    @Override
    List<Role> findAll(Sort sort);
    Role findByRoleID(UUID roleID);
}
