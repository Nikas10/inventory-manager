package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Role;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService {
    List<Role> getAll();

    Role getByRoleID(UUID roleID);

    Set<Role> getByRoleIDs(Set<UUID> uuidSet);

    Role getByRoleName(String roleName);
    Role add(Role role);
    Role update(Role role);

    void remove(Role role);
}
