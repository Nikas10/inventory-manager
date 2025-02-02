package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.dto.HolderDTO;
import com.quartet.inventorydemo.dto.InventoryItemDTOFromHolder;
import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.exception.UpdateNotSupportedException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.RoleService;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

@Service("HolderService")
@Validated
@Transactional
public class HolderServiceImpl implements HolderService, InitializingBean {

  private final String STORAGE_NAME = "STORAGE";
  private final String STORAGE_DESCRIPTION = "DEFAULT STORAGE THAT CONTAINS INVENTORY ITEMS";
  private final InventoryHolderRepository inventoryHolderRepository;
  private final RoleService roleService;

  @Autowired
  public HolderServiceImpl(
      @Qualifier("InventoryHolderRepository") final InventoryHolderRepository inventoryHolderRepository,
      @Qualifier("RoleService") final RoleService roleService) {
    this.inventoryHolderRepository = inventoryHolderRepository;
    this.roleService = roleService;
  }

  @Override
  public Set<Holder> getAll() {
    return inventoryHolderRepository.findAllToSet();
  }

  @Override
  public Optional<Holder> getByHolderID(@NotNull @Valid UUID holderID) {
    return inventoryHolderRepository.findById(holderID);
  }

  @Override
  public Optional<Holder> getByHolderName(@NotBlank @Valid String holderName) {
    return inventoryHolderRepository.findByName(holderName);
  }

  @Override
  public Set<Holder> getByHolderIDs(@NotNull @Valid Set<UUID> holderIDs) {
    return inventoryHolderRepository.findByIdIn(holderIDs);
  }

  @Override
  public Holder getStorageHolder() {
    Optional<Holder> storageOptional = getByHolderName(STORAGE_NAME);
    return storageOptional.orElseGet(() -> add(STORAGE_DESCRIPTION, STORAGE_NAME));
  }

  @Override
  public Holder add(@NotNull @Valid String description, @NotNull @Valid String name) {
    Optional<Holder> optionalHolder = getByHolderName(name);

    if (optionalHolder.isPresent()) {
      throw new ResourceNotFoundException("Holder with name: " + name + " already exists.");
    }

    Holder newHolder = new Holder(name, description);
    return inventoryHolderRepository.saveAndFlush(newHolder);
  }

  @Override
  public Holder update(
      @NotNull @Valid UUID uuid, @NotNull @Valid String description, @NotNull @Valid String name) {
    Optional<Holder> holderOptional = getByHolderID(uuid);

    Holder holderToModified =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: " + uuid + " not found"));
    if (holderToModified.equals(getStorageHolder())) {
      throw new UpdateNotSupportedException("Can not modify storage");
    }

    holderToModified.setDescription(description);
    holderToModified.setName(name);
    return inventoryHolderRepository.saveAndFlush(holderToModified);
  }

  @Override
  public void remove(@NotNull @Valid UUID uuid) {
    Optional<Holder> holderOptional = getByHolderID(uuid);
    Holder holder =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: " + uuid + " not found"));
    Set<InventoryItem> inventoryItems = holder.getInventoryItems();
    if (holder.equals(getStorageHolder())) {
      throw new DeletionNotSupportedException("can not delete storage");
    }
    if (!inventoryItems.isEmpty()) {
      throw new DeletionNotSupportedException("can not delete holder, while it holds any items");
    }
    inventoryHolderRepository.delete(holder);
  }

  @Override
  public Holder addRole(@NotNull @Valid UUID holderId, @NotNull @Valid UUID roleId) {
    Holder holderWithRoles =
        getByHolderID(holderId).orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: " + holderId + " is not found"));
    Role roleToAdd = roleService.getByRoleID(roleId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + roleId + " is not found"));
    holderWithRoles.getRoles().add(roleToAdd);
    return inventoryHolderRepository.saveAndFlush(holderWithRoles);
  }

  @Override
  public Holder removeRole(@NotNull @Valid UUID holderId, @NotNull @Valid UUID roleId) {
    Holder holderWithRoles =
        getByHolderID(holderId).orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: " + holderId + " is not found"));
    Role roleToAdd = roleService.getByRoleID(roleId)
        .orElseThrow(
            () -> new ResourceNotFoundException("Role with id: " + roleId + " is not found"));
    holderWithRoles.getRoles().remove(roleToAdd);
    return inventoryHolderRepository.saveAndFlush(holderWithRoles);
  }

  @Override
  public Collection<InventoryItemDTOFromHolder> getHolderItems(@NotNull @Valid UUID holderId) {
    Optional<Holder> holderOptional = getByHolderID(holderId);
    Holder holderWithItems =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: " + holderId + " not found"));

    Set<InventoryItem> holderInventoryItems = holderWithItems.getInventoryItems();
    Collection<InventoryItemDTOFromHolder> holderItemsWithPositionName = new ArrayList<>();
    for (InventoryItem current : holderInventoryItems) {
      String positionName = current.getInventoryPosition().getName();
      UUID id = current.getInventoryPosition().getId();
      Integer amount = current.getAmount();
      String status = current.getStatus();
      holderItemsWithPositionName
          .add(new InventoryItemDTOFromHolder(id, positionName, status, amount));
    }

    return holderItemsWithPositionName;
  }

  @Override
  public Holder update(@NotNull @Valid UUID holderId, @NotNull @Valid HolderDTO holderDTO) {
    Optional<Holder> holderOptional = getByHolderID(holderId);
    Holder holderToUpdate =
        holderOptional.orElseThrow(
            () -> new ResourceNotFoundException("Holder with id: " + holderId + " not found"));

    if ((holderDTO.getName() != null) &&
        !"".equals(holderDTO.getName())) {
      holderToUpdate.setName(holderDTO.getName());
    }

    if ((holderDTO.getDescription() != null) &&
        !"".equals(holderDTO.getDescription())) {
      holderToUpdate.setDescription(holderDTO.getDescription());
    }

    return inventoryHolderRepository.saveAndFlush(holderToUpdate);
  }

  private boolean isExists(@NotNull @Valid Holder holder) {
    ExampleMatcher nameIgnoreSensitivityMatcher =
        ExampleMatcher.matchingAny()
            .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
            .withIgnorePaths("id", "description", "roles", "accounts", "inventoryItems");
    Example<Holder> holderExample = Example.of(holder, nameIgnoreSensitivityMatcher);
    Optional<Holder> alreadyExists = inventoryHolderRepository.findOne(holderExample);

    return alreadyExists.isPresent();
  }

  @Override
  public void afterPropertiesSet() throws Exception {
    getStorageHolder();
  }

  @Override
  public Set<InventoryPosition> getAvailablePositions(@NotNull @Valid UUID holderId) {
    Holder selectedHolder = inventoryHolderRepository.findById(holderId)
        .orElseThrow(() -> new ResourceNotFoundException("Account with id: "
            + holderId
            + "not found.")
        );

    Set<InventoryPosition> availablePositions = selectedHolder.getRoles()
        .parallelStream()
        .flatMap(e -> e.getInventoryPositions().stream())
        .collect(Collectors.toSet());
    return availablePositions;
  }

  private void checkRolePresence(Set<Role> holderRoles, Collection<Role> rolesToAdd) {
    for (Role currentRole : rolesToAdd) {
      if (holderRoles.contains(currentRole)) {
        throw new ResourceAlreadyExistsException(
            "Role with id: " + currentRole.getId() + " already exists for selected holder.");
      }
    }
  }
}
