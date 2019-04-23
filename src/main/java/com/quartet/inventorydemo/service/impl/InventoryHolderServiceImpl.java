package com.quartet.inventorydemo.service.impl;

import com.quartet.inventorydemo.exception.DeletionNotSupportedException;
import com.quartet.inventorydemo.exception.ResourceNotFoundException;
import com.quartet.inventorydemo.model.Holder;
import com.quartet.inventorydemo.model.InventoryItem;
import com.quartet.inventorydemo.repository.InventoryHolderRepository;
import com.quartet.inventorydemo.service.InventoryHolderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Service("InventoryHolderService")
public class InventoryHolderServiceImpl implements InventoryHolderService {

    @Autowired
    private InventoryHolderRepository invHoldRepo;

    @Override
    public List<Holder> getAll() {
        return invHoldRepo.findAll();
    }

    @Override
    public Holder getByHolderID(UUID holderID) {
        Optional<Holder> byId = invHoldRepo.findById(holderID);
        byId.orElseThrow(ResourceNotFoundException::new);
        return byId.get();
    }

    @Override
    public Set<Holder> getByHolderIDs(Set<UUID> holderIDs) {
        return invHoldRepo.findByIdIn(holderIDs);
    }

    @Override
    public Holder getByHolderName(String holderName) {
        Optional<Holder> byName = invHoldRepo.findByName(holderName);
        byName.orElseThrow(ResourceNotFoundException::new);
        return byName.get();
    }

    @Override
    public Holder add(Holder holder) {
        return invHoldRepo.saveAndFlush(holder);
    }

    @Override
    public Holder update(Holder holder) {
        return invHoldRepo.saveAndFlush(holder);
    }

    @Override
    public void remove(Holder holder) {
        Set<InventoryItem> inventoryItems = holder.getInventoryItems();
        if (!inventoryItems.isEmpty()) {
            throw new DeletionNotSupportedException("can not delete holder, while it holds any items");
        }
        invHoldRepo.delete(holder);
    }
}
