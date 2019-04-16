package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.model.InventoryHolder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.InventoryHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service("InventoryHolderService")
public class InventoryHolderServiceImpl implements InventoryHolderService {

    @Autowired
    private InventoryHolderRepository invHoldRepo;

    @Override
    public List<InventoryHolder> getAll() {
        return invHoldRepo.findAll();
    }

    @Override
    public InventoryHolder getByHolderID(UUID holderID) {
        return invHoldRepo.findByHolderID(holderID);
    }

    @Override
    public Set<InventoryHolder> getByHolderIDs(Set<UUID> holderIDs) {
        return invHoldRepo.findByHolderIDIn(holderIDs);
    }

    @Override
    public List<InventoryHolder> getByHolderName(String holderName) {
        return invHoldRepo.findByName(holderName);
    }

    @Override
    public InventoryHolder add(InventoryHolder holder) {
        holder.setHolderID(UUID.randomUUID());
        return invHoldRepo.saveAndFlush(holder);
    }

    @Override
    public InventoryHolder update(InventoryHolder holder) {
        return invHoldRepo.saveAndFlush(holder);
    }

    @Override
    public void remove(InventoryHolder holder) {
        Set<InventoryItem> holdedItems = holder.getHoldedItems();
        if (!holdedItems.isEmpty()) {
            throw new DeletionNotSupportedException("can not delete holder, while it holds any items");
        }
        invHoldRepo.delete(holder);
    }
}
