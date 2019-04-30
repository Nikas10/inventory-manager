package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Role;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Collection;
import java.util.Set;
import java.util.UUID;

public interface RoleService {

    Collection<Role> getAll();

    Role getByRoleID(@NotNull @Valid UUID roleID);

    Role getByRoleName(@NotBlank @Valid String roleName);

    Collection<Role> getByRoleIDs(@NotNull @Valid Set<UUID> uuidSet);

    Role add(@NotNull @Valid Role role);

    Role update(@NotNull @Valid Role role);

    void remove(@NotNull @Valid Role role);
}
