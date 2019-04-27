package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import com.quartet.inventorydemo.util.OnCreate;
import com.quartet.inventorydemo.util.OnUpdate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("InventoryPositionService")
@Validated
@org.springframework.transaction.annotation.Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {
    private final InventoryPositionRepository inventoryPositionRepository;

    @Autowired
    public InventoryPositionServiceImpl(@Qualifier("InventoryPositionRepository") final InventoryPositionRepository inventoryPositionRepository) {
        this.inventoryPositionRepository = inventoryPositionRepository;
    }

    @Override
    public List<InventoryPosition> getAll() {
        return inventoryPositionRepository.findAll();
    }

    @Override
    public InventoryPosition getByPositionID(@NotNull @Valid UUID positionID) {
        Optional<InventoryPosition> byId = inventoryPositionRepository.findById(positionID);
        byId.orElseThrow(ResourceNotFoundException::new);
        return byId.get();
    }

    @Override
    public InventoryPosition getByName(@NotBlank @Valid String name) {
        Optional<InventoryPosition> byName = inventoryPositionRepository.findByName(name);
        byName.orElseThrow(ResourceNotFoundException::new);
        return byName.get();
    }

    @Override
    public Set<InventoryPosition> getByPositionIDs(@NotNull @Valid Set<UUID> positionIDs) {
        return inventoryPositionRepository.findByIdIn(positionIDs);
    }

    @Validated(OnCreate.class)
    @Override
    public InventoryPosition add(@NotNull @Valid InventoryPosition position) {
        return inventoryPositionRepository.saveAndFlush(position);
    }

    @Validated(OnUpdate.class)
    @Override
    public InventoryPosition update(@NotNull @Valid InventoryPosition position) {
        return inventoryPositionRepository.saveAndFlush(position);
    }

    @Validated(OnUpdate.class)
    @Override
    public void remove(@NotNull @Valid InventoryPosition inventoryPosition) {

    }
}
