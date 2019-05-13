package com.quartet.inventorydemo.service.impl;

import static java.util.Objects.isNull;

import com.quartet.inventorydemo.dto.RoleDTO;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.exception.UpdateNotSupportedException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.RoleRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.service.RoleService;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service("RoleService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class RoleServiceImpl implements RoleService {

  private final RoleRepository roleRepository;
  private final InventoryPositionService inventoryPositionService;

  @Autowired
  public RoleServiceImpl(
      @Qualifier("RoleRepository") final RoleRepository roleRepository,
      @Qualifier("InventoryPositionService") final InventoryPositionService inventoryPositionService) {
    this.roleRepository = roleRepository;
    this.inventoryPositionService = inventoryPositionService;
  }

  @Override
  public Set<Role> getAll() {
    return roleRepository.findAllToSet();
  }

  @Override
  public Optional<Role> getByRoleID(@NotNull @Valid UUID roleID) {
    return roleRepository.findById(roleID);
  }

  @Override
  public Optional<Role> getByRoleName(@NotBlank @Valid String roleName) {
    return roleRepository.findByName(roleName);
  }

  @Override
  public Set<Role> getByRoleIDs(@NotNull @Valid Set<UUID> uuidSet) {
    return roleRepository.findByIdIn(uuidSet);
  }

  @Override
  public Role add(@NotNull @Valid Role role) {
    if (isExists(role)) {
      throw new ResourceAlreadyExistsException("role with same name already exists");
    }
    Role newRole = new Role(role.getName(), role.getDescription());
    return roleRepository.saveAndFlush(newRole);
  }

  @Override
  public Role update(@NotNull @Valid UUID uuid, @NotNull @Valid RoleDTO role) {
    Role roleToModify =
        getByRoleID(uuid).orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));

    if (!isNull(role.getName())) {
      Optional<Role> validation = getByRoleName(role.getName());
      if (validation.isPresent() && !validation.get().getName().equals(roleToModify.getName())) {
        throw new UpdateNotSupportedException("Trying to set a name which is already taken!");
      }
      roleToModify.setName(role.getName());
    }

    if (!isNull(role.getDescription())) {
      roleToModify.setDescription(role.getDescription());
    }

    return roleRepository.saveAndFlush(roleToModify);
  }

  @Override
  public void remove(@NotNull @Valid UUID uuid) {
    Optional<Role> roleOptional = getByRoleID(uuid);
    Role roleToDelete =
        roleOptional.orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + uuid + "not found"));
    roleRepository.delete(roleToDelete);
  }

  @Override
  public Role addInventoryPosition(
      @NotNull @Valid UUID roleId, @NotNull @Valid UUID inventoryPositionId) {
    Role role =
        getByRoleID(roleId).orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + roleId + " not found"));
    InventoryPosition positionToAdd = inventoryPositionService.getByPositionID(inventoryPositionId)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                "Position with id: " + inventoryPositionId + "not found"));
    role.getInventoryPositions().add(positionToAdd);
    return roleRepository.saveAndFlush(role);
  }

  @Override
  public Role removeInventoryPosition(
      @NotNull @Valid UUID roleId, @NotNull @Valid UUID inventoryPositionId) {
    Role role =
        getByRoleID(roleId).orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + roleId + " not found"));
    InventoryPosition positionToRemove = inventoryPositionService
        .getByPositionID(inventoryPositionId)
        .orElseThrow(
            () -> new ResourceNotFoundException(
                "Position with id: " + inventoryPositionId + "not found"));
    role.getInventoryPositions().remove(positionToRemove);
    return roleRepository.saveAndFlush(role);
  }

  @Override
  public Set<UUID> getNotAllowedInventoryPositionIds(
      @NotNull @Valid UUID holderId, @NotNull @Valid Set<UUID> inventoryPositionIds) {
    Set<Role> rolesByHolderId =
        roleRepository.findByHolders_Id(holderId); // can delegate to holder service
    Set<UUID> allowedIds =
        rolesByHolderId.stream()
            .map(Role::getInventoryPositions)
            .flatMap(Collection::stream)
            .map(InventoryPosition::getId)
            .collect(Collectors.toSet());
    HashSet<UUID> idsToBeRestricted = new HashSet<>(inventoryPositionIds);
    idsToBeRestricted.removeAll(allowedIds);
    return idsToBeRestricted;
  }

  private boolean isExists(@NotNull @Valid Role role) {
    ExampleMatcher nameIgnoreSensitivityMatcher =
        ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
            .withIgnorePaths("id", "description", "inventoryPositions", "holders");
    Example<Role> roleExample = Example.of(role, nameIgnoreSensitivityMatcher);
    Optional<Role> alreadyExists = roleRepository.findOne(roleExample);

    return alreadyExists.isPresent();
  }

  private void checkPositionsPresence(Set<InventoryPosition> rolePositions,
      Collection<InventoryPosition> positionsToAdd) {
    for (InventoryPosition currentPosition : positionsToAdd) {
      if (rolePositions.contains(currentPosition)) {
        throw new ResourceAlreadyExistsException(
            "Position with id: " + currentPosition.getId() + " already exists for selected role.");
      }
    }
  }
}
