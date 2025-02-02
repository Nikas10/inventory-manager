package com.quartet.inventorydemo.service;

import com.quartet.inventorydemo.dto.RoleDTO;
import com.quartet.inventorydemo.model.Role;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public interface RoleService {

  Collection<Role> getAll();

  Optional<Role> getByRoleID(@NotNull @Valid UUID roleID);

  Optional<Role> getByRoleName(@NotBlank @Valid String roleName);

  Collection<Role> getByRoleIDs(@NotNull @Valid Set<UUID> uuidSet);

  Role add(@NotNull @Valid Role role);

  Role update(@NotNull @Valid UUID uuid, @NotNull @Valid RoleDTO role);

  void remove(@NotNull @Valid UUID uuid);

  Role addInventoryPosition(
      @NotNull @Valid UUID roleId, @NotNull @Valid UUID inventoryPositionId);

  Role removeInventoryPosition(
      @NotNull @Valid UUID roleId, @NotNull @Valid UUID inventoryPositionId);

  Set<UUID> getNotAllowedInventoryPositionIds(
      @NotNull @Valid UUID holderId, @NotNull @Valid Set<UUID> inventoryPositionIds);
}
