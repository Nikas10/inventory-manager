package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.validation.annotation.Validated;

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

    @Validated(OnCreate.class)
    Role add(@NotNull @Valid Role role);

    @Validated(OnUpdate.class)
    Role update(@NotNull @Valid Role role);

    @Validated(OnUpdate.class)
    void remove(@NotNull @Valid Role role);
}
