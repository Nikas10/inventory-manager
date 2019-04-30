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
    public Holder getByHolderID(@NotNull @Valid UUID holderID) {
        Optional<Holder> byId = inventoryHolderRepository.findById(holderID);
        byId.orElseThrow(ResourceNotFoundException::new);
        return byId.get();
    }

    @Override
    public Set<Holder> getByHolderIDs(@NotNull @Valid Set<UUID> holderIDs) {
        return inventoryHolderRepository.findByIdIn(holderIDs);
    }

    @Override
    public Holder getByHolderName(@NotBlank @Valid String holderName) {
        Optional<Holder> byName = inventoryHolderRepository.findByName(holderName);
        byName.orElseThrow(ResourceNotFoundException::new);
        return byName.get();
    }

    @Validated(IdNull.class)
    @Override
    public Holder add(@NotNull @Valid Holder holder) {
        ExampleMatcher nameIgnoreSensitivityMatcher = ExampleMatcher.matchingAny()
                .withMatcher("name", ExampleMatcher.GenericPropertyMatchers.ignoreCase())
                .withIgnorePaths("id", "description", "roles", "accounts", "inventoryItems");
        Example<Holder> holderExample = Example.of(holder, nameIgnoreSensitivityMatcher);
        Optional<Holder> alreadyExists = inventoryHolderRepository.findOne(holderExample);

        alreadyExists.ifPresent(e -> {
            throw new ResourceAlreadyExistsException("holder with same name already exists");
        });

        return alreadyExists.orElseGet(() -> {
            Holder newHolder = new Holder(holder.getName(), holder.getDescription());
            return inventoryHolderRepository.saveAndFlush(newHolder);
        });
    }

    @Validated(IdNotNull.class)
    @Override
    public Holder update(@NotNull @Valid Holder holder) {
        return inventoryHolderRepository.saveAndFlush(holder);
    }

    @Validated(IdNotNull.class)
    @Override
    public void remove(@NotNull @Valid Holder holder) {
        Set<InventoryItem> inventoryItems = holder.getInventoryItems();
        if (!inventoryItems.isEmpty()) {
            throw new DeletionNotSupportedException("can not delete holder, while it holds any items");
        }
        inventoryHolderRepository.delete(holder);
    }
}
