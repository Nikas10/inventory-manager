package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
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
        return positionRepo.findByPositionIDIn(positionIDs);
    }

    @Override
    public InventoryPosition getByPositionID(UUID positionID) {
        return positionRepo.findByPositionID(positionID);
    }

    @Override
    public InventoryPosition add(InventoryPosition position) {
        position.setPositionID(UUID.randomUUID());
        return positionRepo.saveAndFlush(position);
    }

    @Override
    public InventoryPosition update(InventoryPosition position) {
        return positionRepo.saveAndFlush(position);
    }
}
