package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceAlreadyExistsException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.HolderService;
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
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("HolderService")
@Validated
@Transactional
public class HolderServiceImpl implements HolderService {
    private final InventoryHolderRepository inventoryHolderRepository;

    @Autowired
    public HolderServiceImpl(@Qualifier("InventoryHolderRepository") final InventoryHolderRepository inventoryHolderRepository) {
        this.inventoryHolderRepository = inventoryHolderRepository;
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
        holderOptional.orElseThrow(() -> new ResourceNotFoundException("Holder with id: " + uuid + " not found"));

        if (isExists(holder)) {
            throw new ResourceAlreadyExistsException("holder with same name already exists");
        }

        holderOptional.ifPresent(e -> BeanUtils.copyProperties(holder, e, "id", "roles", "accounts", "inventoryItems"));
        Holder modifiedHolder = holderOptional.get();
        return inventoryHolderRepository.saveAndFlush(modifiedHolder);
    }

    @Override
    public void remove(@NotNull @Valid UUID uuid) {
        Optional<Holder> holderOptional = getByHolderID(uuid);
        holderOptional.orElseThrow(() -> new ResourceNotFoundException("Holder with id: " + uuid + " not found"));
        Holder holder = holderOptional.get();
        Set<InventoryItem> inventoryItems = holder.getInventoryItems();

        if (!inventoryItems.isEmpty()) {
            throw new DeletionNotSupportedException("can not delete holder, while it holds any items");
        }
        inventoryHolderRepository.delete(holder);
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
