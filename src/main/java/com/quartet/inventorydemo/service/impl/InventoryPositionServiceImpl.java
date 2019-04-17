package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.model.InventoryPosition;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.repository.InventoryItemRepository;
import com.quartet.inventorydemo.repository.InventoryPositionRepository;
import com.quartet.inventorydemo.service.InventoryPositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service("InventoryPositionService")
@Transactional
public class InventoryPositionServiceImpl implements InventoryPositionService {

    @Autowired
    private InventoryPositionRepository positionRepo;

    @Autowired
    private InventoryItemRepository inventoryItemRepo;

    @Autowired
    private InventoryHolderRepository inventoryHolderRepo;

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
    public InventoryPosition getByName(String name) {
        return positionRepo.findByName(name);
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

    @Override
    public void remove(InventoryPosition position) {
        List<InventoryItem> itemsWithSelectedPosition = inventoryItemRepo.findByInventoryPosition(position);
        Set<UUID> holderIDs = new HashSet<>();

        if (itemsWithSelectedPosition == null) {
            positionRepo.delete(position);
            return;
        }

        for (InventoryItem current: itemsWithSelectedPosition) {
            holderIDs.add(current.getInventoryHolder().getHolderID());
        }

        List<InventoryHolder> holdersWithAccount = inventoryHolderRepo.findHolderWithAccount();

        for (InventoryHolder current: holdersWithAccount) {
            if (holderIDs.contains(current.getHolderID())) {
                //Not delete
                return;
            }
        }

        positionRepo.delete(position);

    }
}
