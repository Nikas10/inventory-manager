package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.Role;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.HolderService;
import com.quartet.inventorydemo.service.RoleService;
import com.quartet.inventorydemo.util.IdNull;
import com.quartet.inventorydemo.util.IdNotNull;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.*;

@Service("HolderService")
@Validated
@Transactional
public class HolderServiceImpl implements HolderService {
    private final InventoryHolderRepository inventoryHolderRepository;
    private final RoleService roleService;


    @Autowired
    public HolderServiceImpl(@Qualifier("InventoryHolderRepository") final InventoryHolderRepository inventoryHolderRepository,
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
    public Holder add(@NotNull @Valid Holder holder) {
        if (isExists(holder)) {
            throw new ResourceAlreadyExistsException("holder with same name already exists");
        }
        Holder newHolder = new Holder(holder.getName(), holder.getDescription());
        return inventoryHolderRepository.saveAndFlush(newHolder);
    }

    @Override
    public Holder update(@NotNull @Valid UUID uuid, @NotNull @Valid Holder holder) {
        Optional<Holder> holderOptional = getByHolderID(uuid);
        if (isExists(holder)) {
            throw new ResourceAlreadyExistsException("holder with same name already exists");
        }
        Holder holderToModified = holderOptional.orElseThrow(() -> new ResourceNotFoundException("Holder with id: " + uuid + " not found"));
        BeanUtils.copyProperties(holder, holderToModified, "id", "roles", "accounts", "inventoryItems");
        return inventoryHolderRepository.saveAndFlush(holderToModified);
    }

    @Override
    public void remove(@NotNull @Valid UUID uuid) {
        Optional<Holder> holderOptional = getByHolderID(uuid);
        Holder holder = holderOptional.orElseThrow(() -> new ResourceNotFoundException("Holder with id: " + uuid + " not found"));
        Set<InventoryItem> inventoryItems = holder.getInventoryItems();

        if (!inventoryItems.isEmpty()) {
            throw new DeletionNotSupportedException("can not delete holder, while it holds any items");
        }
        inventoryHolderRepository.delete(holder);
    }

    @Override
    public Holder addRoles(@NotNull @Valid UUID holderId, @NotNull @Valid Set<UUID> roleIds) {
        Optional<Holder> holderOptional = getByHolderID(holderId);
        Holder holderWithRoles = holderOptional.orElseThrow(() -> new ResourceNotFoundException("Holder with id: " + holderId + " not found"));

        Set<Role> currentRoles = holderWithRoles.getRoles();
        currentRoles.addAll(roleService.getByRoleIDs(roleIds));

        return inventoryHolderRepository.saveAndFlush(holderWithRoles);
    }

    @Override
    public Holder removeRoles(@NotNull @Valid UUID holderId, @NotNull @Valid Set<UUID> roleIds) {
        Optional<Holder> holderOptional = getByHolderID(holderId);
        Holder holderWithRoles = holderOptional.orElseThrow(() -> new ResourceNotFoundException("Holder with id: " + holderId + " not found"));

        Set<Role> currentRoles = holderWithRoles.getRoles();
        currentRoles.removeAll(roleService.getByRoleIDs(roleIds));

        return inventoryHolderRepository.saveAndFlush(holderWithRoles);
    }

    private boolean isExists(@NotNull @Valid Holder holder) {
        ExampleMatcher nameIgnoreSensitivityMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id", "description", "roles", "accounts", "inventoryItems");
        Example<Holder> holderExample = Example.of(holder, nameIgnoreSensitivityMatcher);
        Optional<Holder> alreadyExists = inventoryHolderRepository.findOne(holderExample);

        return alreadyExists.isPresent();
    }
}
