package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("InventoryPositionService")
@Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {

    @Autowired
    private InventoryPositionRepository positionRepo;

    @Override
    public List<InventoryPosition> getAll() {
        return positionRepo.findAll();
    }

    @Override
    public Set<InventoryPosition> getByPositionIDs(Set<UUID> positionIDs) {
        return positionRepo.findByIdIn(positionIDs);
    }

    @Override
    public InventoryPosition getByPositionID(UUID positionID) {
        Optional<InventoryPosition> byId = positionRepo.findById(positionID);
        byId.orElseThrow(RuntimeException::new);
        return byId.get();
    }

    @Override
    public InventoryPosition getByName(String name) {
        Optional<InventoryPosition> byName = positionRepo.findByName(name);
        byName.orElseThrow(RuntimeException::new);
        return byName.get();
    }

    @Override
    public InventoryPosition add(InventoryPosition position) {
        return positionRepo.saveAndFlush(position);
    }

    @Override
    public InventoryPosition update(InventoryPosition position) {
        return positionRepo.saveAndFlush(position);
    }
}
